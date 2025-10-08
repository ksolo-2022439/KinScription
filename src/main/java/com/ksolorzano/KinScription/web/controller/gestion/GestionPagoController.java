package com.ksolorzano.KinScription.web.controller.gestion;

import com.ksolorzano.KinScription.dominio.service.AdmReportePagoService;
import com.ksolorzano.KinScription.persistence.entity.AdmReportePago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/gestion/pagos")
public class GestionPagoController {

    private final AdmReportePagoService pagoService;

    @Autowired
    public GestionPagoController(AdmReportePagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public String listarPagos(Model model) {
        List<AdmReportePago> pagos = pagoService.getAll();
        model.addAttribute("pagos", pagos);

        if (!model.containsAttribute("pago")) {
            model.addAttribute("pago", new AdmReportePago());
        }

        return "admin/gestion/gestion-pagos";
    }

    @PostMapping("/guardar")
    public String guardarPago(@ModelAttribute("pago") AdmReportePago pago,
                              RedirectAttributes redirectAttributes) {

        String mensaje = (pago.getId() == null) ? "Registro de pago creado." : "Registro de pago actualizado.";
        pagoService.save(pago);
        redirectAttributes.addFlashAttribute("successMessage", mensaje);

        return "redirect:/admin/gestion/pagos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarPago(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        if (pagoService.delete(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Registro de pago eliminado.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró el registro.");
        }
        return "redirect:/admin/gestion/pagos";
    }
}