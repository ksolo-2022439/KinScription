package com.ksolorzano.KinScription.web.controller;

import com.ksolorzano.KinScription.dominio.service.AdmEstudioSocioeconomicoService;
import com.ksolorzano.KinScription.dominio.service.AdmParticipanteService;
import com.ksolorzano.KinScription.persistence.entity.AdmEstudioSocioeconomico;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orientacion")
public class OrientacionController {

    private final AdmParticipanteService participanteService;
    private final AdmEstudioSocioeconomicoService estudioSocioeconomicoService;

    public OrientacionController(AdmParticipanteService ps, AdmEstudioSocioeconomicoService es) {
        this.participanteService = ps;
        this.estudioSocioeconomicoService = es;
    }

    @GetMapping("/formulario/revisar")
    public String listarFormularios(Model model) {
        List<AdmParticipante> participantes = participanteService.getByEstado(EstadoParticipante.ADMITIDO_SOCIOECONOMICO);
        model.addAttribute("participantes", participantes);
        return "admin/orientacion/lista_formularios";
    }

    /**
     * Muestra la página de detalle para la revisión final de Orientación.
     * @param id El ID del PARTICIPANTE.
     */
    @GetMapping("/formulario/revisar/{id}")
    public String revisarFormulario(@PathVariable("id") int id, Model model) {
        AdmParticipante participante = participanteService.getById(id)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        estudioSocioeconomicoService.getByParticipante(participante)
                .ifPresent(estudio -> model.addAttribute("estudio", estudio));

        model.addAttribute("participante", participante);
        return "admin/orientacion/revisar_formulario";
    }

    @PostMapping("/formulario/aprobar")
    public String aprobarFormulario(@RequestParam("estudioId") int estudioId, RedirectAttributes redirectAttributes) {
        try {
            estudioSocioeconomicoService.aprobarPorOrientacion(estudioId);
            redirectAttributes.addFlashAttribute("successMessage", "Formulario aprobado. El participante puede continuar con la papelería.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al aprobar el formulario: " + e.getMessage());
        }
        return "redirect:/admin/orientacion/formulario/revisar";
    }

    @PostMapping("/participante/rechazar")
    public String rechazarParticipante(@RequestParam("participanteId") int participanteId, RedirectAttributes redirectAttributes) {
        AdmParticipante participante = participanteService.getById(participanteId).orElseThrow();
        participante.setEstado(EstadoParticipante.DESAPROBADO);
        participanteService.save(participante);
        redirectAttributes.addFlashAttribute("successMessage", "El participante ha sido rechazado.");
        return "redirect:/admin/orientacion/formulario/revisar";
    }
}