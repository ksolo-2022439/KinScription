package com.ksolorzano.KinScription.web.controller.crud;

import com.ksolorzano.KinScription.dominio.service.JornadaService;
import com.ksolorzano.KinScription.persistence.entity.Jornada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gestion/jornadas")
public class GestionJornadaController {

    private final JornadaService jornadaService;

    @Autowired
    public GestionJornadaController(JornadaService jornadaService) {
        this.jornadaService = jornadaService;
    }

    @GetMapping
    public String mostrarPaginaJornadas(@RequestParam(required = false) String action, Model model) {
        List<Jornada> jornadas = jornadaService.getAll();
        model.addAttribute("jornadas", jornadas);
        model.addAttribute("totalJornadas", jornadas.size());
        model.addAttribute("jornadasActivas", jornadaService.countTotal());

        if (!model.containsAttribute("jornada")) {
            model.addAttribute("jornada", new Jornada());
        }

        if ("new".equals(action)) {
            model.addAttribute("openModal", true);
        }

        return "gestion/gestion-jornadas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Jornada> jornadaOpt = jornadaService.getById(id);
        if (jornadaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Jornada no encontrada.");
            return "redirect:/gestion/jornadas";
        }

        model.addAttribute("jornadas", jornadaService.getAll());
        model.addAttribute("totalJornadas", jornadaService.countTotal());
        model.addAttribute("jornadasActivas", jornadaService.countTotal());
        model.addAttribute("jornada", jornadaOpt.get());
        model.addAttribute("openModal", true);

        return "gestion/gestion-jornadas";
    }

    @PostMapping("/guardar")
    public String guardarJornada(@ModelAttribute("jornada") Jornada jornada, RedirectAttributes redirectAttributes) {
        try {
            String successMessage = (jornada.getIdJornada() == null) ? "Jornada creada correctamente." : "Jornada actualizada correctamente.";
            jornadaService.save(jornada);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: El nombre de la jornada '" + jornada.getNombreJornada() + "' ya existe.");
            redirectAttributes.addFlashAttribute("jornada", jornada);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al guardar la jornada.");
            redirectAttributes.addFlashAttribute("jornada", jornada);
        }
        return "redirect:/gestion/jornadas";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarJornada(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            if (jornadaService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Jornada eliminada correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró la jornada para eliminar.");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acción denegada: La jornada está asignada a uno o más alumnos.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al eliminar la jornada.");
        }
        return "redirect:/gestion/jornadas";
    }
}