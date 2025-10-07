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
@RequestMapping("/admin/director")
public class DirectorAdminController {

    private final AdmParticipanteService participanteService;
    private final AdmEstudioSocioeconomicoService estudioSocioeconomicoService;

    public DirectorAdminController(AdmParticipanteService participanteService, AdmEstudioSocioeconomicoService estudioSocioeconomicoService) {
        this.participanteService = participanteService;
        this.estudioSocioeconomicoService = estudioSocioeconomicoService;
    }

    /**
     * Muestra la lista de participantes cuyo estudio socioeconómico está listo para ser revisado.
     */
    @GetMapping("/socioeconomico/revisar")
    public String listarSocioeconomicos(Model model) {
        List<AdmParticipante> participantes = participanteService.getByEstado(EstadoParticipante.SOCIOECONOMICO_ENVIADO);
        model.addAttribute("participantes", participantes);
        return "admin/director/lista_socioeconomico";
    }

    @PostMapping("/socioeconomico/aprobar")
    public String aprobarSocioeconomico(@RequestParam("estudioId") int estudioId) {
        estudioSocioeconomicoService.aprobarPorDirector(estudioId);
        return "redirect:/admin/director/socioeconomico/revisar";
    }

    @PostMapping("/participante/rechazar")
    public String rechazarParticipante(@RequestParam("participanteId") int participanteId) {
        // Lógica para cambiar el estado a DESAPROBADO
        AdmParticipante participante = participanteService.getById(participanteId).orElseThrow();
        participante.setEstado(EstadoParticipante.DESAPROBADO);
        participanteService.save(participante);
        return "redirect:/admin/director/socioeconomico/revisar";
    }
}