package com.ksolorzano.KinScription.web.controller;

import com.ksolorzano.KinScription.dominio.repository.AlumnoRepository;
import com.ksolorzano.KinScription.dominio.service.AdmEstudioSocioeconomicoService;
import com.ksolorzano.KinScription.dominio.service.AdmParticipanteService;
import com.ksolorzano.KinScription.persistence.entity.AdmEstudioSocioeconomico;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/portal")
public class PortalParticipanteController {

    private final AdmParticipanteService participanteService;
    private final AlumnoRepository alumnoRepository;
    private final AdmEstudioSocioeconomicoService estudioSocioeconomicoService;

    @Autowired
    public PortalParticipanteController(AdmParticipanteService ps, AlumnoRepository ar, AdmEstudioSocioeconomicoService es) {
        this.participanteService = ps;
        this.alumnoRepository = ar;
        this.estudioSocioeconomicoService = es;
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
     * @param model El modelo para pasar datos a la vista.
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
    public String mostrarFormularioSocioeconomico(Model model) {
        model.addAttribute("estudioSocioeconomico", new AdmEstudioSocioeconomico());
        return "portal/socioeconomico";
    }

    @PostMapping("/socioeconomico/guardar")
    public String guardarSocioeconomico(
            @Valid @ModelAttribute("estudioSocioeconomico") AdmEstudioSocioeconomico estudio,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "portal/socioeconomico"; // Volver al formulario si hay errores
        }

        AdmParticipante participante = participanteService.getByUsername(authentication.getName()).orElseThrow();

        if (participante.getEstado() == EstadoParticipante.ADMITIDO_EXAMEN) {
            estudio.setParticipante(participante);
            estudioSocioeconomicoService.save(estudio); // Guardar el formulario

            participante.setEstado(EstadoParticipante.SOCIOECONOMICO_ENVIADO); // Actualizar estado
            participanteService.save(participante);

            redirectAttributes.addFlashAttribute("successMessage", "Formulario enviado correctamente. Está en proceso de revisión.");
        }

        return "redirect:/portal/dashboard";
    }

    private int calculateProgress(EstadoParticipante estado) {
        return switch (estado) {
            case PENDIENTE_EXAMEN -> 0;
            case EXAMEN_REALIZADO -> 5;
            case ADMITIDO_EXAMEN -> 10;
            case SOCIOECONOMICO_ENVIADO -> 20;
            case ADMITIDO_SOCIOECONOMICO, ADMITIDO_FORMULARIO -> 30;
            case PAPELERIA_ENVIADA -> 40;
            case ADMITIDO_PAPELERIA -> 50;
            case CONTRATO_ENVIADO -> 62;
            case ADMITIDO_CONTRATO -> 70;
            case FINALIZADO -> 100;
            default -> 0;
        };
    }
}