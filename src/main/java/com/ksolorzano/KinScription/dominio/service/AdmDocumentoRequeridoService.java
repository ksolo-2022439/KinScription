package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.AdmDocumentoRequeridoRepository;
import com.ksolorzano.KinScription.persistence.entity.AdmDocumentoRequerido;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdmDocumentoRequeridoService {

    public static final List<String> DOCUMENTOS_REQUERIDOS = List.of(
            "Fe de Edad", "Acta de Nacimiento", "Certificado 3ro Básico"
    );

    private final AdmDocumentoRequeridoRepository documentoRepository;
    private final AdmParticipanteService participanteService;

    @Autowired
    public AdmDocumentoRequeridoService(AdmDocumentoRequeridoRepository dr, AdmParticipanteService ps) {
        this.documentoRepository = dr;
        this.participanteService = ps;
    }

    @Transactional
    public void solicitarCorrecciones(int participanteId, List<String> documentosASolicitar) {
        AdmParticipante participante = participanteService.getById(participanteId)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        if (participante.getEstado() != EstadoParticipante.PAPELERIA_ENVIADA) {
            throw new IllegalStateException("El participante no está en el estado correcto para esta acción.");
        }

        List<AdmDocumentoRequerido> documentosSubidos = documentoRepository.findByParticipante(participante);
        documentosSubidos.forEach(doc -> {
            doc.setEstadoRevision("APROBADO");
            doc.setComentarioRechazo(null);
            documentoRepository.save(doc);
        });

        for (String docNombre : documentosASolicitar) {
            Optional<AdmDocumentoRequerido> docOpt = documentoRepository.findByParticipanteAndNombreDocumento(participante, docNombre);

            AdmDocumentoRequerido docParaRechazar;
            if (docOpt.isPresent()) {
                docParaRechazar = docOpt.get();
            } else {
                docParaRechazar = new AdmDocumentoRequerido();
                docParaRechazar.setParticipante(participante);
                docParaRechazar.setNombreDocumento(docNombre);
                docParaRechazar.setUrlArchivo("PENDIENTE_DE_SUBIR");
            }

            docParaRechazar.setEstadoRevision("RECHAZADO");
            //docParaRechazar.setComentarioRechazo("Secretaría ha solicitado este documento.");
            documentoRepository.save(docParaRechazar);
        }

        participante.setEstado(EstadoParticipante.ADMITIDO_FORMULARIO);
        participanteService.save(participante);
    }

    @Transactional
    public void aprobarPapeleria(int participanteId) {
        AdmParticipante participante = participanteService.getById(participanteId)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        if (participante.getEstado() != EstadoParticipante.PAPELERIA_ENVIADA) {
            throw new IllegalStateException("El participante no está en el estado correcto para esta acción.");
        }

        List<AdmDocumentoRequerido> documentosSubidos = documentoRepository.findByParticipante(participante);
        Map<String, AdmDocumentoRequerido> mapaSubidos = documentosSubidos.stream()
                .collect(Collectors.toMap(AdmDocumentoRequerido::getNombreDocumento, doc -> doc));

        for(String docRequerido : DOCUMENTOS_REQUERIDOS){
            if(!mapaSubidos.containsKey(docRequerido)){
                throw new IllegalStateException("No se puede aprobar: falta el documento '" + docRequerido + "'.");
            }
        }

        documentosSubidos.forEach(doc -> {
            doc.setEstadoRevision("APROBADO");
            doc.setComentarioRechazo(null);
            documentoRepository.save(doc);
        });

        participante.setEstado(EstadoParticipante.ADMITIDO_PAPELERIA);
        participanteService.save(participante);
    }

    public AdmDocumentoRequerido save(AdmDocumentoRequerido documento) {
        return documentoRepository.save(documento);
    }

    public Optional<AdmDocumentoRequerido> getById(Integer id) {
        return documentoRepository.findById(id);
    }

    public List<AdmDocumentoRequerido> getAll() {
        return documentoRepository.findAll();
    }

    public boolean delete(Integer id) {
        return getById(id).map(doc -> {
            documentoRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}