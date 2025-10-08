package com.ksolorzano.KinScription.web.controller.crud;

import com.ksolorzano.KinScription.dominio.service.AlumnoService;
import com.ksolorzano.KinScription.dominio.service.CursoService;
import com.ksolorzano.KinScription.dominio.service.NotasService;
import com.ksolorzano.KinScription.persistence.entity.Notas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gestion/notas")
public class GestionNotasController {

    private final NotasService notasService;
    private final AlumnoService alumnoService;
    private final CursoService cursoService;

    @Autowired
    public GestionNotasController(NotasService notasService, AlumnoService alumnoService, CursoService cursoService) {
        this.notasService = notasService;
        this.alumnoService = alumnoService;
        this.cursoService = cursoService;
    }

    @GetMapping
    public String mostrarPaginaNotas(@RequestParam(required = false) Integer alumnoFilter,
                                     @RequestParam(required = false) Integer cursoFilter,
                                     @RequestParam(required = false) BigDecimal promedioFilter,
                                     @RequestParam(required = false) String action,
                                     Model model) {

        List<Notas> notasFiltradas = notasService.buscarPorFiltros(alumnoFilter, cursoFilter, promedioFilter);
        model.addAttribute("notas", notasFiltradas);
        model.addAttribute("totalNotas", notasFiltradas.size());
        model.addAttribute("notasActivas", notasService.countTotal());

        model.addAttribute("alumnoFilter", alumnoFilter);
        model.addAttribute("cursoFilter", cursoFilter);
        model.addAttribute("promedioFilter", promedioFilter);

        cargarListas(model);

        if (!model.containsAttribute("nota")) {
            model.addAttribute("nota", new Notas());
        }

        if ("new".equals(action)) {
            model.addAttribute("openModal", true);
        }

        return "gestion/gestion-notas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Notas> notaOpt = notasService.getById(id);
        if (notaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Nota no encontrada.");
            return "redirect:/gestion/notas";
        }

        model.addAttribute("notas", notasService.getAll());
        model.addAttribute("totalNotas", notasService.countTotal());
        model.addAttribute("notasActivas", notasService.countTotal());
        cargarListas(model);
        model.addAttribute("nota", notaOpt.get());
        model.addAttribute("openModal", true);

        return "gestion/gestion-notas";
    }

    @PostMapping("/guardar")
    public String guardarNota(@ModelAttribute("nota") Notas nota, RedirectAttributes redirectAttributes) {
        try {
            String successMessage = (nota.getIdNota() == null) ? "Nota creada correctamente." : "Nota actualizada correctamente.";
            notasService.save(nota);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Ya existe un registro de notas para este alumno en este curso.");
            redirectAttributes.addFlashAttribute("nota", nota);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al guardar la nota.");
            redirectAttributes.addFlashAttribute("nota", nota);
        }
        return "redirect:/gestion/notas";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarNota(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            if (notasService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Nota eliminada correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró la nota para eliminar.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al eliminar la nota.");
        }
        return "redirect:/gestion/notas";
    }

    private void cargarListas(Model model) {
        model.addAttribute("listaAlumnos", alumnoService.getAll());
        model.addAttribute("listaCursos", cursoService.getAll());
    }
}