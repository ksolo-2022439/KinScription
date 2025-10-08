package com.ksolorzano.KinScription.web.controller.gestion;

import com.ksolorzano.KinScription.dominio.service.AdmContratoService;
import com.ksolorzano.KinScription.persistence.entity.AdmContrato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/gestion/contratos")
public class GestionContratoController {

    private final AdmContratoService contratoService;

    @Autowired
    public GestionContratoController(AdmContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @GetMapping
    public String listarContratos(Model model) {
        // Para este CRUD, necesitamos obtener todos los contratos.
        // El servicio no tiene un método getAll(), así que lo añadiremos.
        List<AdmContrato> contratos = contratoService.getAll();
        model.addAttribute("contratos", contratos);

        if (!model.containsAttribute("contrato")) {
            model.addAttribute("contrato", new AdmContrato());
        }

        return "admin/gestion/gestion-contratos";
    }

    @PostMapping("/guardar")
    public String guardarContrato(@ModelAttribute("contrato") AdmContrato contrato,
                                  RedirectAttributes redirectAttributes) {


        String mensaje = (contrato.getId() == null) ? "Contrato creado correctamente." : "Contrato actualizado correctamente.";
        contratoService.save(contrato);
        redirectAttributes.addFlashAttribute("successMessage", mensaje);

        return "redirect:/admin/gestion/contratos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarContrato(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            if (contratoService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Contrato eliminado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró el contrato para eliminar.");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: No se puede eliminar el contrato, puede estar referenciado en otra parte.");
        }
        return "redirect:/admin/gestion/contratos";
    }
}