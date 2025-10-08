package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.AdmDocumentoRequeridoRepository;
import com.ksolorzano.KinScription.persistence.entity.AdmDocumentoRequerido;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdmDocumentoRequeridoService {

    private final AdmDocumentoRequeridoRepository documentoRequeridoRepository;
    private final AdmParticipanteService participanteService;

    @Autowired
    public AdmDocumentoRequeridoService(AdmDocumentoRequeridoRepository documentoRequeridoRepository, AdmParticipanteService participanteService) {
        this.documentoRequeridoRepository = documentoRequeridoRepository;
        this.participanteService = participanteService;
    }

    /**
     * Guarda un documento subido por un participante.
     * @param documento El objeto del documento.
     * @return El documento guardado.
     */
    public AdmDocumentoRequerido save(AdmDocumentoRequerido documento) {
        return documentoRequeridoRepository.save(documento);
    }

    /**
     * Lógica para que Secretaría apruebe toda la papelería de un participante.
     * Este método asume que se aprueban todos los documentos en conjunto.
     * @param participanteId El ID del participante cuya papelería se aprueba.
     */
    @Transactional
    public void aprobarPapeleriaCompleta(int participanteId) {
        AdmParticipante participante = participanteService.getById(participanteId)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        if (participante.getEstado() != EstadoParticipante.PAPELERIA_ENVIADA) {
            throw new IllegalStateException("El participante no ha completado el paso de formulario socioeconómico.");
        }

        // Aquí podría ir una lógica para verificar que todos los documentos requeridos esten, pero la voy a omitir

        participante.setEstado(EstadoParticipante.ADMITIDO_PAPELERIA);
        participanteService.save(participante);
    }

    public Optional<AdmDocumentoRequerido> getById(Integer id) {
        return documentoRequeridoRepository.findById(id);
    }

    @Transactional
    public Optional<AdmDocumentoRequerido> update(Integer id, AdmDocumentoRequerido newData) {
        return documentoRequeridoRepository.findById(id).map(doc -> {
            doc.setNombreDocumento(newData.getNombreDocumento());
            doc.setUrlArchivo(newData.getUrlArchivo());
            doc.setEstadoRevision(newData.getEstadoRevision());
            return documentoRequeridoRepository.save(doc);
        });
    }

    @Transactional
    public boolean delete(Integer id) {
        return getById(id).map(doc -> {
            documentoRequeridoRepository.deleteById(id);
            return true;
        }).orElse(false);
    }

    /**
     * Obtiene una lista de todos los documentos requeridos subidos en el sistema.
     * @return Una lista de todas las entidades AdmDocumentoRequerido.
     */
    public List<AdmDocumentoRequerido> getAll() {
        return documentoRequeridoRepository.findAll();
    }
}