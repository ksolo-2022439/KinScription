package com.ksolorzano.KinScription.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "adm_participantes")
public class AdmParticipante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String nombreCompleto;

    @Column(nullable = false, length = 100)
    private String apellidos;

    private LocalDate fechaNacimiento;

    @Column(length = 255)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoParticipante estado;

    @Column(precision = 5, scale = 2)
    private Double notaExamen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ciclo_academico", nullable = false)
    private AdmCicloAcademico cicloAcademico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grado_aplica", nullable = false)
    private Grado gradoAplica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera_aplica") // nullable
    private Carrera carreraAplica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tutor_generado") // se llena al aprobar el socioeconómico
    private Tutor tutor;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;
}