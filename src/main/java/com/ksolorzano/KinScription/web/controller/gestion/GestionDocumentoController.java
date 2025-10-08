package com.ksolorzano.KinScription.web.controller.gestion;

import com.ksolorzano.KinScription.dominio.service.AdmDocumentoRequeridoService;
import com.ksolorzano.KinScription.persistence.entity.AdmDocumentoRequerido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/gestion/documentos")
public class GestionDocumentoController {

    private final AdmDocumentoRequeridoService documentoService;

    @Autowired
    public GestionDocumentoController(AdmDocumentoRequeridoService documentoService) {
        this.documentoService = documentoService;
    }

    @GetMapping
    public String listarDocumentos(Model model) {
        List<AdmDocumentoRequerido> documentos = documentoService.getAll();
        model.addAttribute("documentos", documentos);

        if (!model.containsAttribute("documento")) {
            model.addAttribute("documento", new AdmDocumentoRequerido());
        }

        return "admin/gestion/gestion-documentos";
    }

    @PostMapping("/guardar")
    public String guardarDocumento(@ModelAttribute("documento") AdmDocumentoRequerido documento,
                                   RedirectAttributes redirectAttributes) {

        String mensaje = (documento.getId() == null) ? "Documento creado correctamente." : "Documento actualizado correctamente.";
        documentoService.save(documento);
        redirectAttributes.addFlashAttribute("successMessage", mensaje);

        return "redirect:/admin/gestion/documentos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarDocumento(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            if (documentoService.delete(id)) {
                redirectAttributes.addFlashAttribute("successMessage", "Documento eliminado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró el documento para eliminar.");
            }
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: No se puede eliminar el documento, puede estar referenciado.");
        }
        return "redirect:/admin/gestion/documentos";
    }
}