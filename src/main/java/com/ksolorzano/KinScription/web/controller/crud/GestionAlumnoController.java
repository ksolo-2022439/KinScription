package com.ksolorzano.KinScription.web.controller.crud;

import com.ksolorzano.KinScription.dominio.service.*;
import com.ksolorzano.KinScription.persistence.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gestion/alumnos")
public class GestionAlumnoController {

    private final AlumnoService alumnoService;
    private final GradoService gradoService;
    private final SeccionService seccionService;
    private final JornadaService jornadaService;
    private final CarreraService carreraService;
    private final TutorService tutorService;

    @Autowired
    public GestionAlumnoController(AlumnoService alumnoService, GradoService gradoService, SeccionService seccionService, JornadaService jornadaService, CarreraService carreraService, TutorService tutorService) {
        this.alumnoService = alumnoService;
        this.gradoService = gradoService;
        this.seccionService = seccionService;
        this.jornadaService = jornadaService;
        this.carreraService = carreraService;
        this.tutorService = tutorService;
    }

    @GetMapping
    public String mostrarPaginaAlumnos(@RequestParam(required = false) String carnetFilter,
                                       @RequestParam(required = false) String nombreFilter,
                                       @RequestParam(required = false) String emailFilter,
                                       @RequestParam(required = false) Integer gradoFilter,
                                       @RequestParam(required = false) Integer seccionFilter,
                                       @RequestParam(required = false) Integer carreraFilter,
                                       @RequestParam(required = false) String action,
                                       Model model) {

        List<Alumno> alumnosFiltrados = alumnoService.buscarPorFiltros(carnetFilter, nombreFilter, emailFilter, gradoFilter, seccionFilter, carreraFilter);
        model.addAttribute("alumnos", alumnosFiltrados);
        model.addAttribute("totalAlumnos", alumnosFiltrados.size());
        model.addAttribute("alumnosActivos", alumnoService.countTotal());

        model.addAttribute("carnetFilter", carnetFilter);
        model.addAttribute("nombreFilter", nombreFilter);
        model.addAttribute("emailFilter", emailFilter);
        model.addAttribute("gradoFilter", gradoFilter);
        model.addAttribute("seccionFilter", seccionFilter);
        model.addAttribute("carreraFilter", carreraFilter);

        if (!model.containsAttribute("alumno")) {
            model.addAttribute("alumno", new Alumno());
        }

        if ("new".equals(action)) {
            model.addAttribute("openModal", true);
        }

        cargarListasDesplegables(model);
        return "gestion/gestion-alumnos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Alumno> alumnoOpt = alumnoService.getById(id);
        if (alumnoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Alumno no encontrado.");
            return "redirect:/gestion/alumnos";
        }

        List<Alumno> todosLosAlumnos = alumnoService.getAll();
        model.addAttribute("alumnos", todosLosAlumnos);
        model.addAttribute("totalAlumnos", todosLosAlumnos.size());
        model.addAttribute("alumnosActivos", alumnoService.countTotal());
        cargarListasDesplegables(model);
        model.addAttribute("alumno", alumnoOpt.get());
        model.addAttribute("openModal", true);

        return "gestion/gestion-alumnos";
    }

    @PostMapping("/guardar")
    public String guardarAlumno(@ModelAttribute("alumno") Alumno alumno,
                                @RequestParam("gradoId") Integer gradoId,
                                @RequestParam("seccionId") Integer seccionId,
                                @RequestParam(value = "jornadaId", required = false) Integer jornadaId,
                                @RequestParam(value = "carreraId", required = false) Integer carreraId,
                                @RequestParam("tutorId") Integer tutorId,
                                RedirectAttributes redirectAttributes) {
        try {
            gradoService.getById(gradoId).ifPresent(alumno::setGrado);
            seccionService.getById(seccionId).ifPresent(alumno::setSeccion);
            if (jornadaId != null) jornadaService.getById(jornadaId).ifPresent(alumno::setJornada);
            if (carreraId != null) carreraService.getById(carreraId).ifPresent(alumno::setCarrera);
            tutorService.getById(tutorId).ifPresent(alumno::setTutor);

            String successMessage;
            boolean isNew = alumno.getIdAlumno() == null;

            if (isNew) {
                alumnoService.save(alumno);
                successMessage = "Alumno creado correctamente.";
            } else {
                if (alumno.getContrasena() == null || alumno.getContrasena().trim().isEmpty()) {
                    alumnoService.getById(alumno.getIdAlumno())
                            .map(Alumno::getContrasena)
                            .ifPresent(alumno::setContrasena);
                }
                alumnoService.save(alumno);
                successMessage = "Alumno actualizado correctamente.";
            }
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar el alumno: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alumno", alumno);
        }
        return "redirect:/gestion/alumnos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarAlumno(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            if (alumnoService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Alumno eliminado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Alumno no encontrado para eliminar.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el alumno. Puede estar asociado a otros registros.");
        }
        return "redirect:/gestion/alumnos";
    }

    private void cargarListasDesplegables(Model model) {
        model.addAttribute("listaGrados", gradoService.getAll());
        model.addAttribute("listaSecciones", seccionService.getAll());
        model.addAttribute("listaJornadas", jornadaService.getAll());
        model.addAttribute("listaCarreras", carreraService.getAll());
        model.addAttribute("listaTutores", tutorService.getAll());
    }
}