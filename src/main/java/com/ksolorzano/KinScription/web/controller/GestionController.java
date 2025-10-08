package com.ksolorzano.KinScription.web.controller;

import com.ksolorzano.KinScription.dominio.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gestion")
public class GestionController {

    private final AlumnoService alumnoService;
    private final ProfesorService profesorService;
    private final CursoService cursoService;
    private final NotasService notasService;
    private final GradoCursoService gradoCursoService;
    private final GradoService gradoService;
    private final ProfesorAsignacionService profesorAsignacionService;

    @Autowired
    public GestionController(AlumnoService alumnoService, ProfesorService profesorService, CursoService cursoService, NotasService notasService, GradoCursoService gradoCursoService, GradoService gradoService, ProfesorAsignacionService profesorAsignacionService) {
        this.alumnoService = alumnoService;
        this.profesorService = profesorService;
        this.cursoService = cursoService;
        this.notasService = notasService;
        this.gradoCursoService = gradoCursoService;
        this.gradoService = gradoService;
        this.profesorAsignacionService = profesorAsignacionService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "gestion/login";
    }

    /**
     * Prepara y muestra el dashboard principal.
     * Carga las estadísticas de varias entidades y las listas necesarias para el asistente de IA.
     * @param model El modelo para pasar datos a la vista.
     * @return El nombre de la plantilla del dashboard.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalAlumnos", alumnoService.countTotal());
        model.addAttribute("totalProfesores", profesorService.countTotal());
        model.addAttribute("totalNotas", notasService.countTotal());
        model.addAttribute("totalAsignaciones", gradoCursoService.countTotal());
        model.addAttribute("totalProfAsignaciones", profesorAsignacionService.countTotal());

        // Listas para los dropdowns del asistente de IA
        model.addAttribute("listaCursos", cursoService.getAll());
        model.addAttribute("listaGrados", gradoService.getAll());

        return "gestion/gestion-dashboard";
    }

    @GetMapping("/recovery")
    public String showForgotPasswordForm() {
        return "gestion/recuperar-contrasena";
    }
}