package com.ksolorzano.KinScription.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradoCursoId implements Serializable {
    private Integer idGrado;
    private Integer idCurso;
}