package com.ksolorzano.KinScription.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "adm_estudios_socioeconomicos")
public class AdmEstudioSocioeconomico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_participante", nullable = false, unique = true)
    private AdmParticipante participante;

    @NotEmpty(message = "El nombre del tutor no puede estar vacío.")
    @Column(name = "datos_tutor_nombre", nullable = false, length = 100)
    private String datosTutorNombre;

    @NotEmpty(message = "El apellido del tutor no puede estar vacío.")
    @Column(name = "datos_tutor_apellido", nullable = false, length = 100)
    private String datosTutorApellido;

    @NotEmpty(message = "El teléfono del tutor no puede estar vacío.")
    @Column(name = "datos_tutor_telefono", nullable = false, length = 15)
    private String datosTutorTelefono;

    @NotEmpty(message = "La dirección del tutor no puede estar vacía.")
    @Column(name = "datos_tutor_direccion", nullable = false, length = 255)
    private String datosTutorDireccion;

    // Aquí irían todos los demás campos del formulario con sus anotaciones ...

    @PositiveOrZero(message = "El monto no puede ser negativo.")
    @Digits(integer = 8, fraction = 2, message = "Formato de monto inválido.")
    @Column(name = "monto_inscripcion", precision = 10, scale = 2)
    private BigDecimal montoInscripcion;

    @PositiveOrZero(message = "El monto no puede ser negativo.")
    @Digits(integer = 8, fraction = 2, message = "Formato de monto inválido.")
    @Column(name = "monto_mensualidad", precision = 10, scale = 2)
    private BigDecimal montoMensualidad;

    @Column(name = "aprobado_director", nullable = false)
    private Boolean aprobadoDirector = false;

    @Column(name = "aprobado_orientacion", nullable = false)
    private Boolean aprobadoOrientacion = false;
}