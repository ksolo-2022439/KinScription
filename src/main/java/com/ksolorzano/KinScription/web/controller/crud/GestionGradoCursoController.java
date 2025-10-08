package com.ksolorzano.KinScription.web.controller.crud;

import com.ksolorzano.KinScription.dominio.service.CursoService;
import com.ksolorzano.KinScription.dominio.service.GradoCursoService;
import com.ksolorzano.KinScription.dominio.service.GradoService;
import com.ksolorzano.KinScription.persistence.entity.GradoCurso;
import com.ksolorzano.KinScription.persistence.entity.GradoCursoId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/gestion/gradocurso")
public class GestionGradoCursoController {

    private final GradoCursoService gradoCursoService;
    private final GradoService gradoService;
    private final CursoService cursoService;

    @Autowired
    public GestionGradoCursoController(GradoCursoService gradoCursoService, GradoService gradoService, CursoService cursoService) {
        this.gradoCursoService = gradoCursoService;
        this.gradoService = gradoService;
        this.cursoService = cursoService;
    }

    @GetMapping
    public String mostrarPaginaGradoCurso(@RequestParam(required = false) Integer gradoFilter,
                                          @RequestParam(required = false) String action,
                                          Model model) {

        List<GradoCurso> asociaciones = gradoCursoService.buscarPorFiltro(gradoFilter);
        model.addAttribute("asociaciones", asociaciones);
        model.addAttribute("totalAsociaciones", asociaciones.size());
        model.addAttribute("asociacionesActivas", gradoCursoService.countTotal());

        model.addAttribute("listaGrados", gradoService.getAll());
        model.addAttribute("listaCursos", cursoService.getAll());
        model.addAttribute("gradoFilter", gradoFilter);

        if (!model.containsAttribute("asociacion")) {
            model.addAttribute("asociacion", new GradoCurso());
        }

        if ("new".equals(action)) {
            model.addAttribute("openModal", true);
        }

        return "gestion/gestion-gradocurso";
    }

    @PostMapping("/guardar")
    public String guardarAsociacion(@ModelAttribute("asociacion") GradoCurso asociacion, RedirectAttributes redirectAttributes) {
        try {
            if (asociacion.getIdGrado() == null || asociacion.getIdCurso() == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Debe seleccionar un grado y un curso.");
                return "redirect:/gestion/gradocurso";
            }
            gradoCursoService.save(asociacion);
            redirectAttributes.addFlashAttribute("successMessage", "Asociación creada correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Este curso ya está asociado a este grado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al guardar la asociación.");
        }
        return "redirect:/gestion/gradocurso";
    }

    @PostMapping("/eliminar")
    public String eliminarAsociacion(@RequestParam("idGrado") Integer idGrado,
                                     @RequestParam("idCurso") Integer idCurso,
                                     RedirectAttributes redirectAttributes) {
        try {
            GradoCursoId id = new GradoCursoId(idGrado, idCurso);
            if (gradoCursoService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Asociación eliminada correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró la asociación para eliminar.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al eliminar la asociación.");
        }
        return "redirect:/gestion/gradocurso";
    }
}