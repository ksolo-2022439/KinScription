package com.ksolorzano.KinScription.dominio.repository;

import com.ksolorzano.KinScription.persistence.entity.AdmDocumentoRequerido;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmDocumentoRequeridoRepository extends JpaRepository<AdmDocumentoRequerido, Integer> {

    /**
     * Busca todos los documentos que han sido subidos por un participante específico.
     * Como un participante puede subir varios documentos, el resultado es una lista.
     * @param participante El objeto del participante por el cual filtrar.
     * @return Una lista de los documentos subidos por ese participante.
     */
    List<AdmDocumentoRequerido> findByParticipante(AdmParticipante participante);

    /**
     * Busca un documento específico (por su nombre) que haya sido subido por un participante específico.
     * Esto es útil para comprobar si un documento en particular ya existe antes de volver a subirlo.
     * @param participante El objeto del participante.
     * @param nombreDocumento El nombre del documento a buscar (ej. "Fe de Edad").
     * @return Un Optional que contiene el documento si se encuentra.
     */
    Optional<AdmDocumentoRequerido> findByParticipanteAndNombreDocumento(AdmParticipante participante, String nombreDocumento);
}