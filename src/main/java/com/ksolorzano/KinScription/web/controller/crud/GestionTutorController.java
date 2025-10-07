package com.ksolorzano.KinScription.web.controller.crud;

import com.ksolorzano.KinScription.dominio.service.TutorService;
import com.ksolorzano.KinScription.persistence.entity.Tutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gestion/tutores")
public class GestionTutorController {

    private final TutorService tutorService;

    @Autowired
    public GestionTutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @GetMapping
    public String mostrarPaginaTutores(@RequestParam(required = false) String nombreFilter,
                                       @RequestParam(required = false) String telefonoFilter,
                                       @RequestParam(required = false) String action,
                                       Model model) {

        List<Tutor> tutoresFiltrados = tutorService.buscarPorFiltros(nombreFilter, telefonoFilter);
        model.addAttribute("tutores", tutoresFiltrados);
        model.addAttribute("totalTutores", tutoresFiltrados.size());
        model.addAttribute("tutoresActivos", tutorService.countTotal());

        model.addAttribute("nombreFilter", nombreFilter);
        model.addAttribute("telefonoFilter", telefonoFilter);

        if (!model.containsAttribute("tutor")) {
            model.addAttribute("tutor", new Tutor());
        }

        if ("new".equals(action)) {
            model.addAttribute("openModal", true);
        }

        return "gestion/gestion-tutores";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Tutor> tutorOpt = tutorService.getById(id);
        if (tutorOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Tutor no encontrado.");
            return "redirect:/gestion/tutores";
        }

        List<Tutor> todosLosTutores = tutorService.getAll();
        model.addAttribute("tutores", todosLosTutores);
        model.addAttribute("totalTutores", todosLosTutores.size());
        model.addAttribute("tutoresActivos", tutorService.countTotal());

        model.addAttribute("tutor", tutorOpt.get());
        model.addAttribute("openModal", true);

        return "gestion/gestion-tutores";
    }

    @PostMapping("/guardar")
    public String guardarTutor(@ModelAttribute("tutor") Tutor tutor, RedirectAttributes redirectAttributes) {
        try {
            String successMessage = (tutor.getIdTutor() == null) ? "Tutor creado correctamente." : "Tutor actualizado correctamente.";
            tutorService.save(tutor);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al guardar el tutor.");
            redirectAttributes.addFlashAttribute("tutor", tutor);
        }
        return "redirect:/gestion/tutores";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarTutor(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            if (tutorService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Tutor eliminado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró el tutor para eliminar.");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acción denegada: El tutor está asignado a uno o más alumnos.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al eliminar al tutor.");
        }
        return "redirect:/gestion/tutores";
    }
}