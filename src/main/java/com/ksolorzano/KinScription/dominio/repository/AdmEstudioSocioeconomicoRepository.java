package com.ksolorzano.KinScription.dominio.repository;

import com.ksolorzano.KinScription.persistence.entity.AdmEstudioSocioeconomico;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdmEstudioSocioeconomicoRepository extends JpaRepository<AdmEstudioSocioeconomico, Integer> {
    Optional<AdmEstudioSocioeconomico> findByParticipante(AdmParticipante participante);
}