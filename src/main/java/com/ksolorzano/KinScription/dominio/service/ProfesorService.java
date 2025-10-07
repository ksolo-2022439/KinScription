package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.ProfesorRepository;
import com.ksolorzano.KinScription.persistence.entity.Profesor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // TODO: Hashear contraseña antes de guardar
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
            if (newData.getContrasena() != null && !newData.getContrasena().trim().isEmpty()) {
                profesor.setContrasena(newData.getContrasena());
            }
            return profesorRepository.save(profesor);
        });
    }

    @Transactional
    public boolean delete(int id) {
        return getById(id).map(profesor -> {
            profesorRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}