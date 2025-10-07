package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.TutorRepository;
import com.ksolorzano.KinScription.persistence.entity.Tutor;
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
public class TutorService {

    private final TutorRepository tutorRepository;

    @Autowired
    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public List<Tutor> getAll() {
        return tutorRepository.findAll();
    }

    public Optional<Tutor> getById(int id) {
        return tutorRepository.findById(id);
    }

    public Tutor save(Tutor tutor) {
        return tutorRepository.save(tutor);
    }

    @Transactional
    public Optional<Tutor> update(int id, Tutor newData) {
        return tutorRepository.findById(id).map(tutor -> {
            tutor.setNombreCompleto(newData.getNombreCompleto());
            tutor.setApellidoCompleto(newData.getApellidoCompleto());
            tutor.setNumeroTelefono(newData.getNumeroTelefono());
            tutor.setDireccion(newData.getDireccion());
            return tutorRepository.save(tutor);
        });
    }

    @Transactional
    public boolean delete(int id) {
        if (tutorRepository.existsById(id)) {
            tutorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long countTotal() {
        return tutorRepository.count();
    }

    /**
     * Busca tutores aplicando filtros dinámicos por nombre y número de teléfono.
     * @param nombreFiltro Filtro para el nombre completo (búsqueda parcial).
     * @param telefonoFiltro Filtro para el número de teléfono (búsqueda parcial).
     * @return Una lista de tutores que coinciden con los criterios.
     */
    public List<Tutor> buscarPorFiltros(String nombreFiltro, String telefonoFiltro) {
        Specification<Tutor> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(nombreFiltro)) {
                Predicate nombrePredicate = criteriaBuilder.like(root.get("nombreCompleto"), "%" + nombreFiltro + "%");
                Predicate apellidoPredicate = criteriaBuilder.like(root.get("apellidoCompleto"), "%" + nombreFiltro + "%");
                predicates.add(criteriaBuilder.or(nombrePredicate, apellidoPredicate));
            }
            if (StringUtils.hasText(telefonoFiltro)) {
                predicates.add(criteriaBuilder.like(root.get("numeroTelefono"), "%" + telefonoFiltro + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return tutorRepository.findAll(spec);
    }
}