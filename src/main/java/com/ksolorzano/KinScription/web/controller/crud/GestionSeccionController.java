package com.ksolorzano.KinScription.web.controller.crud;

import com.ksolorzano.KinScription.dominio.service.SeccionService;
import com.ksolorzano.KinScription.persistence.entity.Seccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gestion/secciones")
public class GestionSeccionController {

    private final SeccionService seccionService;

    @Autowired
    public GestionSeccionController(SeccionService seccionService) {
        this.seccionService = seccionService;
    }

    @GetMapping
    public String mostrarPaginaSecciones(@RequestParam(required = false) String action, Model model) {
        List<Seccion> secciones = seccionService.getAll();
        model.addAttribute("secciones", secciones);
        model.addAttribute("totalSecciones", secciones.size());
        model.addAttribute("seccionesActivas", seccionService.countTotal());

        if (!model.containsAttribute("seccion")) {
            model.addAttribute("seccion", new Seccion());
        }

        if ("new".equals(action)) {
            model.addAttribute("openModal", true);
        }

        return "gestion/gestion-secciones";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Seccion> seccionOpt = seccionService.getById(id);
        if (seccionOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Sección no encontrada.");
            return "redirect:/gestion/secciones";
        }

        model.addAttribute("secciones", seccionService.getAll());
        model.addAttribute("totalSecciones", seccionService.countTotal());
        model.addAttribute("seccionesActivas", seccionService.countTotal());
        model.addAttribute("seccion", seccionOpt.get());
        model.addAttribute("openModal", true);

        return "gestion/gestion-secciones";
    }

    @PostMapping("/guardar")
    public String guardarSeccion(@ModelAttribute("seccion") Seccion seccion, RedirectAttributes redirectAttributes) {
        try {
            String successMessage = (seccion.getIdSeccion() == null) ? "Sección creada correctamente." : "Sección actualizada correctamente.";
            seccionService.save(seccion);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: El nombre de la sección '" + seccion.getNombreSeccion() + "' ya existe.");
            redirectAttributes.addFlashAttribute("seccion", seccion);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al guardar la sección.");
            redirectAttributes.addFlashAttribute("seccion", seccion);
        }
        return "redirect:/gestion/secciones";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarSeccion(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            if (seccionService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Sección eliminada correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró la sección para eliminar.");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acción denegada: La sección está asignada a uno o más alumnos.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al eliminar la sección.");
        }
        return "redirect:/gestion/secciones";
    }
}