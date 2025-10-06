package com.ksolorzano.KinScription.dominio.repository;

import com.ksolorzano.KinScription.persistence.entity.AdmContrato;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdmContratoRepository extends JpaRepository<AdmContrato, Integer> {
    Optional<AdmContrato> findByParticipante(AdmParticipante participante);
}