package com.ksolorzano.KinScription.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Jornadas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idJornada")
    private Long idJornada;

    @Column(name = "nombreJornada", nullable = false, unique = true)
    private String nombreJornada;
}
