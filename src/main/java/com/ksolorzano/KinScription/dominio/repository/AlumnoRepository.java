package com.ksolorzano.KinScription.dominio.repository;

import com.ksolorzano.KinScription.persistence.entity.Administrador;
import com.ksolorzano.KinScription.persistence.entity.Alumno;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    /**
     * Busca un Alumno basándose en el ID de su registro de participante de origen.
     * El nombre sigue la convención de Spring: findBy[CampoEnLaEntidad]_[CampoEnLaEntidadAnidada]
     * @param participanteId El ID del AdmParticipante.
     * @return Un Optional que contiene al Alumno si se encuentra.
     */
    Optional<Alumno> findByAdmParticipante_Id(Integer participanteId);

    boolean existsByCarnetAlumno(String carnet);
    boolean existsByEmailAcademico(String email);

    Optional<Alumno> findByCarnetAlumno(String carnet);

    List<Alumno> findAll(Specification<Alumno> spec, Sort unsorted);
}