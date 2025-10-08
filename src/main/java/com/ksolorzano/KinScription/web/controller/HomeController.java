package com.ksolorzano.KinScription.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Punto de entrada después de un login exitoso.
     * Redirige al usuario a su dashboard correspondiente basado en su rol.
     */
    @GetMapping("/")
    public String home(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_PARTICIPANTE")) {
            return "redirect:/portal/dashboard";
        }

        // Si no es participante, es algún tipo de administrador
        if (request.isUserInRole("ROLE_ADMIN_INSCRIPCION") ||
                request.isUserInRole("ROLE_DIRECTOR_ADMIN") ||
                request.isUserInRole("ROLE_ORIENTACION") ||
                request.isUserInRole("ROLE_SECRETARIA") ||
                request.isUserInRole("ROLE_SUPER_ADMIN")) {
            return "redirect:/admin/dashboard"; // dashboard general para administradores
        }

        // Fallback por si acaso
        return "redirect:/login";
    }
}