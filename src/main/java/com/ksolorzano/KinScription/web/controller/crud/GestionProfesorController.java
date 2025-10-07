package com.ksolorzano.KinScription.web.controller.crud;

import com.ksolorzano.KinScription.dominio.service.ProfesorService;
import com.ksolorzano.KinScription.persistence.entity.Profesor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gestion/profesores")
public class GestionProfesorController {

    private final ProfesorService profesorService;

    @Autowired
    public GestionProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    @GetMapping
    public String mostrarPaginaProfesores(@RequestParam(required = false) String nombreFilter,
                                          @RequestParam(required = false) String apellidoFilter,
                                          @RequestParam(required = false) String telefonoFilter,
                                          @RequestParam(required = false) String emailFilter,
                                          @RequestParam(required = false) String action,
                                          Model model) {

        List<Profesor> profesoresFiltrados = profesorService.buscarPorFiltros(nombreFilter, apellidoFilter, telefonoFilter, emailFilter);
        model.addAttribute("profesores", profesoresFiltrados);
        model.addAttribute("totalProfesores", profesoresFiltrados.size());
        model.addAttribute("profesoresActivos", profesorService.countTotal());

        model.addAttribute("nombreFilter", nombreFilter);
        model.addAttribute("apellidoFilter", apellidoFilter);
        model.addAttribute("telefonoFilter", telefonoFilter);
        model.addAttribute("emailFilter", emailFilter);

        if (!model.containsAttribute("profesor")) {
            model.addAttribute("profesor", new Profesor());
        }

        if ("new".equals(action)) {
            model.addAttribute("openModal", true);
        }

        return "gestion/gestion-profesores";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Profesor> profesorOpt = profesorService.getById(id);
        if (profesorOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Profesor no encontrado.");
            return "redirect:/gestion/profesores";
        }

        model.addAttribute("profesores", profesorService.getAll());
        model.addAttribute("totalProfesores", profesorService.countTotal());
        model.addAttribute("profesoresActivos", profesorService.countTotal());
        model.addAttribute("profesor", profesorOpt.get());
        model.addAttribute("openModal", true);

        return "gestion/gestion-profesores";
    }

    @PostMapping("/guardar")
    public String guardarProfesor(@ModelAttribute("profesor") Profesor profesor, RedirectAttributes redirectAttributes) {
        try {
            boolean isNew = profesor.getIdProfesor() == null;
            if (!isNew && (profesor.getContrasena() == null || profesor.getContrasena().trim().isEmpty())) {
                profesorService.getById(profesor.getIdProfesor())
                        .map(Profesor::getContrasena)
                        .ifPresent(profesor::setContrasena);
            }

            profesorService.save(profesor);
            String successMessage = isNew ? "Profesor creado correctamente." : "Profesor actualizado correctamente.";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: El email '" + profesor.getEmail() + "' ya está registrado.");
            redirectAttributes.addFlashAttribute("profesor", profesor);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al guardar el profesor.");
            redirectAttributes.addFlashAttribute("profesor", profesor);
        }
        return "redirect:/gestion/profesores";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarProfesor(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            if (profesorService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Profesor eliminado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró el profesor para eliminar.");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acción denegada: El profesor está asignado a otras entidades.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al eliminar el profesor.");
        }
        return "redirect:/gestion/profesores";
    }
}