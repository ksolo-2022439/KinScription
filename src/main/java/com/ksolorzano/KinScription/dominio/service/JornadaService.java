package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.JornadaRepository;
import com.ksolorzano.KinScription.persistence.entity.Jornada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JornadaService {

    private final JornadaRepository jornadaRepository;

    @Autowired
    public JornadaService(JornadaRepository jornadaRepository) {
        this.jornadaRepository = jornadaRepository;
    }

    public List<Jornada> getAll() {
        return jornadaRepository.findAll();
    }

    public Optional<Jornada> getById(int id) {
        return jornadaRepository.findById(id);
    }

    public Jornada save(Jornada jornada) {
        return jornadaRepository.save(jornada);
    }

    @Transactional
    public Optional<Jornada> update(int id, Jornada newData) {
        return jornadaRepository.findById(id).map(jornada -> {
            jornada.setNombreJornada(newData.getNombreJornada());
            return jornadaRepository.save(jornada);
        });
    }

    @Transactional
    public boolean delete(int id) {
        return getById(id).map(jornada -> {
            jornadaRepository.deleteById(id);
            return true;
        }).orElse(false);
    }

    public long countTotal() {
        return jornadaRepository.count();
    }
}