package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.AdmContratoRepository;
import com.ksolorzano.KinScription.persistence.entity.AdmContrato;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdmContratoService {

    private final AdmContratoRepository contratoRepository;
    private final AdmParticipanteService participanteService;

    @Autowired
    public AdmContratoService(AdmContratoRepository contratoRepository, AdmParticipanteService participanteService) {
        this.contratoRepository = contratoRepository;
        this.participanteService = participanteService;
    }

    /**
     * Guarda la información del contrato.
     * @param contrato El objeto a guardar.
     * @return El contrato guardado.
     */
    public AdmContrato save(AdmContrato contrato) {
        return contratoRepository.save(contrato);
    }

    /**
     * Lógica para que un administrativo apruebe el contrato subido.
     * @param contratoId El ID del contrato a aprobar.
     */
    @Transactional
    public void aprobarContrato(int contratoId) {
        AdmContrato contrato = contratoRepository.findById(contratoId)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

        AdmParticipante participante = contrato.getParticipante();
        if (participante.getEstado() != EstadoParticipante.ADMITIDO_PAPELERIA) {
            throw new IllegalStateException("El participante no ha completado el paso de papelería.");
        }

        contrato.setAprobado(true);
        participante.setEstado(EstadoParticipante.ADMITIDO_CONTRATO);

        contratoRepository.save(contrato);
        participanteService.save(participante);

        // Este es el último paso. Aquí se llamaría al método final
        // para crear el alumno, sus credenciales y cambiar el estado a FINALIZADO.
    }

    public Optional<AdmContrato> getById(int id) {
        return contratoRepository.findById(id);
    }

    @Transactional
    public Optional<AdmContrato> update(int id, AdmContrato newData) {
        return contratoRepository.findById(id).map(contrato -> {
            contrato.setNombreAbogado(newData.getNombreAbogado());
            contrato.setColegiadoAbogado(newData.getColegiadoAbogado());
            contrato.setUrlPdfContrato(newData.getUrlPdfContrato());
            return contratoRepository.save(contrato);
        });
    }

    @Transactional
    public boolean delete(int id) {
        return getById(id).map(contrato -> {
            contratoRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}