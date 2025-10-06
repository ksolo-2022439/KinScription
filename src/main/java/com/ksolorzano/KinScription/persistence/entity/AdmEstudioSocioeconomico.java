package com.ksolorzano.KinScription.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "adm_estudios_socioeconomicos")
public class AdmEstudioSocioeconomico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_participante", nullable = false, unique = true)
    private AdmParticipante participante;

    // campos temporales para recolectar la info del tutor
    private String datosTutorNombre;
    private String datosTutorApellido;
    private String datosTutorTelefono;
    private String datosTutorDireccion;

    // campos cuando se tenga el cuestionario real

    private Boolean aprobadoDirector = false;
    private Boolean aprobadoOrientacion = false;
}