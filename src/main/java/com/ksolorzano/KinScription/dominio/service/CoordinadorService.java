package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.CoordinadorRepository;
import com.ksolorzano.KinScription.persistence.entity.Coordinador;
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
public class CoordinadorService {

    private final CoordinadorRepository coordinadorRepository;

    @Autowired
    public CoordinadorService(CoordinadorRepository coordinadorRepository) {
        this.coordinadorRepository = coordinadorRepository;
    }

    public List<Coordinador> getAll() {
        return coordinadorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Coordinador> getById(int id) {
        Optional<Coordinador> coordinadorOpt = coordinadorRepository.findById(id);
        coordinadorOpt.ifPresent(coordinador -> Hibernate.initialize(coordinador.getGrado()));
        return coordinadorOpt;
    }

    public Coordinador save(Coordinador coordinador) {
        return coordinadorRepository.save(coordinador);
    }

    @Transactional
    public Optional<Coordinador> update(int id, Coordinador newData) {
        return coordinadorRepository.findById(id).map(coordinador -> {
            coordinador.setNombreCompleto(newData.getNombreCompleto());
            coordinador.setApellidoCompleto(newData.getApellidoCompleto());
            coordinador.setEmail(newData.getEmail());
            coordinador.setIdGrado(newData.getIdGrado());
            if (StringUtils.hasText(newData.getContrasena())) {
                coordinador.setContrasena(newData.getContrasena());
            }
            return coordinadorRepository.save(coordinador);
        });
    }

    @Transactional
    public boolean delete(int id) {
        if (coordinadorRepository.existsById(id)) {
            coordinadorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long countTotal() {
        return coordinadorRepository.count();
    }

    public List<Coordinador> buscarPorFiltros(String nombre, String apellido, String email, Integer idGrado) {
        Specification<Coordinador> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(nombre)) {
                predicates.add(criteriaBuilder.like(root.get("nombreCompleto"), "%" + nombre + "%"));
            }
            if (StringUtils.hasText(apellido)) {
                predicates.add(criteriaBuilder.like(root.get("apellidoCompleto"), "%" + apellido + "%"));
            }
            if (StringUtils.hasText(email)) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + email + "%"));
            }
            if (idGrado != null) {
                predicates.add(criteriaBuilder.equal(root.get("idGrado"), idGrado));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return coordinadorRepository.findAll(spec);
    }
}