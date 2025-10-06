package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.NotasRepository;
import com.ksolorzano.KinScription.persistence.entity.Alumno;
import com.ksolorzano.KinScription.persistence.entity.Notas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotasService {

    private final NotasRepository notasRepository;

    @Autowired
    public NotasService(NotasRepository notasRepository) {
        this.notasRepository = notasRepository;
    }

    public Optional<Notas> getById(int id) {
        return notasRepository.findById(id);
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
            // No se debería cambiar el alumno o el curso de una nota existente
            return notasRepository.save(nota);
        });
    }

    @Transactional
    public boolean delete(int id) {
        return getById(id).map(nota -> {
            notasRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}