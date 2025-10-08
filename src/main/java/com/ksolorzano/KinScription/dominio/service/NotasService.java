package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.NotasRepository;
import com.ksolorzano.KinScription.persistence.entity.Alumno;
import com.ksolorzano.KinScription.persistence.entity.Notas;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotasService {

    private final NotasRepository notasRepository;

    @Autowired
    public NotasService(NotasRepository notasRepository) {
        this.notasRepository = notasRepository;
    }

    public List<Notas> getAll() {
        return notasRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Notas> getById(int id) {
        Optional<Notas> notaOpt = notasRepository.findById(id);
        notaOpt.ifPresent(nota -> {
            Hibernate.initialize(nota.getAlumno());
            Hibernate.initialize(nota.getCurso());
        });
        return notaOpt;
    }

    public List<Notas> getByAlumno(Alumno alumno) {
        return notasRepository.findByAlumno(alumno);
    }

    public Notas save(Notas nota) {
        return notasRepository.save(nota);
    }

    @Transactional
    public Optional<Notas> update(int id, Notas newData) {
        return notasRepository.findById(id).map(nota -> {
            nota.setBimestre1(newData.getBimestre1());
            nota.setBimestre2(newData.getBimestre2());
            nota.setBimestre3(newData.getBimestre3());
            nota.setBimestre4(newData.getBimestre4());
            // No se actualiza el alumno ni el curso, ya que son la clave de la relación.
            return notasRepository.save(nota);
        });
    }

    @Transactional
    public boolean delete(int id) {
        if (notasRepository.existsById(id)) {
            notasRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long countTotal() {
        return notasRepository.count();
    }

    public List<Notas> buscarPorFiltros(Integer idAlumno, Integer idCurso, BigDecimal promedioFilter) {
        Specification<Notas> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (idAlumno != null) {
                predicates.add(criteriaBuilder.equal(root.get("idAlumno"), idAlumno));
            }
            if (idCurso != null) {
                predicates.add(criteriaBuilder.equal(root.get("idCurso"), idCurso));
            }
            if (promedioFilter != null) {
                Expression<BigDecimal> b1 = criteriaBuilder.coalesce(root.get("bimestre1"), BigDecimal.ZERO);
                Expression<BigDecimal> b2 = criteriaBuilder.coalesce(root.get("bimestre2"), BigDecimal.ZERO);
                Expression<BigDecimal> b3 = criteriaBuilder.coalesce(root.get("bimestre3"), BigDecimal.ZERO);
                Expression<BigDecimal> b4 = criteriaBuilder.coalesce(root.get("bimestre4"), BigDecimal.ZERO);

                Expression<BigDecimal> sum = criteriaBuilder.sum(criteriaBuilder.sum(b1, b2), criteriaBuilder.sum(b3, b4));
                Expression<BigDecimal> avg = criteriaBuilder.quot(sum, 4).as(BigDecimal.class);

                predicates.add(criteriaBuilder.greaterThanOrEqualTo(avg, promedioFilter));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return notasRepository.findAll(spec);
    }
}