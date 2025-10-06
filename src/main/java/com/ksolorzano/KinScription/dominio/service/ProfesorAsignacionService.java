package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.ProfesorAsignacionRepository;
import com.ksolorzano.KinScription.persistence.entity.ProfesorAsignacion;
import com.ksolorzano.KinScription.persistence.entity.ProfesorAsignacionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}