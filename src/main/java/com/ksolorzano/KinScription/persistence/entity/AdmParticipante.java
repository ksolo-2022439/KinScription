package com.ksolorzano.KinScription.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "adm_participantes")
public class AdmParticipante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotEmpty(message = "El nombre de usuario no puede estar vacío.")
    @Size(min = 5, max = 50, message = "El nombre de usuario debe tener entre 5 y 50 caracteres.")
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @NotEmpty(message = "La contraseña no puede estar vacía.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @NotEmpty(message = "El nombre completo no puede estar vacío.")
    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    @NotEmpty(message = "Los apellidos no pueden estar vacíos.")
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @Past(message = "La fecha de nacimiento debe ser una fecha en el pasado.")
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @NotEmpty(message = "La dirección no puede estar vacía.")
    @Column(name = "direccion", nullable = false, length = 255)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoParticipante estado;

    @Column(name = "nota_examen", precision = 5, scale = 2)
    private Double notaExamen;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ciclo_academico", nullable = false)
    private AdmCicloAcademico cicloAcademico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grado_aplica", nullable = false)
    private Grado gradoAplica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera_aplica")
    private Carrera carreraAplica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tutor_generado")
    private Tutor tutor;
}