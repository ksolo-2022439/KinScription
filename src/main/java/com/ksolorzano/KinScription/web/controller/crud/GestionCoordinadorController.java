package com.ksolorzano.KinScription.web.controller.crud;

import com.ksolorzano.KinScription.dominio.service.CoordinadorService;
import com.ksolorzano.KinScription.dominio.service.GradoService;
import com.ksolorzano.KinScription.persistence.entity.Coordinador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gestion/coordinadores")
public class GestionCoordinadorController {

    private final CoordinadorService coordinadorService;
    private final GradoService gradoService;

    @Autowired
    public GestionCoordinadorController(CoordinadorService coordinadorService, GradoService gradoService) {
        this.coordinadorService = coordinadorService;
        this.gradoService = gradoService;
    }

    @GetMapping
    public String mostrarPaginaCoordinadores(@RequestParam(required = false) String nombreFilter,
                                             @RequestParam(required = false) String apellidoFilter,
                                             @RequestParam(required = false) String emailFilter,
                                             @RequestParam(required = false) Integer gradoFilter,
                                             @RequestParam(required = false) String action,
                                             Model model) {

        List<Coordinador> coordinadoresFiltrados = coordinadorService.buscarPorFiltros(nombreFilter, apellidoFilter, emailFilter, gradoFilter);
        model.addAttribute("coordinadores", coordinadoresFiltrados);
        model.addAttribute("totalCoordinadores", coordinadoresFiltrados.size());
        model.addAttribute("coordinadoresActivos", coordinadorService.countTotal());
        model.addAttribute("listaGrados", gradoService.getAll());

        model.addAttribute("nombreFilter", nombreFilter);
        model.addAttribute("apellidoFilter", apellidoFilter);
        model.addAttribute("emailFilter", emailFilter);
        model.addAttribute("gradoFilter", gradoFilter);

        if (!model.containsAttribute("coordinador")) {
            model.addAttribute("coordinador", new Coordinador());
        }

        if ("new".equals(action)) {
            model.addAttribute("openModal", true);
        }

        return "gestion/gestion-coordinadores";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Coordinador> coordinadorOpt = coordinadorService.getById(id);
        if (coordinadorOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Coordinador no encontrado.");
            return "redirect:/gestion/coordinadores";
        }

        model.addAttribute("coordinadores", coordinadorService.getAll());
        model.addAttribute("totalCoordinadores", coordinadorService.countTotal());
        model.addAttribute("coordinadoresActivos", coordinadorService.countTotal());
        model.addAttribute("listaGrados", gradoService.getAll());
        model.addAttribute("coordinador", coordinadorOpt.get());
        model.addAttribute("openModal", true);

        return "gestion/gestion-coordinadores";
    }

    @PostMapping("/guardar")
    public String guardarCoordinador(@ModelAttribute("coordinador") Coordinador coordinador, RedirectAttributes redirectAttributes) {
        try {
            boolean isNew = coordinador.getIdCoordinador() == null;
            if (!isNew && (coordinador.getContrasena() == null || coordinador.getContrasena().trim().isEmpty())) {
                coordinadorService.getById(coordinador.getIdCoordinador())
                        .map(Coordinador::getContrasena)
                        .ifPresent(coordinador::setContrasena);
            }

            coordinadorService.save(coordinador);
            String successMessage = isNew ? "Coordinador creado correctamente." : "Coordinador actualizado correctamente.";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: El email '" + coordinador.getEmail() + "' ya está registrado.");
            redirectAttributes.addFlashAttribute("coordinador", coordinador);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al guardar el coordinador.");
            redirectAttributes.addFlashAttribute("coordinador", coordinador);
        }
        return "redirect:/gestion/coordinadores";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCoordinador(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            if (coordinadorService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Coordinador eliminado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró el coordinador para eliminar.");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acción denegada: El coordinador no puede ser eliminado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado al eliminar el coordinador.");
        }
        return "redirect:/gestion/coordinadores";
    }
}