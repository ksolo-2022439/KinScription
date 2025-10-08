package com.ksolorzano.KinScription.web.controller.crud;

import com.ksolorzano.KinScription.dominio.service.*;
import com.ksolorzano.KinScription.persistence.entity.ProfesorAsignacion;
import com.ksolorzano.KinScription.persistence.entity.ProfesorAsignacionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/gestion/profesorasignacion")
public class GestionProfesorAsignacionController {

    private final ProfesorAsignacionService profesorAsignacionService;
    private final ProfesorService profesorService;
    private final CursoService cursoService;
    private final GradoService gradoService;
    private final SeccionService seccionService;
    private final JornadaService jornadaService;

    @Autowired
    public GestionProfesorAsignacionController(ProfesorAsignacionService profesorAsignacionService, ProfesorService profesorService, CursoService cursoService, GradoService gradoService, SeccionService seccionService, JornadaService jornadaService) {
        this.profesorAsignacionService = profesorAsignacionService;
        this.profesorService = profesorService;
        this.cursoService = cursoService;
        this.gradoService = gradoService;
        this.seccionService = seccionService;
        this.jornadaService = jornadaService;
    }

    @GetMapping
    public String mostrarPaginaAsignaciones(@RequestParam(required = false) Integer profesorFilter,
                                            @RequestParam(required = false) Integer cursoFilter,
                                            @RequestParam(required = false) Integer gradoFilter,
                                            @RequestParam(required = false) Integer seccionFilter,
                                            @RequestParam(required = false) Integer jornadaFilter,
                                            @RequestParam(required = false) String action,
                                            Model model) {

        List<ProfesorAsignacion> asignaciones = profesorAsignacionService.buscarPorFiltros(profesorFilter, cursoFilter, gradoFilter, seccionFilter, jornadaFilter);
        model.addAttribute("asignaciones", asignaciones);
        model.addAttribute("totalAsignaciones", asignaciones.size());
        model.addAttribute("asignacionesActivas", profesorAsignacionService.countTotal());

        cargarListas(model);
        model.addAttribute("profesorFilter", profesorFilter);
        model.addAttribute("cursoFilter", cursoFilter);
        model.addAttribute("gradoFilter", gradoFilter);
        model.addAttribute("seccionFilter", seccionFilter);
        model.addAttribute("jornadaFilter", jornadaFilter);

        if (!model.containsAttribute("asociacion")) {
            model.addAttribute("asociacion", new ProfesorAsignacion());
        }

        if ("new".equals(action)) {
            model.addAttribute("openModal", true);
        }

        return "gestion/gestion-profesorasignacion";
    }

    @PostMapping("/guardar")
    public String guardarAsignacion(@ModelAttribute("asociacion") ProfesorAsignacion asignacion, RedirectAttributes redirectAttributes) {
        try {
            if (asignacion.getIdProfesor() == null || asignacion.getIdCurso() == null || asignacion.getIdGrado() == null || asignacion.getIdSeccion() == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Los campos Profesor, Curso, Grado y Sección son obligatorios.");
                return "redirect:/gestion/profesorasignacion";
            }
            profesorAsignacionService.save(asignacion);
            redirectAttributes.addFlashAttribute("successMessage", "Asignación creada correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Esta asignación exacta ya existe.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al guardar la asignación.");
        }
        return "redirect:/gestion/profesorasignacion";
    }

    @PostMapping("/eliminar")
    public String eliminarAsignacion(@RequestParam("idProfesor") Integer idProfesor,
                                     @RequestParam("idCurso") Integer idCurso,
                                     @RequestParam("idGrado") Integer idGrado,
                                     @RequestParam("idSeccion") Integer idSeccion,
                                     RedirectAttributes redirectAttributes) {
        try {
            ProfesorAsignacionId id = new ProfesorAsignacionId(idProfesor, idCurso, idGrado, idSeccion);
            if (profesorAsignacionService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Asignación eliminada correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró la asignación para eliminar.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al eliminar la asignación.");
        }
        return "redirect:/gestion/profesorasignacion";
    }

    private void cargarListas(Model model) {
        model.addAttribute("listaProfesores", profesorService.getAll());
        model.addAttribute("listaCursos", cursoService.getAll());
        model.addAttribute("listaGrados", gradoService.getAll());
        model.addAttribute("listaSecciones", seccionService.getAll());
        model.addAttribute("listaJornadas", jornadaService.getAll());
    }
}