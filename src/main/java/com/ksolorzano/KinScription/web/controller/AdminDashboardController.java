package com.ksolorzano.KinScription.web.controller;

import com.ksolorzano.KinScription.dominio.service.AdmParticipanteService;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final AdmParticipanteService participanteService;

    @Autowired
    public AdminDashboardController(AdmParticipanteService participanteService) {
        this.participanteService = participanteService;
    }

    /**
     * Muestra el dashboard principal para todos los roles administrativos.
     * Proporciona un resumen del estado del proceso de admisión.
     */
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        Integer pendientesExamen = participanteService.getByEstado(EstadoParticipante.PENDIENTE_EXAMEN).size();
        Integer pendientesSocioeconomico = participanteService.getByEstado(EstadoParticipante.ADMITIDO_EXAMEN).size();
        Integer pendientesPapeleria = participanteService.getByEstado(EstadoParticipante.ADMITIDO_FORMULARIO).size();
        Integer finalizados = participanteService.getByEstado(EstadoParticipante.FINALIZADO).size();

        model.addAttribute("countPendientesExamen", pendientesExamen);
        model.addAttribute("countPendientesSocioeconomico", pendientesSocioeconomico);
        model.addAttribute("countPendientesPapeleria", pendientesPapeleria);
        model.addAttribute("countFinalizados", finalizados);

        // La vista a renderizar
        return "admin/dashboard";
    }
}