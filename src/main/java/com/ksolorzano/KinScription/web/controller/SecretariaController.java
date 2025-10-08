package com.ksolorzano.KinScription.web.controller;

import com.ksolorzano.KinScription.dominio.repository.AdmDocumentoRequeridoRepository;
import com.ksolorzano.KinScription.dominio.service.AdmContratoService;
import com.ksolorzano.KinScription.dominio.service.AdmDocumentoRequeridoService;
import com.ksolorzano.KinScription.dominio.service.AdmParticipanteService;
import com.ksolorzano.KinScription.persistence.entity.AdmDocumentoRequerido;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/secretaria")
public class SecretariaController {

    private final AdmParticipanteService participanteService;
    private final AdmDocumentoRequeridoService documentoService;
    private final AdmContratoService contratoService;
    private final AdmDocumentoRequeridoRepository documentoRepository;

    public SecretariaController(AdmParticipanteService ps, AdmDocumentoRequeridoService ds, AdmContratoService cs, AdmDocumentoRequeridoRepository dr) {
        this.participanteService = ps;
        this.documentoService = ds;
        this.contratoService = cs;
        this.documentoRepository = dr;
    }

    @GetMapping("/papeleria")
    public String listarPendientesPapeleria(Model model) {
        List<AdmParticipante> participantes = participanteService.getByEstado(EstadoParticipante.PAPELERIA_ENVIADA);
        model.addAttribute("participantes", participantes);
        return "admin/secretaria/lista_papeleria";
    }

    @GetMapping("/papeleria/revisar/{id}")
    public String revisarPapeleria(@PathVariable("id") int id, Model model) {
        AdmParticipante participante = participanteService.getById(id)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        model.addAttribute("documentosRequeridos", AdmDocumentoRequeridoService.DOCUMENTOS_REQUERIDOS);

        Map<String, AdmDocumentoRequerido> mapaDocumentos = documentoRepository.findByParticipante(participante).stream()
                .collect(Collectors.toMap(AdmDocumentoRequerido::getNombreDocumento, doc -> doc));

        model.addAttribute("participante", participante);
        model.addAttribute("mapaDocumentos", mapaDocumentos);

        return "admin/secretaria/revisar_papeleria";
    }

    @PostMapping("/papeleria/aprobar")
    public String aprobarPapeleria(@RequestParam("participanteId") int participanteId, RedirectAttributes redirectAttributes) {
        try {
            documentoService.aprobarPapeleria(participanteId);
            redirectAttributes.addFlashAttribute("successMessage", "Papelería aprobada correctamente. El participante puede continuar al siguiente paso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al aprobar la papelería: " + e.getMessage());
        }
        return "redirect:/admin/secretaria/papeleria";
    }

    @PostMapping("/papeleria/solicitar-correccion")
    public String solicitarCorreccion(@RequestParam("participanteId") int participanteId,
                                      @RequestParam(value = "documentosASolicitar", required = false) List<String> documentosASolicitar,
                                      RedirectAttributes redirectAttributes) {
        if (documentosASolicitar == null || documentosASolicitar.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Debe seleccionar al menos un documento para solicitar corrección.");
            return "redirect:/admin/secretaria/papeleria/revisar/" + participanteId;
        }

        try {
            documentoService.solicitarCorrecciones(participanteId, documentosASolicitar);
            redirectAttributes.addFlashAttribute("successMessage", "Se ha solicitado la corrección al participante.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/secretaria/papeleria";
    }

    /**
     * Rechaza a un participante y lo devuelve a la lista de origen.
     * @param participanteId El ID del participante a rechazar.
     * @param origen Un parámetro opcional que indica desde qué página se hizo el rechazo (ej. "papeleria", "contratos").
     * @param redirectAttributes Para enviar mensajes de feedback.
     * @return Una redirección a la lista correspondiente.
     */
    @PostMapping("/participante/rechazar")
    public String rechazarParticipante(@RequestParam("participanteId") int participanteId,
                                       @RequestParam(value = "origen", required = false) String origen,
                                       RedirectAttributes redirectAttributes) {

        AdmParticipante participante = participanteService.getById(participanteId)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        participante.setEstado(EstadoParticipante.DESAPROBADO);
        participanteService.save(participante);

        redirectAttributes.addFlashAttribute("successMessage", "El participante ha sido rechazado correctamente.");

        if ("contratos".equals(origen)) {
            return "redirect:/admin/secretaria/contratos";
        }

        return "redirect:/admin/secretaria/papeleria";
    }

    @GetMapping("/contratos")
    public String listarContratos(Model model) {
        List<AdmParticipante> participantes = participanteService.getByEstado(EstadoParticipante.CONTRATO_ENVIADO);
        model.addAttribute("participantes", participantes);
        return "admin/secretaria/lista_contratos";
    }

    /**
     * Muestra la página de detalle para revisar el contrato de un participante.
     * @param id El ID del PARTICIPANTE.
     */
    @GetMapping("/contratos/revisar/{id}")
    public String revisarContrato(@PathVariable("id") int id, Model model) {
        AdmParticipante participante = participanteService.getById(id)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        contratoService.findByParticipante(participante)
                .ifPresent(contrato -> model.addAttribute("contrato", contrato));

        model.addAttribute("participante", participante);
        return "admin/secretaria/revisar_contrato";
    }

    @PostMapping("/contratos/aprobar")
    public String aprobarContrato(@RequestParam("contratoId") int contratoId, RedirectAttributes redirectAttributes) {
        try {
            contratoService.aprobarContrato(contratoId);
            redirectAttributes.addFlashAttribute("successMessage", "¡Contrato aprobado! El participante ha sido inscrito y convertido en alumno exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al aprobar el contrato: " + e.getMessage());
        }
        return "redirect:/admin/secretaria/contratos";
    }
}