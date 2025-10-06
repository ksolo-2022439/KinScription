package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.CoordinadorRepository;
import com.ksolorzano.KinScription.persistence.entity.Coordinador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Optional<Coordinador> getById(int id) {
        return coordinadorRepository.findById(id);
    }

    public Coordinador save(Coordinador coordinador) {
        // TODO: Hashear contraseña antes de guardar
        return coordinadorRepository.save(coordinador);
    }

    @Transactional
    public Optional<Coordinador> update(int id, Coordinador newData) {
        return coordinadorRepository.findById(id).map(coordinador -> {
            coordinador.setNombreCompleto(newData.getNombreCompleto());
            coordinador.setApellidoCompleto(newData.getApellidoCompleto());
            coordinador.setEmail(newData.getEmail());
            coordinador.setIdGrado(newData.getIdGrado());
            return coordinadorRepository.save(coordinador);
        });
    }

    @Transactional
    public boolean delete(int id) {
        return getById(id).map(coordinador -> {
            coordinadorRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}