package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.AdmParticipanteRepository;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdmParticipanteService {

    private final AdmParticipanteRepository participanteRepository;
    // AlumnoService y TutorService.

    @Autowired
    public AdmParticipanteService(AdmParticipanteRepository participanteRepository) {
        this.participanteRepository = participanteRepository;
    }

    /**
     * Obtiene todos los participantes registrados.
     * @return Una lista de todos los participantes.
     */
    public List<AdmParticipante> getAll() {
        return participanteRepository.findAll();
    }

    /**
     * Obtiene un participante por su ID.
     * @param id El ID del participante.
     * @return Un Optional con el participante si existe.
     */
    public Optional<AdmParticipante> getById(int id) {
        return participanteRepository.findById(id);
    }

    /**
     * Guarda un nuevo participante o actualiza uno existente.
     * @param participante El objeto AdmParticipante a guardar.
     * @return El participante guardado con su ID actualizado.
     */
    public AdmParticipante save(AdmParticipante participante) {
        return participanteRepository.save(participante);
    }

    /**
     * Busca participantes por un estado específico.
     * @param estado El estado a filtrar.
     * @return Lista de participantes en ese estado.
     */
    public List<AdmParticipante> getByEstado(EstadoParticipante estado) {
        return participanteRepository.findByEstado(estado);
    }

    /**
     * Lógica para calificar el examen de un participante.
     * @param participanteId El ID del participante.
     * @param nota La nota obtenida en el examen.
     * @return El participante actualizado.
     * @throws IllegalStateException Si el participante no está en el estado PENDIENTE_EXAMEN.
     */
    @Transactional
    public AdmParticipante calificarExamen(int participanteId, double nota) {
        AdmParticipante participante = participanteRepository.findById(participanteId)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        if (participante.getEstado() != EstadoParticipante.PENDIENTE_EXAMEN) {
            throw new IllegalStateException("El participante no está en estado de pendiente de examen.");
        }

        participante.setNotaExamen(nota);
        if (nota >= 60) {
            participante.setEstado(EstadoParticipante.ADMITIDO_EXAMEN);
        } else {
            participante.setEstado(EstadoParticipante.DESAPROBADO);
        }

        return participanteRepository.save(participante);
    }

    @Transactional
    public Optional<AdmParticipante> update(int id, AdmParticipante newData) {
        return participanteRepository.findById(id).map(participante -> {
            participante.setNombreCompleto(newData.getNombreCompleto());
            participante.setApellidos(newData.getApellidos());
            participante.setFechaNacimiento(newData.getFechaNacimiento());
            participante.setDireccion(newData.getDireccion());
            // Actualizar el estado o la nota por esta vía debería ser una acción administrativa cuidadosa
            // y separada de los métodos de flujo de negocio.
            // participante.setEstado(newData.getEstado());
            return participanteRepository.save(participante);
        });
    }

    @Transactional
    public boolean delete(int id) {
        return getById(id).map(participante -> {
            participanteRepository.deleteById(id);
            return true;
        }).orElse(false);
    }

    // Otros metodos: aprobarSocioeconomico, generarCredencialesFinales, etc.
}