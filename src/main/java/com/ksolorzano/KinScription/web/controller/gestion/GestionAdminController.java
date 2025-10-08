package com.ksolorzano.KinScription.web.controller.gestion;

import com.ksolorzano.KinScription.dominio.service.AdministradorService;
import com.ksolorzano.KinScription.persistence.entity.Administrador;
import com.ksolorzano.KinScription.persistence.entity.RolAdministrador;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/gestion/administradores")
public class GestionAdminController {

    private final AdministradorService adminService;

    @Autowired
    public GestionAdminController(AdministradorService adminService) {
        this.adminService = adminService;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("roles", RolAdministrador.values());
    }

    @GetMapping
    public String listarAdmins(Model model) {
        List<Administrador> administradores = adminService.getAll();
        model.addAttribute("administradores", administradores);

        if (!model.containsAttribute("admin")) {
            model.addAttribute("admin", new Administrador());
        }

        return "admin/gestion/gestion-administradores";
    }

    @PostMapping("/guardar")
    public String guardarAdmin(@Valid @ModelAttribute("admin") Administrador admin,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("admin", admin);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.admin", result);
            redirectAttributes.addFlashAttribute("showModal", true);
            return "redirect:/admin/gestion/administradores";
        }

        try {
            String mensaje = (admin.getId() == null) ? "Administrador creado correctamente." : "Administrador actualizado correctamente.";
            adminService.save(admin);
            redirectAttributes.addFlashAttribute("successMessage", mensaje);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: El correo electrónico '" + admin.getEmail() + "' ya está en uso.");
            redirectAttributes.addFlashAttribute("admin", admin);
            redirectAttributes.addFlashAttribute("showModal", true);
        }

        return "redirect:/admin/gestion/administradores";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarAdmin(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            if (adminService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Administrador eliminado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró el administrador para eliminar.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error al eliminar el administrador.");
        }
        return "redirect:/admin/gestion/administradores";
    }
}