package com.ksolorzano.KinScription.dominio.repository;

import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmParticipanteRepository extends JpaRepository<AdmParticipante, Integer> {

    @Override
    @Query("SELECT p FROM AdmParticipante p WHERE p.activo = true")
    List<AdmParticipante> findAll();

    @Query("SELECT p FROM AdmParticipante p WHERE p.id = :id AND p.activo = true")
    Optional<AdmParticipante> findByIdActivo(@Param("id") int id);

    @Query("SELECT p FROM AdmParticipante p WHERE p.username = :username AND p.activo = true")
    Optional<AdmParticipante> findByUsername(@Param("username") String username);

    @Query("SELECT p FROM AdmParticipante p WHERE p.estado = :estado AND p.activo = true")
    List<AdmParticipante> findByEstado(@Param("estado") EstadoParticipante estado);
}