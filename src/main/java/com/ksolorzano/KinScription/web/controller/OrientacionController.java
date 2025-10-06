package com.ksolorzano.KinScription.web.controller;

import com.ksolorzano.KinScription.dominio.service.AdmEstudioSocioeconomicoService;
import com.ksolorzano.KinScription.dominio.service.AdmParticipanteService;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/orientacion")
public class OrientacionController {

    private final AdmParticipanteService participanteService;
    private final AdmEstudioSocioeconomicoService estudioSocioeconomicoService;

    public OrientacionController(AdmParticipanteService participanteService, AdmEstudioSocioeconomicoService estudioSocioeconomicoService) {
        this.participanteService = participanteService;
        this.estudioSocioeconomicoService = estudioSocioeconomicoService;
    }

    @GetMapping("/formulario/revisar")
    public String listarFormularios(Model model) {
        List<AdmParticipante> participantes = participanteService.getByEstado(EstadoParticipante.ADMITIDO_SOCIOECONOMICO);
        model.addAttribute("participantes", participantes);
        return "admin/orientacion/lista_formularios";
    }

    @PostMapping("/formulario/aprobar")
    public String aprobarFormulario(@RequestParam("estudioId") int estudioId) {
        estudioSocioeconomicoService.aprobarPorOrientacion(estudioId);
        // Aquí se podría añadir la llamada para generar el reporte de pagos
        return "redirect:/admin/orientacion/formulario/revisar";
    }

    @PostMapping("/participante/rechazar")
    public String rechazarParticipante(@RequestParam("participanteId") int participanteId) {
        AdmParticipante participante = participanteService.getById(participanteId)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        participante.setEstado(EstadoParticipante.DESAPROBADO);
        participanteService.save(participante);
        return "redirect:/admin/orientacion/formulario/revisar";
    }
}