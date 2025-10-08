package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.CursoRepository;
import com.ksolorzano.KinScription.persistence.entity.Curso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    @Autowired
    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<Curso> getAll() {
        return cursoRepository.findAll();
    }

    public Optional<Curso> getById(int id) {
        return cursoRepository.findById(id);
    }

    public Curso save(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Transactional
    public Optional<Curso> update(int id, Curso newData) {
        return cursoRepository.findById(id).map(curso -> {
            curso.setNombreCurso(newData.getNombreCurso());
            return cursoRepository.save(curso);
        });
    }

    @Transactional
    public boolean delete(int id) {
        return getById(id).map(curso -> {
            cursoRepository.deleteById(id);
            return true;
        }).orElse(false);
    }

    public long countTotal() {
        return cursoRepository.count();
    }
}