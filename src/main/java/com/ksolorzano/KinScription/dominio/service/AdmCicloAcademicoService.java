package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.AdmCicloAcademicoRepository;
import com.ksolorzano.KinScription.persistence.entity.AdmCicloAcademico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdmCicloAcademicoService {

    private final AdmCicloAcademicoRepository cicloAcademicoRepository;

    @Autowired
    public AdmCicloAcademicoService(AdmCicloAcademicoRepository cicloAcademicoRepository) {
        this.cicloAcademicoRepository = cicloAcademicoRepository;
    }

    public List<AdmCicloAcademico> getAll() {
        return cicloAcademicoRepository.findAll();
    }

    public Optional<AdmCicloAcademico> getById(int id) {
        return cicloAcademicoRepository.findById(id);
    }

    public AdmCicloAcademico save(AdmCicloAcademico cicloAcademico) {
        return cicloAcademicoRepository.save(cicloAcademico);
    }

    @Transactional
    public Optional<AdmCicloAcademico> update(int id, AdmCicloAcademico newData) {
        return cicloAcademicoRepository.findById(id).map(ciclo -> {
            ciclo.setNombre(newData.getNombre());
            ciclo.setFechaInicio(newData.getFechaInicio());
            ciclo.setFechaFin(newData.getFechaFin());
            return cicloAcademicoRepository.save(ciclo);
        });
    }

    @Transactional
    public boolean delete(int id) {
        return getById(id).map(ciclo -> {
            cicloAcademicoRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}