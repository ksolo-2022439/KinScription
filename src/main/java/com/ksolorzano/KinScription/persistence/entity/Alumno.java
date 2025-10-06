package com.ksolorzano.KinScription.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Alumnos")
@Data
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAlumno")
    private Long idAlumno;

    @Column(name = "carnetAlumno", nullable = false, unique = true, length = 20)
    private String carnetAlumno;

    @Column(name = "nombreCompleto", nullable = false, length = 100)
    private String nombreCompleto;

    @Column(name = "apellidoCompleto", nullable = false, length = 100)
    private String apellidoCompleto;

    @Column(name = "emailAcademico", nullable = false)
    private String emailAcademico;

    @Column(nullable = false)
    private String contrasena;

    @Column(nullable = false)
    private String direccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idGrado", insertable = false, updatable = false)
    private Grado grado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSeccion", insertable = false, updatable = false)
    private Seccion seccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idJornada", insertable = false, updatable = false)
    private Jornada jornada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCarrera", insertable = false, updatable = false)
    private Carrera carrera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTutor", insertable = false, updatable = false)
    private Tutor tutor;

    @Column(name = "idGrado")
    private Long idGrado;

    @Column(name = "idSeccion")
    private Long idSeccion;

    @Column(name = "idJornada")
    private Long idJornada;

    @Column(name = "idCarrera")
    private Long idCarrera;

    @Column(name = "idTutor")
    private Long idTutor;
}