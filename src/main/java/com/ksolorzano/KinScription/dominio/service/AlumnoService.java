package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.AlumnoRepository;
import com.ksolorzano.KinScription.dominio.repository.specification.AlumnoSpecification;
import com.ksolorzano.KinScription.persistence.entity.Alumno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
        return alumnoRepository.save(alumno);
    }

    @Transactional
    public Optional<Alumno> update(int id, Alumno newData) {
        return alumnoRepository.findById(id).map(alumno -> {
            alumno.setNombreCompleto(newData.getNombreCompleto());
            alumno.setApellidoCompleto(newData.getApellidoCompleto());
            alumno.setDireccion(newData.getDireccion());
            alumno.setGrado(newData.getGrado());
            alumno.setSeccion(newData.getSeccion());
            alumno.setJornada(newData.getJornada());
            alumno.setCarrera(newData.getCarrera());
            alumno.setTutor(newData.getTutor());
            if (newData.getContrasena() != null && !newData.getContrasena().trim().isEmpty()) {
                alumno.setContrasena(newData.getContrasena());
            }
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

    public long countTotal() {
        return alumnoRepository.count();
    }

    public List<Alumno> buscarAlumnos(String carnet, String nombre, String email, Long gradoId, Long seccionId, Long carreraId) {
        Specification<Alumno> spec = AlumnoSpecification.getSpec(carnet, nombre, email, gradoId, seccionId, carreraId);
        return alumnoRepository.findAll(spec, Sort.unsorted());
    }
}