package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.TutorRepository;
import com.ksolorzano.KinScription.persistence.entity.Tutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TutorService {

    private final TutorRepository tutorRepository;

    @Autowired
    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public List<Tutor> getAll() {
        return tutorRepository.findAll();
    }

    public Optional<Tutor> getById(int id) {
        return tutorRepository.findById(id);
    }

    public Tutor save(Tutor tutor) {
        return tutorRepository.save(tutor);
    }

    @Transactional
    public Optional<Tutor> update(int id, Tutor newData) {
        return tutorRepository.findById(id).map(tutor -> {
            tutor.setNombreCompleto(newData.getNombreCompleto());
            tutor.setApellidoCompleto(newData.getApellidoCompleto());
            tutor.setNumeroTelefono(newData.getNumeroTelefono());
            tutor.setDireccion(newData.getDireccion());
            return tutorRepository.save(tutor);
        });
    }

    @Transactional
    public boolean delete(int id) {
        return getById(id).map(tutor -> {
            tutorRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}