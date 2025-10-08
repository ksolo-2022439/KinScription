package com.ksolorzano.KinScription.web.controller.gestion;

import com.ksolorzano.KinScription.dominio.service.AdmCicloAcademicoService;
import com.ksolorzano.KinScription.persistence.entity.AdmCicloAcademico;
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
@RequestMapping("/admin/gestion/ciclos")
public class GestionCicloController {

    private final AdmCicloAcademicoService cicloService;

    @Autowired
    public GestionCicloController(AdmCicloAcademicoService cicloService) {
        this.cicloService = cicloService;
    }

    @GetMapping
    public String listarCiclos(Model model) {
        List<AdmCicloAcademico> ciclos = cicloService.getAll();
        model.addAttribute("ciclos", ciclos);

        if (!model.containsAttribute("ciclo")) {
            model.addAttribute("ciclo", new AdmCicloAcademico());
        }

        return "admin/gestion/gestion-ciclos";
    }

    @PostMapping("/guardar")
    public String guardarCiclo(@Valid @ModelAttribute("ciclo") AdmCicloAcademico ciclo,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("ciclo", ciclo);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.ciclo", result);
            redirectAttributes.addFlashAttribute("showModal", true);
            return "redirect:/admin/gestion/ciclos";
        }

        String mensaje = (ciclo.getId() == null) ? "Ciclo académico creado correctamente." : "Ciclo académico actualizado correctamente.";
        cicloService.save(ciclo);
        redirectAttributes.addFlashAttribute("successMessage", mensaje);

        return "redirect:/admin/gestion/ciclos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCiclo(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            if (cicloService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Ciclo eliminado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró el ciclo para eliminar.");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acción denegada: El ciclo está asignado a uno o más participantes.");
        }
        return "redirect:/admin/gestion/ciclos";
    }
}