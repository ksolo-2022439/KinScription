package com.ksolorzano.KinScription.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gestion")
public class GestionController {

    @GetMapping("/login")
    public String loginPage() {
        return "gestion/login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "gestion/dashboard";
    }

    @GetMapping("/recovery")
    public String showForgotPasswordForm() {
        return "gestion/recuperar-contrasena";
    }
}