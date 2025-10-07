package com.ksolorzano.KinScription.web.controller;

import com.ksolorzano.KinScription.dominio.service.AdmEstudioSocioeconomicoService;
import com.ksolorzano.KinScription.dominio.service.AdmParticipanteService;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/director")
public class DirectorAdminController {

    private final AdmParticipanteService participanteService;
    private final AdmEstudioSocioeconomicoService estudioSocioeconomicoService;

    public DirectorAdminController(AdmParticipanteService ps, AdmEstudioSocioeconomicoService es) {
        this.participanteService = ps;
        this.estudioSocioeconomicoService = es;
    }

    @GetMapping("/socioeconomico/revisar")
    public String listarSocioeconomicos(Model model) {
        List<AdmParticipante> participantes = participanteService.getByEstado(EstadoParticipante.SOCIOECONOMICO_ENVIADO);
        model.addAttribute("participantes", participantes);
        return "admin/director/lista_socioeconomico";
    }

    /**
     * Muestra la página de detalle para revisar un estudio socioeconómico específico.
     * @param id El ID del PARTICIPANTE.
     */
    @GetMapping("/socioeconomico/revisar/{id}")
    public String revisarSocioeconomico(@PathVariable("id") int id, Model model) {
        AdmParticipante participante = participanteService.getById(id)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        // Buscamos el estudio asociado a este participante
        estudioSocioeconomicoService.getByParticipante(participante)
                .ifPresent(estudio -> model.addAttribute("estudio", estudio));

        model.addAttribute("participante", participante);
        return "admin/director/revisar_socioeconomico";
    }

    @PostMapping("/socioeconomico/aprobar")
    public String aprobarSocioeconomico(@RequestParam("estudioId") int estudioId,
                                        @RequestParam("montoInscripcion") BigDecimal montoInscripcion,
                                        @RequestParam("montoMensualidad") BigDecimal montoMensualidad,
                                        RedirectAttributes redirectAttributes) {
        try {
            estudioSocioeconomicoService.aprobarPorDirector(estudioId, montoInscripcion, montoMensualidad);
            redirectAttributes.addFlashAttribute("successMessage", "Estudio aprobado y cuotas asignadas correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al aprobar el estudio: " + e.getMessage());
        }
        return "redirect:/admin/director/socioeconomico/revisar";
    }

    @PostMapping("/participante/rechazar")
    public String rechazarParticipante(@RequestParam("participanteId") int participanteId, RedirectAttributes redirectAttributes) {
        AdmParticipante participante = participanteService.getById(participanteId).orElseThrow();
        participante.setEstado(EstadoParticipante.DESAPROBADO);
        participanteService.save(participante);
        redirectAttributes.addFlashAttribute("successMessage", "El participante ha sido rechazado.");
        return "redirect:/admin/director/socioeconomico/revisar";
    }
}