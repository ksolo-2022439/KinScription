package com.ksolorzano.KinScription.web.controller.crud;

import com.ksolorzano.KinScription.dominio.service.CarreraService;
import com.ksolorzano.KinScription.persistence.entity.Carrera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gestion/carreras")
public class GestionCarreraController {

    private final CarreraService carreraService;

    @Autowired
    public GestionCarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping
    public String mostrarPaginaCarreras(@RequestParam(required = false) String action, Model model) {
        List<Carrera> carreras = carreraService.getAll();
        model.addAttribute("carreras", carreras);
        model.addAttribute("totalCarreras", carreras.size());
        model.addAttribute("carrerasActivas", carreraService.countTotal());

        if (!model.containsAttribute("carrera")) {
            model.addAttribute("carrera", new Carrera());
        }

        if ("new".equals(action)) {
            model.addAttribute("openModal", true);
        }

        return "gestion/gestion-carreras";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Carrera> carreraOpt = carreraService.getById(id);
        if (carreraOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Carrera no encontrada.");
            return "redirect:/gestion/carreras";
        }

        model.addAttribute("carreras", carreraService.getAll());
        model.addAttribute("totalCarreras", carreraService.countTotal());
        model.addAttribute("carrerasActivas", carreraService.countTotal());
        model.addAttribute("carrera", carreraOpt.get());
        model.addAttribute("openModal", true);

        return "gestion/gestion-carreras";
    }

    @PostMapping("/guardar")
    public String guardarCarrera(@ModelAttribute("carrera") Carrera carrera, RedirectAttributes redirectAttributes) {
        try {
            String successMessage = (carrera.getIdCarrera() == null) ? "Carrera creada correctamente." : "Carrera actualizada correctamente.";
            carreraService.save(carrera);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: El nombre de la carrera '" + carrera.getNombreCarrera() + "' ya existe.");
            redirectAttributes.addFlashAttribute("carrera", carrera);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al guardar la carrera.");
            redirectAttributes.addFlashAttribute("carrera", carrera);
        }
        return "redirect:/gestion/carreras";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCarrera(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            if (carreraService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Carrera eliminada correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró la carrera para eliminar.");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acción denegada: La carrera está asignada a uno o más alumnos.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al eliminar la carrera.");
        }
        return "redirect:/gestion/carreras";
    }
}