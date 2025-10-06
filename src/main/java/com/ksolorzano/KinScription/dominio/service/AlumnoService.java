package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.AlumnoRepository;
import com.ksolorzano.KinScription.persistence.entity.Alumno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;

    @Autowired
    public AlumnoService(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    public List<Alumno> getAll() {
        return alumnoRepository.findAll();
    }

    public Optional<Alumno> getById(int id) {
        return alumnoRepository.findById(id);
    }

    public Alumno save(Alumno alumno) {
        // TODO: Lógica para generar carnet, email, etc.
        return alumnoRepository.save(alumno);
    }

    @Transactional
    public Optional<Alumno> update(int id, Alumno newData) {
        return alumnoRepository.findById(id).map(alumno -> {
            alumno.setNombreCompleto(newData.getNombreCompleto());
            alumno.setApellidoCompleto(newData.getApellidoCompleto());
            alumno.setDireccion(newData.getDireccion());
            alumno.setIdGrado(newData.getIdGrado());
            alumno.setIdSeccion(newData.getIdSeccion());
            alumno.setIdJornada(newData.getIdJornada());
            alumno.setIdCarrera(newData.getIdCarrera());
            alumno.setIdTutor(newData.getIdTutor());
            return alumnoRepository.save(alumno);
        });
    }

    @Transactional
    public boolean delete(int id) {
        return getById(id).map(alumno -> {
            // Lógica de borrado en cascada o validaciones (ej. si tiene notas)
            alumnoRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}