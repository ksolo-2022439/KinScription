package com.ksolorzano.KinScription.web.controller;

import com.ksolorzano.KinScription.dominio.repository.AdmDocumentoRequeridoRepository;
import com.ksolorzano.KinScription.dominio.service.AdmContratoService;
import com.ksolorzano.KinScription.dominio.service.AdmDocumentoRequeridoService;
import com.ksolorzano.KinScription.dominio.service.AdmParticipanteService;
import com.ksolorzano.KinScription.persistence.entity.AdmDocumentoRequerido;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/secretaria")
public class SecretariaController {

    private final AdmParticipanteService participanteService;
    private final AdmDocumentoRequeridoService documentoService;
    private final AdmContratoService contratoService;
    private final AdmDocumentoRequeridoRepository documentoRepository;

    public SecretariaController(AdmParticipanteService participanteService, AdmDocumentoRequeridoService documentoService, AdmContratoService contratoService, AdmDocumentoRequeridoRepository dr) {
        this.participanteService = participanteService;
        this.documentoService = documentoService;
        this.contratoService = contratoService;
        this.documentoRepository = dr;
    }

    /**
     * Muestra los participantes listos para la revisión de papelería.
     */
    @GetMapping("/papeleria")
    public String listarPendientesPapeleria(Model model) {
        List<AdmParticipante> participantes = participanteService.getByEstado(EstadoParticipante.PAPELERIA_ENVIADA);
        model.addAttribute("participantes", participantes);
        return "admin/secretaria/lista_papeleria";
    }

    /**
     * Muestra la página de detalle para revisar todos los documentos de un participante.
     * @param id El ID del PARTICIPANTE.
     */
    @GetMapping("/papeleria/revisar/{id}")
    public String revisarPapeleria(@PathVariable("id") int id, Model model) {
        AdmParticipante participante = participanteService.getById(id)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        List<AdmDocumentoRequerido> documentos = documentoRepository.findByParticipante(participante);

        model.addAttribute("participante", participante);
        model.addAttribute("documentos", documentos);

        return "admin/secretaria/revisar_papeleria";
    }

    /**
     * Procesa la aprobación de toda la papelería de un participante.
     */
    @PostMapping("/papeleria/aprobar")
    public String aprobarPapeleria(@RequestParam("participanteId") int participanteId) {
        documentoService.aprobarPapeleriaCompleta(participanteId);
        return "redirect:/admin/secretaria/papeleria";
    }

    /**
     * Muestra la lista de contratos subidos que están pendientes de aprobación.
     */
    @GetMapping("/contratos")
    public String listarContratos(Model model) {
        List<AdmParticipante> participantes = participanteService.getByEstado(EstadoParticipante.CONTRATO_ENVIADO);
        model.addAttribute("participantes", participantes);
        return "admin/secretaria/lista_contratos";
    }

    /**
     * Procesa la aprobación final del contrato, el último paso del proceso.
     */
    @PostMapping("/contratos/aprobar")
    public String aprobarContrato(@RequestParam("contratoId") int contratoId) {
        contratoService.aprobarContrato(contratoId);
        // ¡PASO CLAVE! Después de aprobar, se debe llamar al servicio
        // para "graduar" al participante y convertirlo en alumno.
        // Ej: participanteService.finalizarProcesoAdmision(participanteId);
        return "redirect:/admin/secretaria/contratos";
    }

    @PostMapping("/participante/rechazar")
    public String rechazarParticipante(@RequestParam("participanteId") int participanteId, @RequestParam("origen") String origen) {
        AdmParticipante participante = participanteService.getById(participanteId)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        participante.setEstado(EstadoParticipante.DESAPROBADO);
        participanteService.save(participante);

        // Redirige a la página desde donde se hizo el rechazo (papelería o contratos)
        if ("papeleria".equals(origen)) {
            return "redirect:/admin/secretaria/papeleria";
        }
        return "redirect:/admin/secretaria/contratos";
    }
}