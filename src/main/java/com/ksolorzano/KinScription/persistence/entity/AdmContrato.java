package com.ksolorzano.KinScription.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "adm_contratos")
public class AdmContrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_participante", nullable = false, unique = true)
    private AdmParticipante participante;

    @Column(length = 100)
    private String nombreAbogado;

    @Column(length = 20)
    private String colegiadoAbogado;

    @Column(length = 255)
    private String urlPdfContrato;

    private Boolean aprobado = false;
}