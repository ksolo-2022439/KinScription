package com.ksolorzano.KinScription.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "Notas")
@Data
public class Notas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idNota")
    private Integer idNota;

    @Column(name = "idAlumno", nullable = false)
    private Integer idAlumno;

    @Column(name = "idCurso", nullable = false)
    private Integer idCurso;

    @Column(precision = 5, scale = 2)
    private BigDecimal bimestre1;

    @Column(precision = 5, scale = 2)
    private BigDecimal bimestre2;

    @Column(precision = 5, scale = 2)
    private BigDecimal bimestre3;

    @Column(precision = 5, scale = 2)
    private BigDecimal bimestre4;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAlumno", insertable = false, updatable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCurso", insertable = false, updatable = false)
    private Curso curso;

    /**
     * Calcula el promedio de los bimestres que no son nulos.
     * @return El promedio como BigDecimal, o BigDecimal.ZERO si no hay notas.
     */
    @Transient
    public BigDecimal getPromedio() {
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;
        if (bimestre1 != null) { sum = sum.add(bimestre1); count++; }
        if (bimestre2 != null) { sum = sum.add(bimestre2); count++; }
        if (bimestre3 != null) { sum = sum.add(bimestre3); count++; }
        if (bimestre4 != null) { sum = sum.add(bimestre4); count++; }
        if (count == 0) {
            return BigDecimal.ZERO;
        }
        return sum.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP);
    }
}