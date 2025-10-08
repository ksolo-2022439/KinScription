package com.ksolorzano.KinScription.web.controller.gestion;

import com.ksolorzano.KinScription.dominio.service.AdmCicloAcademicoService;
import com.ksolorzano.KinScription.dominio.service.AdmParticipanteService;
import com.ksolorzano.KinScription.dominio.service.CarreraService;
import com.ksolorzano.KinScription.dominio.service.GradoService;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/gestion/participantes")
public class GestionParticipanteController {

    private final AdmParticipanteService participanteService;
    private final GradoService gradoService;
    private final CarreraService carreraService;
    private final AdmCicloAcademicoService cicloService;

    @Autowired
    public GestionParticipanteController(AdmParticipanteService ps, GradoService gs, CarreraService cs, AdmCicloAcademicoService acs) {
        this.participanteService = ps;
        this.gradoService = gs;
        this.carreraService = cs;
        this.cicloService = acs;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("grados", gradoService.getAll());
        model.addAttribute("carreras", carreraService.getAll());
        model.addAttribute("ciclos", cicloService.getAll());
        model.addAttribute("estados", EstadoParticipante.values());
    }

    @GetMapping
    public String listarParticipantes(Model model) {
        List<AdmParticipante> participantes = participanteService.getAll();
        model.addAttribute("participantes", participantes);

        if (!model.containsAttribute("participante")) {
            model.addAttribute("participante", new AdmParticipante());
        }

        return "admin/gestion/gestion-participantes";
    }

    @PostMapping("/guardar")
    public String guardarParticipante(@Valid @ModelAttribute("participante") AdmParticipante participante,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("participante", participante);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.participante", result);
            redirectAttributes.addFlashAttribute("showModal", true);
            return "redirect:/admin/gestion/participantes";
        }

        try {
            String mensaje = (participante.getId() == null) ? "Participante creado correctamente." : "Participante actualizado correctamente.";
            participanteService.save(participante);
            redirectAttributes.addFlashAttribute("successMessage", mensaje);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: El nombre de usuario '" + participante.getUsername() + "' ya está en uso.");
            redirectAttributes.addFlashAttribute("participante", participante);
            redirectAttributes.addFlashAttribute("showModal", true);
        }

        return "redirect:/admin/gestion/participantes";
    }

    @PostMapping("/desactivar/{id}")
    public String desactivarParticipante(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        if (participanteService.desactivar(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Participante desactivado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró el participante.");
        }
        return "redirect:/admin/gestion/participantes";
    }
}