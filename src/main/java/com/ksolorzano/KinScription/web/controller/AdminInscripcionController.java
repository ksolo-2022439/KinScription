package com.ksolorzano.KinScription.web.controller;

import com.ksolorzano.KinScription.dominio.service.AdmParticipanteService;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
     * Muestra la página de detalle para revisar y calificar el examen de un participante.
     * @param id El ID del participante a revisar.
     * @param model El modelo para pasar el objeto participante a la vista.
     * @return El nombre de la vista "admin/inscripcion/revisar_examen".
     */
    @GetMapping("/revisar/{id}")
    public String revisarExamen(@PathVariable("id") int id, Model model) {
        AdmParticipante participante = participanteService.getById(id)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));
        model.addAttribute("participante", participante);
        return "admin/inscripcion/revisar_examen";
    }

    @PostMapping("/calificar")
    public String calificarExamen(@RequestParam("participanteId") int participanteId,
                                  @RequestParam("nota") BigDecimal nota,
                                  RedirectAttributes redirectAttributes) {
        try {
            participanteService.calificarExamen(participanteId, nota);
            redirectAttributes.addFlashAttribute("successMessage", "Examen calificado correctamente. El estado del participante ha sido actualizado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al calificar el examen: " + e.getMessage());
        }
        return "redirect:/admin/inscripcion/examenes";
    }
}