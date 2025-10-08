package com.ksolorzano.KinScription.web.controller.gestion;

import com.ksolorzano.KinScription.dominio.service.AdmEstudioSocioeconomicoService;
import com.ksolorzano.KinScription.persistence.entity.AdmEstudioSocioeconomico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/gestion/estudios")
public class GestionEstudioController {

    private final AdmEstudioSocioeconomicoService estudioService;

    @Autowired
    public GestionEstudioController(AdmEstudioSocioeconomicoService estudioService) {
        this.estudioService = estudioService;
    }

    @GetMapping
    public String listarEstudios(Model model) {
        List<AdmEstudioSocioeconomico> estudios = estudioService.getAll();
        model.addAttribute("estudios", estudios);

        if (!model.containsAttribute("estudio")) {
            model.addAttribute("estudio", new AdmEstudioSocioeconomico());
        }

        return "admin/gestion/gestion-estudios";
    }

    @PostMapping("/guardar")
    public String guardarEstudio(@ModelAttribute("estudio") AdmEstudioSocioeconomico estudio,
                                 RedirectAttributes redirectAttributes) {

        String mensaje = (estudio.getId() == null) ? "Estudio creado correctamente." : "Estudio actualizado correctamente.";
        estudioService.save(estudio);
        redirectAttributes.addFlashAttribute("successMessage", mensaje);

        return "redirect:/admin/gestion/estudios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarEstudio(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            if (estudioService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Estudio eliminado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró el estudio para eliminar.");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: No se puede eliminar el estudio, puede estar referenciado.");
        }
        return "redirect:/admin/gestion/estudios";
    }
}