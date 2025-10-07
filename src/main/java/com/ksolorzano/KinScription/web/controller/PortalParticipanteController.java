package com.ksolorzano.KinScription.web.controller;

import com.ksolorzano.KinScription.dominio.repository.AdmDocumentoRequeridoRepository;
import com.ksolorzano.KinScription.dominio.repository.AlumnoRepository;
import com.ksolorzano.KinScription.dominio.service.AdmContratoService;
import com.ksolorzano.KinScription.dominio.service.AdmEstudioSocioeconomicoService;
import com.ksolorzano.KinScription.dominio.service.AdmParticipanteService;
import com.ksolorzano.KinScription.dominio.service.FileSystemStorageService;
import com.ksolorzano.KinScription.persistence.entity.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/portal")
public class PortalParticipanteController {

    private final AdmParticipanteService participanteService;
    private final AlumnoRepository alumnoRepository;
    private final AdmEstudioSocioeconomicoService estudioSocioeconomicoService;
    private final FileSystemStorageService storageService;
    private final AdmDocumentoRequeridoRepository documentoRepository;
    private final AdmContratoService contratoService;

    @Autowired
    public PortalParticipanteController(AdmParticipanteService ps, AlumnoRepository ar, AdmEstudioSocioeconomicoService es, FileSystemStorageService ss, AdmDocumentoRequeridoRepository dr, AdmContratoService cs) {
        this.participanteService = ps;
        this.alumnoRepository = ar;
        this.estudioSocioeconomicoService = es;
        this.storageService = ss;
        this.documentoRepository = dr;
        this.contratoService = cs;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        AdmParticipante participante = participanteService.getByUsername(username)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        int progressPercentage = calculateProgress(participante.getEstado());

        model.addAttribute("participante", participante);
        model.addAttribute("progressPercentage", progressPercentage);

        if (participante.getEstado() == EstadoParticipante.FINALIZADO) {
            alumnoRepository.findByAdmParticipante_Id(participante.getId())
                    .ifPresent(alumno -> model.addAttribute("alumno", alumno));
        }

        return "portal/dashboard";
    }

    /**
     * Muestra la página de simulación del examen de admisión.
     *
     * @param model          El modelo para pasar datos a la vista.
     * @param authentication Contiene la información del usuario autenticado.
     * @return El nombre de la vista "portal/examen".
     */
    @GetMapping("/examen")
    public String mostrarPaginaExamen(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AdmParticipante participante = participanteService.getByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        model.addAttribute("participante", participante);
        return "portal/examen";
    }

    /**
     * Procesa la "finalización" del examen de simulación.
     * En un entorno real, aquí se guardaría el examen. En nuestra simulación,
     * simplemente redirige al dashboard, donde el participante esperará la calificación.
     *
     * @param authentication Contiene la información del usuario.
     * @return Una redirección al dashboard del portal.
     */
    @PostMapping("/examen/completar")
    public String completarExamen(Authentication authentication, RedirectAttributes redirectAttributes) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AdmParticipante participante = participanteService.getByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        if (participante.getEstado() == EstadoParticipante.PENDIENTE_EXAMEN) {
            participante.setEstado(EstadoParticipante.EXAMEN_REALIZADO);
            participanteService.save(participante);
            redirectAttributes.addFlashAttribute("successMessage", "¡Felicidades por completar el examen!");
        }

