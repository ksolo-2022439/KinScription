package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.AdmEstudioSocioeconomicoRepository;
import com.ksolorzano.KinScription.persistence.entity.AdmEstudioSocioeconomico;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdmEstudioSocioeconomicoService {

    private final AdmEstudioSocioeconomicoRepository estudioSocioeconomicoRepository;
    private final AdmParticipanteService participanteService;

    @Autowired
    public AdmEstudioSocioeconomicoService(AdmEstudioSocioeconomicoRepository estudioSocioeconomicoRepository, AdmParticipanteService participanteService) {
        this.estudioSocioeconomicoRepository = estudioSocioeconomicoRepository;
        this.participanteService = participanteService;
    }

    /**
     * Guarda un estudio socioeconómico.
     * @param estudio El objeto a guardar.
     * @return El estudio guardado.
     */
    public AdmEstudioSocioeconomico save(AdmEstudioSocioeconomico estudio) {
        return estudioSocioeconomicoRepository.save(estudio);
    }

    /**
     * Busca un estudio por el participante asociado.
     * @param participante El participante.
     * @return Un optional con el estudio si existe.
     */
    public Optional<AdmEstudioSocioeconomico> getByParticipante(AdmParticipante participante) {
        return estudioSocioeconomicoRepository.findByParticipante(participante);
    }

    /**
     * Lógica para la aprobación por parte del Director Administrativo.
     * @param estudioId El ID del estudio socioeconómico.
     */
    @Transactional
    public void aprobarPorDirector(int estudioId) {
        AdmEstudioSocioeconomico estudio = estudioSocioeconomicoRepository.findById(estudioId)
                .orElseThrow(() -> new RuntimeException("Estudio no encontrado"));

        // Lógica de validación
        AdmParticipante participante = estudio.getParticipante();
        if(participante.getEstado() != EstadoParticipante.ADMITIDO_EXAMEN) {
            throw new IllegalStateException("El participante no está en el estado correcto para esta acción.");
        }

        estudio.setAprobadoDirector(true);
        participante.setEstado(EstadoParticipante.ADMITIDO_SOCIOECONOMICO);

        estudioSocioeconomicoRepository.save(estudio);
        participanteService.save(participante);
    }

    /**
     * Lógica para la aprobación final por parte de Orientación.
     * @param estudioId El ID del estudio socioeconómico.
     */
    @Transactional
    public void aprobarPorOrientacion(int estudioId) {
        AdmEstudioSocioeconomico estudio = estudioSocioeconomicoRepository.findById(estudioId)
                .orElseThrow(() -> new RuntimeException("Estudio no encontrado"));

        AdmParticipante participante = estudio.getParticipante();
        if(participante.getEstado() != EstadoParticipante.ADMITIDO_SOCIOECONOMICO) {
            throw new IllegalStateException("El participante debe ser aprobado primero por el director.");
        }

        estudio.setAprobadoOrientacion(true);
        participante.setEstado(EstadoParticipante.ADMITIDO_FORMULARIO);

        estudioSocioeconomicoRepository.save(estudio);
        participanteService.save(participante);

        // Aquí se dispararía la lógica para generar el reporte de pagos.
    }

    public Optional<AdmEstudioSocioeconomico> getById(int id) {
        return estudioSocioeconomicoRepository.findById(id);
    }

    @Transactional
    public Optional<AdmEstudioSocioeconomico> update(int id, AdmEstudioSocioeconomico newData) {
        return estudioSocioeconomicoRepository.findById(id).map(estudio -> {
            // Actualizar campos del formulario
            estudio.setDatosTutorNombre(newData.getDatosTutorNombre());
            estudio.setDatosTutorApellido(newData.getDatosTutorApellido());
            // etc. para todos los campos del formulario
            return estudioSocioeconomicoRepository.save(estudio);
        });
    }

    @Transactional
    public boolean delete(int id) {
        return getById(id).map(estudio -> {
            estudioSocioeconomicoRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}