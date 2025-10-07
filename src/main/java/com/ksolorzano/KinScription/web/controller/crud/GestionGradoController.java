package com.ksolorzano.KinScription.web.controller.crud;

import com.ksolorzano.KinScription.dominio.service.GradoService;
import com.ksolorzano.KinScription.persistence.entity.Grado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gestion/grados")
public class GestionGradoController {

    private final GradoService gradoService;

    @Autowired
    public GestionGradoController(GradoService gradoService) {
        this.gradoService = gradoService;
    }

    @GetMapping
    public String mostrarPaginaGrados(@RequestParam(required = false) String action, Model model) {
        List<Grado> grados = gradoService.getAll();
        model.addAttribute("grados", grados);
        model.addAttribute("totalGrados", grados.size());
        model.addAttribute("gradosActivos", gradoService.countTotal());

        if (!model.containsAttribute("grado")) {
            model.addAttribute("grado", new Grado());
        }

        if ("new".equals(action)) {
            model.addAttribute("openModal", true);
        }

        return "gestion/gestion-grados";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Grado> gradoOpt = gradoService.getById(id);
        if (gradoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Grado no encontrado.");
            return "redirect:/gestion/grados";
        }

        model.addAttribute("grados", gradoService.getAll());
        model.addAttribute("totalGrados", gradoService.countTotal());
        model.addAttribute("gradosActivos", gradoService.countTotal());
        model.addAttribute("grado", gradoOpt.get());
        model.addAttribute("openModal", true);

        return "gestion/gestion-grados";
    }

    @PostMapping("/guardar")
    public String guardarGrado(@ModelAttribute("grado") Grado grado, RedirectAttributes redirectAttributes) {
        try {
            String successMessage = (grado.getIdGrado() == null) ? "Grado creado correctamente." : "Grado actualizado correctamente.";
            gradoService.save(grado);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: El nombre del grado '" + grado.getNombreGrado() + "' ya existe.");
            redirectAttributes.addFlashAttribute("grado", grado);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al guardar el grado.");
            redirectAttributes.addFlashAttribute("grado", grado);
        }
        return "redirect:/gestion/grados";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarGrado(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            if (gradoService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Grado eliminado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró el grado para eliminar.");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acción denegada: El grado está asignado a uno o más alumnos.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al eliminar el grado.");
        }
        return "redirect:/gestion/grados";
    }
}