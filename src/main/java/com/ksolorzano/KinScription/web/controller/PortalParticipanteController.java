package com.ksolorzano.KinScription.web.controller;

import com.ksolorzano.KinScription.dominio.service.AdmParticipanteService;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/portal")
public class PortalParticipanteController {

    private final AdmParticipanteService participanteService;

    @Autowired
    public PortalParticipanteController(AdmParticipanteService participanteService) {
        this.participanteService = participanteService;
    }

    /**
     * Muestra el dashboard principal del participante.
     * Este método es la clave, ya que obtiene el estado actual del participante
     * y lo pasa a la vista.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        AdmParticipante participante = participanteService.getByUsername(username)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        model.addAttribute("participante", participante);
        return "portal/dashboard";
    }

    // Aquí irían los métodos @PostMapping para manejar los envíos de formularios.
}