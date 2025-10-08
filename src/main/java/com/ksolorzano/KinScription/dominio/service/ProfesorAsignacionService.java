package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.ProfesorAsignacionRepository;
import com.ksolorzano.KinScription.persistence.entity.ProfesorAsignacion;
import com.ksolorzano.KinScription.persistence.entity.ProfesorAsignacionId;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfesorAsignacionService {

    private final ProfesorAsignacionRepository profesorAsignacionRepository;

    @Autowired
    public ProfesorAsignacionService(ProfesorAsignacionRepository profesorAsignacionRepository) {
        this.profesorAsignacionRepository = profesorAsignacionRepository;
    }

    public List<ProfesorAsignacion> getAll() {
        return profesorAsignacionRepository.findAll();
    }

    public Optional<ProfesorAsignacion> getById(ProfesorAsignacionId id) {
        return profesorAsignacionRepository.findById(id);
    }

    public ProfesorAsignacion save(ProfesorAsignacion asignacion) {
        return profesorAsignacionRepository.save(asignacion);
    }

    @Transactional
    public boolean delete(ProfesorAsignacionId id) {
        if (profesorAsignacionRepository.existsById(id)) {
            profesorAsignacionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long countTotal() {
        return profesorAsignacionRepository.count();
    }

    @Transactional(readOnly = true)
    public List<ProfesorAsignacion> buscarPorFiltros(Integer idProfesor, Integer idCurso, Integer idGrado, Integer idSeccion, Integer idJornada) {
        Specification<ProfesorAsignacion> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (idProfesor != null) predicates.add(criteriaBuilder.equal(root.get("idProfesor"), idProfesor));
            if (idCurso != null) predicates.add(criteriaBuilder.equal(root.get("idCurso"), idCurso));
            if (idGrado != null) predicates.add(criteriaBuilder.equal(root.get("idGrado"), idGrado));
            if (idSeccion != null) predicates.add(criteriaBuilder.equal(root.get("idSeccion"), idSeccion));
            if (idJornada != null) predicates.add(criteriaBuilder.equal(root.get("idJornada"), idJornada));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        List<ProfesorAsignacion> resultados = profesorAsignacionRepository.findAll(spec);
        resultados.forEach(pa -> {
            Hibernate.initialize(pa.getProfesor());
            Hibernate.initialize(pa.getCurso());
            Hibernate.initialize(pa.getGrado());
            Hibernate.initialize(pa.getSeccion());
            Hibernate.initialize(pa.getJornada());
        });
        return resultados;
    }
}