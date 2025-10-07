package com.ksolorzano.KinScription.dominio.repository;

import com.ksolorzano.KinScription.persistence.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Integer> {
    Optional<Profesor> findByEmail(String email);
}