package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.AdministradorRepository;
import com.ksolorzano.KinScription.persistence.entity.Administrador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;

    @Autowired
    public AdministradorService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    /**
     * Obtiene todos los administradores.
     * @return Lista de administradores.
     */
    public List<Administrador> getAll() {
        return administradorRepository.findAll();
    }

    /**
     * Busca un administrador por su correo electrónico.
     * @param email El email del administrador.
     * @return Un Optional con el administrador si se encuentra.
     */
    public Optional<Administrador> getByEmail(String email) {
        return administradorRepository.findByEmail(email);
    }

    public Optional<Administrador> getById(int id) {
        return administradorRepository.findById(id);
    }

    /**
     * Guarda un nuevo administrador.
     * @param administrador El objeto a guardar.
     * @return El administrador guardado.
     */
    public Administrador save(Administrador administrador) {
        // TODO: Hashear contraseña antes de guardar
        return administradorRepository.save(administrador);
    }

    @Transactional
    public Optional<Administrador> update(int id, Administrador newData) {
        return administradorRepository.findById(id).map(admin -> {
            admin.setNombreCompleto(newData.getNombreCompleto());
            admin.setEmail(newData.getEmail());
            admin.setRol(newData.getRol());

            if (newData.getPassword() != null && !newData.getPassword().trim().isEmpty()) {
                admin.setPassword(newData.getPassword());
            }

            return administradorRepository.save(admin);
        });
    }

    @Transactional
    public boolean delete(int id) {
        return getById(id).map(admin -> {
            administradorRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}