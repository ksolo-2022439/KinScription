package com.ksolorzano.KinScription.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

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

    @Column(name = "monto_inscripcion", precision = 10, scale = 2)
    private BigDecimal montoInscripcion;

    @Column(name = "monto_mensualidad", precision = 10, scale = 2)
    private BigDecimal montoMensualidad;

    private Boolean aprobadoDirector = false;
    private Boolean aprobadoOrientacion = false;

    // campos temporales para recolectar la info del tutor
    private String datosTutorNombre;
    private String datosTutorApellido;
    private String datosTutorTelefono;
    private String datosTutorDireccion;

    // campos cuando se tenga el cuestionario real
}