package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.CarreraRepository;
import com.ksolorzano.KinScription.persistence.entity.Carrera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarreraService {

    private final CarreraRepository carreraRepository;

    @Autowired
    public CarreraService(CarreraRepository carreraRepository) {
        this.carreraRepository = carreraRepository;
    }

    public List<Carrera> getAll() {
        return carreraRepository.findAll();
    }

    public Optional<Carrera> getById(int id) {
        return carreraRepository.findById(id);
    }

    public Carrera save(Carrera carrera) {
        return carreraRepository.save(carrera);
    }

    @Transactional
    public Optional<Carrera> update(int id, Carrera newData) {
        return carreraRepository.findById(id).map(carrera -> {
            carrera.setNombreCarrera(newData.getNombreCarrera());
            return carreraRepository.save(carrera);
        });
    }

    @Transactional
    public boolean delete(int id) {
        return getById(id).map(carrera -> {
            carreraRepository.deleteById(id);
            return true;
        }).orElse(false);
    }

    public long countTotal() {
        return carreraRepository.count();
    }
}