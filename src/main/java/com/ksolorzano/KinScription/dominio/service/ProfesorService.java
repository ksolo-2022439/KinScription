package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.ProfesorRepository;
import com.ksolorzano.KinScription.persistence.entity.Profesor;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfesorService {

    private final ProfesorRepository profesorRepository;

    @Autowired
    public ProfesorService(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    public List<Profesor> getAll() {
        return profesorRepository.findAll();
    }

    public Optional<Profesor> getById(int id) {
        return profesorRepository.findById(id);
    }

    public Profesor save(Profesor profesor) {
        return profesorRepository.save(profesor);
    }

    @Transactional
    public Optional<Profesor> update(int id, Profesor newData) {
        return profesorRepository.findById(id).map(profesor -> {
            profesor.setNombreCompleto(newData.getNombreCompleto());
            profesor.setApellidoCompleto(newData.getApellidoCompleto());
            profesor.setDireccion(newData.getDireccion());
            profesor.setNumeroTelefono(newData.getNumeroTelefono());
            profesor.setEmail(newData.getEmail());
            if (StringUtils.hasText(newData.getContrasena())) {
                profesor.setContrasena(newData.getContrasena());
            }
            return profesorRepository.save(profesor);
        });
    }

    @Transactional
    public boolean delete(int id) {
        if (profesorRepository.existsById(id)) {
            profesorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long countTotal() {
        return profesorRepository.count();
    }

    /**
     * Busca profesores aplicando filtros dinámicos.
     * @param nombreFiltro Filtro para el nombre.
     * @param apellidoFiltro Filtro para el apellido.
     * @param telefonoFiltro Filtro para el teléfono.
     * @param emailFiltro Filtro para el email.
     * @return Una lista de profesores que coinciden con los criterios.
     */
    public List<Profesor> buscarPorFiltros(String nombreFiltro, String apellidoFiltro, String telefonoFiltro, String emailFiltro) {
        Specification<Profesor> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(nombreFiltro)) {
                predicates.add(criteriaBuilder.like(root.get("nombreCompleto"), "%" + nombreFiltro + "%"));
            }
            if (StringUtils.hasText(apellidoFiltro)) {
                predicates.add(criteriaBuilder.like(root.get("apellidoCompleto"), "%" + apellidoFiltro + "%"));
            }
            if (StringUtils.hasText(telefonoFiltro)) {
                predicates.add(criteriaBuilder.like(root.get("numeroTelefono"), "%" + telefonoFiltro + "%"));
            }
            if (StringUtils.hasText(emailFiltro)) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + emailFiltro + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return profesorRepository.findAll(spec);
    }
}