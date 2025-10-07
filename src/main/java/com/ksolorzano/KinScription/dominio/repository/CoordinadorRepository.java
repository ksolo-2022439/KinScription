package com.ksolorzano.KinScription.dominio.repository;

import com.ksolorzano.KinScription.persistence.entity.Coordinador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoordinadorRepository extends JpaRepository<Coordinador, Integer> {
    Optional<Coordinador> findByEmail(String email);
}