package com.ksolorzano.KinScription.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Alumnos")
@Data
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alumno")
    private Long idAlumno;

    @Column(name = "carnet_alumno", nullable = false, unique = true, length = 20)
    private String carnetAlumno;

    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    @Column(name = "apellido_completo", nullable = false, length = 100)
    private String apellidoCompleto;

    @Column(name = "email_academico", nullable = false, unique = true)
    private String emailAcademico;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grado")
    private Grado grado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_seccion")
    private Seccion seccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jornada")
    private Jornada jornada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera")
    private Carrera carrera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tutor")
    private Tutor tutor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_participante_origen")
    private AdmParticipante admParticipante;

}