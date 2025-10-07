package com.ksolorzano.KinScription.persistence.entity;

public enum EstadoParticipante {
    // Fase 1: Examen
    PENDIENTE_EXAMEN,       // El participante debe realizar el examen.
    EXAMEN_REALIZADO,       // El participante ya hizo el examen, espera calificación.

    // Fase 2: Socioeconómico
    ADMITIDO_EXAMEN,        // Aprobó el examen, debe llenar el formulario.
    SOCIOECONOMICO_ENVIADO, // Ya llenó el formulario, espera revisión del Director/Orientación.

    // Fase 3: Aprobación Administrativa (combinamos los estados de espera)
    ADMITIDO_SOCIOECONOMICO, // El Director aprobó, espera a Orientación.
    ADMITIDO_FORMULARIO,    // Orientación aprobó, debe subir papelería.

    // Fase 4: Papelería
    PAPELERIA_ENVIADA,      // Ya subió sus documentos, espera revisión de Secretaría.

    // Fase 5: Contrato
    ADMITIDO_PAPELERIA,     // Secretaría aprobó la papelería, debe subir el contrato.
    CONTRATO_ENVIADO,       // Ya subió el contrato, espera revisión final.

    // Estados Finales
    ADMITIDO_CONTRATO,      // Contrato aprobado, el sistema está creando el Alumno.
    FINALIZADO,             // Proceso completado, ya es Alumno.
    DESAPROBADO             // Proceso detenido en cualquier punto.
}