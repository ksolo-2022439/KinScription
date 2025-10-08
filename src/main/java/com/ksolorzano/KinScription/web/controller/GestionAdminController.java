package com.ksolorzano.KinScription.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/gestion")
public class GestionAdminController {

    /**
     * Muestra el menú principal de gestión de datos (CRUDs).
     * @return El nombre de la vista del menú de gestión.
     */
    @GetMapping
    public String gestionMenu() {
        return "admin/gestion/index";
    }
}