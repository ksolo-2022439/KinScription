package com.ksolorzano.KinScription.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "adm_documentos_requeridos")
public class AdmDocumentoRequerido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_participante", nullable = false)
    private AdmParticipante participante;

    @Column(nullable = false, length = 100)
    private String nombreDocumento;

    @Column(nullable = false, length = 255)
    private String urlArchivo;

    @Column(length = 20)
    private String estadoRevision; // "PENDIENTE", "APROBADO"
}