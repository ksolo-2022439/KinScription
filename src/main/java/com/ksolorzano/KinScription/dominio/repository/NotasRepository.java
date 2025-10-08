package com.ksolorzano.KinScription.dominio.repository;

import com.ksolorzano.KinScription.persistence.entity.Alumno;
import com.ksolorzano.KinScription.persistence.entity.Notas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotasRepository extends JpaRepository<Notas, Integer>, JpaSpecificationExecutor<Notas> {

    /**
     * Busca todas las notas asociadas a un alumno específico.
     * Spring Data JPA generará automáticamente la consulta a partir del nombre del método.
     * @param alumno El objeto Alumno por el cual filtrar.
     * @return Una lista de las notas para el alumno dado.
     */
    List<Notas> findByAlumno(Alumno alumno);
}