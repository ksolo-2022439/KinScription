package com.ksolorzano.KinScription.dominio.repository;

import com.ksolorzano.KinScription.persistence.entity.AdmDocumentoRequerido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmDocumentoRequeridoRepository extends JpaRepository<AdmDocumentoRequerido, Long> {
}