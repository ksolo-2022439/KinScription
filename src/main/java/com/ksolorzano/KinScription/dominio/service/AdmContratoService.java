package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.AdmContratoRepository;
import com.ksolorzano.KinScription.persistence.entity.AdmContrato;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar la lógica de negocio de los contratos de admisión.
 */
@Service
public class AdmContratoService {

    private final AdmContratoRepository contratoRepository;
    private final AdmParticipanteService participanteService;

    /**
     * Constructor para inyección de dependencias.
     *
     * @param contratoRepository  Repositorio para la entidad AdmContrato.
     * @param participanteService Servicio para la entidad AdmParticipante.
     */
    @Autowired
    public AdmContratoService(AdmContratoRepository contratoRepository, AdmParticipanteService participanteService) {
        this.contratoRepository = contratoRepository;
        this.participanteService = participanteService;
    }

    /**
     * Guarda la información de un contrato en la base de datos.
     *
     * @param contrato El objeto {@link AdmContrato} a guardar.
     * @return El contrato guardado.
     */
    public AdmContrato save(AdmContrato contrato) {
        return contratoRepository.save(contrato);
    }

    /**
     * Busca un contrato por su ID único.
     *
     * @param id El ID del contrato.
     * @return Un {@link Optional} que contiene el contrato si se encuentra.
     */
    public Optional<AdmContrato> getById(int id) {
        return contratoRepository.findById(id);
    }

    /**
     * Procesa la aprobación de un contrato. Este es el último paso administrativo.
     * La operación realiza dos acciones clave dentro de una misma transacción:
     * 1. Cambia el estado del participante a ADMITIDO_CONTRATO.
     * 2. Llama al servicio de participante para finalizar el proceso y crear la entidad Alumno.
     *
     * @param contratoId El ID del contrato a aprobar.
     * @throws IllegalStateException Si el participante no ha completado el paso de papelería.
     */
    @Transactional
    public void aprobarContrato(int contratoId) {
        AdmContrato contrato = getById(contratoId)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado con ID: " + contratoId));

        AdmParticipante participante = contrato.getParticipante();
        if (participante.getEstado() != EstadoParticipante.CONTRATO_ENVIADO) {
            throw new IllegalStateException("El participante no ha completado el paso de papelería.");
        }

        contrato.setAprobado(true);
        participante.setEstado(EstadoParticipante.ADMITIDO_CONTRATO);

        contratoRepository.save(contrato);
        participanteService.save(participante);

        // Dispara el proceso final para convertir al participante en alumno
        participanteService.finalizarProcesoYCrearAlumno(participante.getId());
    }

    /**
     * Actualiza los datos de un contrato existente.
     *
     * @param id      El ID del contrato a modificar.
     * @param newData Un objeto AdmContrato con la nueva información.
     * @return Un {@link Optional} con el contrato actualizado.
     */
    @Transactional
    public Optional<AdmContrato> update(int id, AdmContrato newData) {
        return getById(id).map(contrato -> {
            contrato.setNombreAbogado(newData.getNombreAbogado());
            contrato.setColegiadoAbogado(newData.getColegiadoAbogado());
            contrato.setUrlPdfContrato(newData.getUrlPdfContrato());
            return contratoRepository.save(contrato);
        });
    }

    /**
     * Elimina un contrato de la base de datos.
     *
     * @param id El ID del contrato a eliminar.
     * @return {@code true} si se eliminó con éxito, {@code false} si no se encontró.
     */
    @Transactional
    public boolean delete(int id) {
        return getById(id).map(contrato -> {
            contratoRepository.deleteById(id);
            return true;
        }).orElse(false);
    }

    public Optional<AdmContrato> findByParticipante(AdmParticipante participante) {
        return contratoRepository.findByParticipante(participante);
    }

    public List<AdmContrato> getAll() {
        return contratoRepository.findAll();
    }
}