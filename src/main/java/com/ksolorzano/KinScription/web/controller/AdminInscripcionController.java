package com.ksolorzano.KinScription.web.controller;

import com.ksolorzano.KinScription.dominio.service.AdmParticipanteService;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/inscripcion")
public class AdminInscripcionController {

    private final AdmParticipanteService participanteService;

    @Autowired
    public AdminInscripcionController(AdmParticipanteService participanteService) {
        this.participanteService = participanteService;
    }

    /**
     * Muestra una lista de todos los participantes que están pendientes de calificar su examen.
     */
    @GetMapping("/examenes")
    public String listarExamenesPendientes(Model model) {
        List<AdmParticipante> participantes = participanteService.getByEstado(EstadoParticipante.EXAMEN_REALIZADO);
        model.addAttribute("participantes", participantes);
        return "admin/inscripcion/lista_examenes";
    }

    /**
     * Procesa la calificación de un examen.
     * Se llama desde un formulario en la vista "lista_examenes.html".
     */
    @PostMapping("/calificar")
    public String calificarExamen(@RequestParam("participanteId") int participanteId,
                                  @RequestParam("nota") BigDecimal nota) {
        try {
            participanteService.calificarExamen(participanteId, nota);
        } catch (Exception e) {
            // Manejar el error, quizás redirigir con un mensaje de error
            return "redirect:/admin/inscripcion/examenes?error=true";
        }
        return "redirect:/admin/inscripcion/examenes";
    }
}