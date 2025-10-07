package com.ksolorzano.KinScription.dominio.repository;

import com.ksolorzano.KinScription.persistence.entity.Administrador;
import com.ksolorzano.KinScription.persistence.entity.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {

    /**
     * Busca el carnet de alumno más alto que comienza con el prefijo del año actual.
     * Esto nos permite encontrar el último número de carnet asignado en el año.
     * @param prefijoAno El prefijo del año, por ejemplo, "2025".
     * @return Un String con el carnet más alto encontrado, o null si no hay ninguno para ese año.
     */
    @Query("SELECT MAX(a.carnetAlumno) FROM Alumno a WHERE a.carnetAlumno LIKE :prefijoAno%")
    String findTopByCarnetAlumnoStartingWithOrderByCarnetAlumnoDesc(@Param("prefijoAno") String prefijoAno);

    boolean existsByCarnetAlumno(String carnet);
    boolean existsByEmailAcademico(String email);

    Optional<Alumno> findByCarnetAlumno(String carnet);
}