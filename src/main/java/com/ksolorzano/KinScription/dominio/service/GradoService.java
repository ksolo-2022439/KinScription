package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.GradoRepository;
import com.ksolorzano.KinScription.persistence.entity.Grado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GradoService {

    private final GradoRepository gradoRepository;

    @Autowired
    public GradoService(GradoRepository gradoRepository) {
        this.gradoRepository = gradoRepository;
    }

    public List<Grado> getAll() {
        return gradoRepository.findAll();
    }

    public Optional<Grado> getById(int id) {
        return gradoRepository.findById(id);
    }

    public Grado save(Grado grado) {
        return gradoRepository.save(grado);
    }

    @Transactional
    public Optional<Grado> update(int id, Grado newData) {
        return gradoRepository.findById(id).map(grado -> {
            grado.setNombreGrado(newData.getNombreGrado());
            return gradoRepository.save(grado);
        });
    }

    @Transactional
    public boolean delete(int id) {
        return getById(id).map(grado -> {
            gradoRepository.deleteById(id);
            return true;
        }).orElse(false);
    }

    public long countTotal() {
        return gradoRepository.count();
    }
}