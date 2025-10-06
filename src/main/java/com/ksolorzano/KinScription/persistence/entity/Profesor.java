package com.ksolorzano.KinScription.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Entity
@Table(name = "Profesores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProfesor")
    private Long idProfesor;

    @Column(name = "nombreCompleto", nullable = false)
    private String nombreCompleto;

    @Column(name = "apellidoCompleto", nullable = false)
    private String apellidoCompleto;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Column(name = "numeroTelefono", nullable = false, length = 15)
    private String numeroTelefono;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;
}