        return "redirect:/portal/dashboard";
    }

    @GetMapping("/socioeconomico")
    public String mostrarFormularioSocioeconomico(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AdmParticipante participante = participanteService.getByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        model.addAttribute("participante", participante);
        model.addAttribute("estudioSocioeconomico", new AdmEstudioSocioeconomico());

        return "portal/socioeconomico";
    }

    @PostMapping("/socioeconomico/guardar")
    public String guardarSocioeconomico(
            @Valid @ModelAttribute("estudioSocioeconomico") AdmEstudioSocioeconomico estudio,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model) {

        AdmParticipante participante = participanteService.getByUsername(authentication.getName()).orElseThrow();

        if (bindingResult.hasErrors()) {
            model.addAttribute("participante", participante);

            return "portal/socioeconomico";
        }

        if (participante.getEstado() == EstadoParticipante.ADMITIDO_EXAMEN) {
            estudio.setParticipante(participante);
            estudioSocioeconomicoService.save(estudio);

            participante.setEstado(EstadoParticipante.SOCIOECONOMICO_ENVIADO);
            participanteService.save(participante);

            redirectAttributes.addFlashAttribute("successMessage", "Formulario enviado correctamente. Está en proceso de revisión.");
        }

        return "redirect:/portal/dashboard";
    }

    @GetMapping("/papeleria")
    public String mostrarFormularioPapeleria(Model model, Authentication authentication) {
        AdmParticipante participante = participanteService.getByUsername(authentication.getName()).orElseThrow();

        List<String> documentosRequeridos = List.of("Fe de Edad", "Acta de Nacimiento", "Certificado 3ro Básico");

        // Documentos que el usuario ya ha subido
        List<AdmDocumentoRequerido> documentosSubidos = documentoRepository.findByParticipante(participante);
        Map<String, String> mapaSubidos = documentosSubidos.stream()
                .collect(Collectors.toMap(AdmDocumentoRequerido::getNombreDocumento, AdmDocumentoRequerido::getUrlArchivo));

        model.addAttribute("participante", participante);
        model.addAttribute("documentosRequeridos", documentosRequeridos);
        model.addAttribute("documentosSubidos", mapaSubidos);

        return "portal/papeleria";
    }

    @PostMapping("/papeleria/subir")
    public String subirDocumento(@RequestParam("file") MultipartFile file,
                                 @RequestParam("documentoNombre") String documentoNombre,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {

        AdmParticipante participante = participanteService.getByUsername(authentication.getName()).orElseThrow();
        String filename = storageService.store(file);

        AdmDocumentoRequerido doc = new AdmDocumentoRequerido();
        doc.setParticipante(participante);
        doc.setNombreDocumento(documentoNombre);
        doc.setUrlArchivo(filename);
        doc.setEstadoRevision("PENDIENTE");
        documentoRepository.save(doc);

        redirectAttributes.addFlashAttribute("successMessage", "Documento '" + documentoNombre + "' subido correctamente.");
        return "redirect:/portal/papeleria";
    }

    @PostMapping("/papeleria/finalizar")
    public String finalizarPapeleria(Authentication authentication, RedirectAttributes redirectAttributes) {
        AdmParticipante participante = participanteService.getByUsername(authentication.getName()).orElseThrow();
        if (participante.getEstado() == EstadoParticipante.ADMITIDO_FORMULARIO) {
            participante.setEstado(EstadoParticipante.PAPELERIA_ENVIADA);
            participanteService.save(participante);
            redirectAttributes.addFlashAttribute("successMessage", "Toda tu papelería ha sido enviada para revisión.");
        }
        return "redirect:/portal/dashboard";
    }

    @GetMapping("/contrato")
    public String mostrarFormularioContrato(Model model, Authentication authentication) {
        AdmParticipante participante = participanteService.getByUsername(authentication.getName()).orElseThrow();
        model.addAttribute("participante", participante); // Para el layout
        return "portal/contrato";
    }

    @PostMapping("/contrato/subir")
    public String subirContrato(@RequestParam("file") MultipartFile file,
                                @RequestParam("nombreAbogado") String nombreAbogado,
                                @RequestParam("colegiadoAbogado") String colegiadoAbogado,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {

        AdmParticipante participante = participanteService.getByUsername(authentication.getName()).orElseThrow();

        if (participante.getEstado() == EstadoParticipante.ADMITIDO_PAPELERIA) {
            String filename = storageService.store(file);

            AdmContrato contrato = new AdmContrato();
            contrato.setParticipante(participante);
            contrato.setNombreAbogado(nombreAbogado);
            contrato.setColegiadoAbogado(colegiadoAbogado);
            contrato.setUrlPdfContrato(filename);
            contrato.setAprobado(false);
            contratoService.save(contrato);

            // Actualizar el estado del participante
            participante.setEstado(EstadoParticipante.CONTRATO_ENVIADO);
            participanteService.save(participante);

            redirectAttributes.addFlashAttribute("successMessage", "Contrato subido correctamente. Está en proceso de revisión final.");
        }

        return "redirect:/portal/dashboard";
    }

    private int calculateProgress(EstadoParticipante estado) {
        return switch (estado) {
            case PENDIENTE_EXAMEN -> 0;
            case EXAMEN_REALIZADO -> 10;
            case ADMITIDO_EXAMEN -> 30;
            case SOCIOECONOMICO_ENVIADO -> 30;
            case ADMITIDO_SOCIOECONOMICO -> 40;
            case ADMITIDO_FORMULARIO, PAPELERIA_ENVIADA -> 50;
            case ADMITIDO_PAPELERIA, CONTRATO_ENVIADO -> 70;
            case ADMITIDO_CONTRATO -> 90;
            case FINALIZADO -> 100;
            default -> 0;
        };
    }
}