package com.ksolorzano.KinScription.web.controller.crud;

import com.ksolorzano.KinScription.dominio.service.CursoService;
import com.ksolorzano.KinScription.persistence.entity.Curso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gestion/cursos")
public class GestionCursoController {

    private final CursoService cursoService;

    @Autowired
    public GestionCursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public String mostrarPaginaCursos(@RequestParam(required = false) String action, Model model) {
        List<Curso> cursos = cursoService.getAll();
        model.addAttribute("cursos", cursos);
        model.addAttribute("totalCursos", cursos.size());
        model.addAttribute("cursosActivos", cursoService.countTotal());

        if (!model.containsAttribute("curso")) {
            model.addAttribute("curso", new Curso());
        }

        if ("new".equals(action)) {
            model.addAttribute("openModal", true);
        }

        return "gestion/gestion-cursos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Curso> cursoOpt = cursoService.getById(id);
        if (cursoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Curso no encontrado.");
            return "redirect:/gestion/cursos";
        }

        model.addAttribute("cursos", cursoService.getAll());
        model.addAttribute("totalCursos", cursoService.countTotal());
        model.addAttribute("cursosActivos", cursoService.countTotal());
        model.addAttribute("curso", cursoOpt.get());
        model.addAttribute("openModal", true);

        return "gestion/gestion-cursos";
    }

    @PostMapping("/guardar")
    public String guardarCurso(@ModelAttribute("curso") Curso curso, RedirectAttributes redirectAttributes) {
        try {
            String successMessage = (curso.getIdCurso() == null) ? "Curso creado correctamente." : "Curso actualizado correctamente.";
            cursoService.save(curso);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: El nombre del curso '" + curso.getNombreCurso() + "' ya existe.");
            redirectAttributes.addFlashAttribute("curso", curso);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al guardar el curso.");
            redirectAttributes.addFlashAttribute("curso", curso);
        }
        return "redirect:/gestion/cursos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCurso(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            if (cursoService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Curso eliminado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró el curso para eliminar.");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acción denegada: El curso no puede ser eliminado porque está en uso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al eliminar el curso.");
        }
        return "redirect:/gestion/cursos";
    }
}