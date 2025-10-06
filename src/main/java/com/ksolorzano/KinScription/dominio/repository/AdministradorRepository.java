package com.ksolorzano.KinScription.dominio.repository;

import com.ksolorzano.KinScription.persistence.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {

    /**
     * Busca un administrador por su dirección de correo electrónico.
     * @param email El email utilizado para el login.
     * @return Un Optional que contiene al Administrador si se encuentra.
     */
    Optional<Administrador> findByEmail(String email);
}