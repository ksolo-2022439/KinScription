package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.GradoCursoRepository;
import com.ksolorzano.KinScription.persistence.entity.GradoCurso;
import com.ksolorzano.KinScription.persistence.entity.GradoCursoId;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GradoCursoService {

    private final GradoCursoRepository gradoCursoRepository;

    @Autowired
    public GradoCursoService(GradoCursoRepository gradoCursoRepository) {
        this.gradoCursoRepository = gradoCursoRepository;
    }

    public List<GradoCurso> getAll() {
        return gradoCursoRepository.findAll();
    }

    public Optional<GradoCurso> getById(GradoCursoId id) {
        return gradoCursoRepository.findById(id);
    }

    public GradoCurso save(GradoCurso gradoCurso) {
        return gradoCursoRepository.save(gradoCurso);
    }

    // Para entidades con clave compuesta, 'update' no es común.
    // La práctica habitual es eliminar el registro antiguo y crear uno nuevo.

    @Transactional
    public boolean delete(GradoCursoId id) {
        if (gradoCursoRepository.existsById(id)) {
            gradoCursoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long countTotal() {
        return gradoCursoRepository.count();
    }

    /**
     * Busca asociaciones por filtro de grado y asegura la carga de las entidades relacionadas.
     * @param idGrado El ID del grado por el cual filtrar (opcional).
     * @return Una lista de asociaciones GradoCurso.
     */
    @Transactional(readOnly = true)
    public List<GradoCurso> buscarPorFiltro(Integer idGrado) {
        Specification<GradoCurso> spec = (root, query, criteriaBuilder) -> {
            if (idGrado != null) {
                return criteriaBuilder.equal(root.get("idGrado"), idGrado);
            }
            return criteriaBuilder.conjunction(); // Retorna un predicado 'true' si no hay filtro
        };
        List<GradoCurso> resultados = gradoCursoRepository.findAll(spec);
        // Forzar inicialización para evitar LazyInitializationException en la vista
        resultados.forEach(gc -> {
            Hibernate.initialize(gc.getGrado());
            Hibernate.initialize(gc.getCurso());
        });
        return resultados;
    }
}