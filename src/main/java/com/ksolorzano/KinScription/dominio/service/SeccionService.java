package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.SeccionRepository;
import com.ksolorzano.KinScription.persistence.entity.Seccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SeccionService {

    private final SeccionRepository seccionRepository;

    @Autowired
    public SeccionService(SeccionRepository seccionRepository) {
        this.seccionRepository = seccionRepository;
    }

    public List<Seccion> getAll() {
        return seccionRepository.findAll();
    }

    public Optional<Seccion> getById(int id) {
        return seccionRepository.findById(id);
    }

    public Seccion save(Seccion seccion) {
        return seccionRepository.save(seccion);
    }

    @Transactional
    public Optional<Seccion> update(int id, Seccion newData) {
        return seccionRepository.findById(id).map(seccion -> {
            seccion.setNombreSeccion(newData.getNombreSeccion());
            return seccionRepository.save(seccion);
        });
    }

    @Transactional
    public boolean delete(int id) {
        return getById(id).map(seccion -> {
            seccionRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}