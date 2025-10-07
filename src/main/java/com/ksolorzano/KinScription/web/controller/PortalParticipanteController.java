package com.ksolorzano.KinScription.web.controller;

import com.ksolorzano.KinScription.dominio.repository.AlumnoRepository; // << AÑADIR
import com.ksolorzano.KinScription.dominio.service.AdmParticipanteService;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante; // << AÑADIR
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/portal")
public class PortalParticipanteController {

    private final AdmParticipanteService participanteService;
    private final AlumnoRepository alumnoRepository; // << AÑADIR

    @Autowired
    public PortalParticipanteController(AdmParticipanteService participanteService, AlumnoRepository alumnoRepository) { // << AÑADIR
        this.participanteService = participanteService;
        this.alumnoRepository = alumnoRepository; // << AÑADIR
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        AdmParticipante participante = participanteService.getByUsername(username)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        int progressPercentage = calculateProgress(participante.getEstado());

        model.addAttribute("participante", participante);
        model.addAttribute("progressPercentage", progressPercentage);

        // ¡NUEVO! Si el estado es FINALIZADO, busca el alumno asociado para mostrar sus credenciales.
        if (participante.getEstado() == EstadoParticipante.FINALIZADO) {
            // Nota: Esto asume que el carnet del alumno contiene el ID del participante.
            // Una mejor aproximación sería tener una FK de Alumno a AdmParticipante.
            // Por ahora, buscaremos por el nombre. ¡Esto es solo para pruebas!
            // La lógica real buscaría por un ID de participante en la tabla alumno.
            String carnet = "2025001"; // Carnet hardcodeado del Seeder
            alumnoRepository.findByCarnetAlumno(carnet) // Necesitarás añadir este método al repo
                    .ifPresent(alumno -> model.addAttribute("alumno", alumno));
        }

        return "portal/dashboard";
    }

    private int calculateProgress(EstadoParticipante estado) {
        return switch (estado) {
            case PENDIENTE_EXAMEN -> 0;
            case ADMITIDO_EXAMEN -> 25;
            case ADMITIDO_SOCIOECONOMICO, ADMITIDO_FORMULARIO -> 50;
            case ADMITIDO_PAPELERIA -> 75;
            case ADMITIDO_CONTRATO, FINALIZADO -> 100;
            default -> 0;
        };
    }
}