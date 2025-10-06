package com.ksolorzano.KinScription.dominio.repository;

import com.ksolorzano.KinScription.persistence.entity.GradoCurso;
import com.ksolorzano.KinScription.persistence.entity.GradoCursoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradoCursoRepository extends JpaRepository<GradoCurso, GradoCursoId> { }