package com.ksolorzano.KinScription.dominio.dto;

import com.ksolorzano.KinScription.persistence.entity.Curso;

public record CursoApiDto(Integer idCurso, String nombreCurso) {

    /**
     * Constructor de conveniencia para convertir una entidad Curso a este DTO.
     * @param curso La entidad a convertir.
     */
    public CursoApiDto(Curso curso) {
        this(curso.getIdCurso(), curso.getNombreCurso());
    }
}