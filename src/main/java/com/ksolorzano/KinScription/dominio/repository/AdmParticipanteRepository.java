package com.ksolorzano.KinScription.dominio.repository;

import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmParticipanteRepository extends JpaRepository<AdmParticipante, Integer> {

    /**
     * Busca a un participante por su nombre de usuario.
     * @param username El username para el login del portal.
     * @return Un Optional que contiene al Participante si se encuentra.
     */
    Optional<AdmParticipante> findByUsername(String username);

    /**
     * Encuentra todos los participantes que se encuentran en un estado específico.
     * @param estado El estado a filtrar (ej. PENDIENTE_EXAMEN).
     * @return Una lista de participantes que coinciden con el estado.
     */
    List<AdmParticipante> findByEstado(EstadoParticipante estado);
}