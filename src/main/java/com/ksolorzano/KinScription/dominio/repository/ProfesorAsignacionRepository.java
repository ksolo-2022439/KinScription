package com.ksolorzano.KinScription.dominio.repository;

import com.ksolorzano.KinScription.persistence.entity.ProfesorAsignacion;
import com.ksolorzano.KinScription.persistence.entity.ProfesorAsignacionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesorAsignacionRepository extends JpaRepository<ProfesorAsignacion, ProfesorAsignacionId>, JpaSpecificationExecutor<ProfesorAsignacion> {
}