package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.AlumnoRepository;
import com.ksolorzano.KinScription.persistence.entity.Alumno;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;

    @Autowired
    public AlumnoService(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    public List<Alumno> getAll() {
        return alumnoRepository.findAll();
    }

    /**
     * Busca un alumno por su ID y fuerza la inicialización de sus relaciones de carga perezosa (LAZY).
     * Esto previene la LazyInitializationException en la capa de la vista.
     * @param id El ID del alumno a buscar.
     * @return Un Optional que contiene al Alumno con sus relaciones inicializadas si se encuentra.
     */
    @Transactional(readOnly = true)
    public Optional<Alumno> getById(int id) {
        Optional<Alumno> alumnoOpt = alumnoRepository.findById(id);
        alumnoOpt.ifPresent(alumno -> {
            Hibernate.initialize(alumno.getGrado());
            Hibernate.initialize(alumno.getSeccion());
            Hibernate.initialize(alumno.getJornada());
            Hibernate.initialize(alumno.getCarrera());
            Hibernate.initialize(alumno.getTutor());
        });
        return alumnoOpt;
    }

    public Alumno save(Alumno alumno) {
        return alumnoRepository.save(alumno);
    }

    /**
     * Actualiza un alumno existente. Si la contraseña en los nuevos datos es nula o está vacía,
     * se mantiene la contraseña existente para evitar que se sobrescriba accidentalmente.
     * @param id El ID del alumno a actualizar.
     * @param newData El objeto Alumno con los nuevos datos.
     * @return Un Optional con el alumno actualizado, o vacío si el alumno no fue encontrado.
     */
    @Transactional
    public Optional<Alumno> update(int id, Alumno newData) {
        return alumnoRepository.findById(id).map(alumno -> {
            alumno.setNombreCompleto(newData.getNombreCompleto());
            alumno.setApellidoCompleto(newData.getApellidoCompleto());
            alumno.setDireccion(newData.getDireccion());
            alumno.setGrado(newData.getGrado());
            alumno.setSeccion(newData.getSeccion());
            alumno.setJornada(newData.getJornada());
            alumno.setCarrera(newData.getCarrera());
            alumno.setTutor(newData.getTutor());

            if (StringUtils.hasText(newData.getContrasena())) {
                alumno.setContrasena(newData.getContrasena());
            }
            return alumnoRepository.save(alumno);
        });
    }

    @Transactional
    public boolean delete(int id) {
        return getById(id).map(alumno -> {
            alumnoRepository.deleteById(id);
            return true;
        }).orElse(false);
    }

    public long countTotal() {
        return alumnoRepository.count();
    }

    /**
     * Busca alumnos aplicando un conjunto de filtros dinámicos.
     * Construye una consulta utilizando la API de Especificación de JPA.
     *
     * @param carnetFiltro  Filtro para el carnet del alumno (búsqueda parcial).
     * @param nombreFiltro  Filtro para el nombre completo del alumno (búsqueda parcial).
     * @param emailFiltro   Filtro para el email académico del alumno (búsqueda parcial).
     * @param gradoId       Filtro para el ID del grado.
     * @param seccionId     Filtro para el ID de la sección.
     * @param carreraId     Filtro para el ID de la carrera.
     * @return Una lista de alumnos que coinciden con los criterios de búsqueda.
     */
    public List<Alumno> buscarPorFiltros(String carnetFiltro, String nombreFiltro, String emailFiltro, Integer gradoId, Integer seccionId, Integer carreraId) {
        Specification<Alumno> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(carnetFiltro)) {
                predicates.add(criteriaBuilder.like(root.get("carnetAlumno"), "%" + carnetFiltro + "%"));
            }
            if (StringUtils.hasText(nombreFiltro)) {
                predicates.add(criteriaBuilder.like(root.get("nombreCompleto"), "%" + nombreFiltro + "%"));
            }
            if (StringUtils.hasText(emailFiltro)) {
                predicates.add(criteriaBuilder.like(root.get("emailAcademico"), "%" + emailFiltro + "%"));
            }
            if (gradoId != null) {
                predicates.add(criteriaBuilder.equal(root.get("grado").get("idGrado"), gradoId));
            }
            if (seccionId != null) {
                predicates.add(criteriaBuilder.equal(root.get("seccion").get("idSeccion"), seccionId));
            }
            if (carreraId != null) {
                predicates.add(criteriaBuilder.equal(root.get("carrera").get("idCarrera"), carreraId));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return alumnoRepository.findAll(spec);
    }
}