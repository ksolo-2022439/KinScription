package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.AdmEstudioSocioeconomicoRepository;
import com.ksolorzano.KinScription.persistence.entity.AdmEstudioSocioeconomico;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import com.ksolorzano.KinScription.persistence.entity.Tutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que encapsula la lógica de negocio para la gestión de los estudios socioeconómicos.
 * Maneja la creación, actualización, y el flujo de aprobación por parte de los roles administrativos.
 */
@Service
public class AdmEstudioSocioeconomicoService {

    private final AdmEstudioSocioeconomicoRepository estudioSocioeconomicoRepository;
    private final AdmParticipanteService participanteService;
    private final TutorService tutorService;
    private final AdmReportePagoService reportePagoService;

    /**
     * Constructor para la inyección de dependencias de los repositorios y otros servicios.
     * @param e Repositorio para AdmEstudioSocioeconomico.
     * @param p Servicio para AdmParticipante.
     * @param t Servicio para Tutor.
     * @param r Servicio para AdmReportePago.
     */
    @Autowired
    public AdmEstudioSocioeconomicoService(AdmEstudioSocioeconomicoRepository e, AdmParticipanteService p, TutorService t, AdmReportePagoService r) {
        this.estudioSocioeconomicoRepository = e;
        this.participanteService = p;
        this.tutorService = t;
        this.reportePagoService = r;
    }

    /**
     * Guarda un nuevo estudio socioeconómico o actualiza uno existente en la base de datos.
     * @param estudio El objeto AdmEstudioSocioeconomico a persistir.
     * @return El estudio guardado, incluyendo su ID generado si es una nueva entidad.
     */
    public AdmEstudioSocioeconomico save(AdmEstudioSocioeconomico estudio) {
        return estudioSocioeconomicoRepository.save(estudio);
    }

    /**
     * Busca un estudio socioeconómico específico asociado a un participante.
     * @param participante El participante cuyo estudio se desea encontrar.
     * @return Un Optional que contiene el estudio si se encuentra, o un Optional vacío si no.
     */
    public Optional<AdmEstudioSocioeconomico> getByParticipante(AdmParticipante participante) {
        return estudioSocioeconomicoRepository.findByParticipante(participante);
    }

    /**
     * Busca un estudio socioeconómico por su ID único.
     * @param id El ID del estudio a buscar.
     * @return Un Optional que contiene el estudio si se encuentra.
     */
    public Optional<AdmEstudioSocioeconomico> getById(int id) {
        return estudioSocioeconomicoRepository.findById(id);
    }

    /**
     * Procesa la aprobación del estudio por parte del Director Administrativo.
     * Guarda los montos de pago asignados y actualiza el estado del participante.
     * @param estudioId El ID del estudio socioeconómico.
     * @param montoInscripcion El monto de inscripción asignado por el director.
     * @param montoMensualidad El monto de mensualidad asignado por el director.
     * @throws IllegalStateException Si el participante no está en el estado correcto.
     */
    @Transactional
    public void aprobarPorDirector(int estudioId, BigDecimal montoInscripcion, BigDecimal montoMensualidad) {
        AdmEstudioSocioeconomico estudio = getById(estudioId)
                .orElseThrow(() -> new RuntimeException("Estudio no encontrado con ID: " + estudioId));

        AdmParticipante participante = estudio.getParticipante();
        if (participante.getEstado() != EstadoParticipante.SOCIOECONOMICO_ENVIADO) {
            throw new IllegalStateException("El participante no está en el estado correcto para esta acción.");
        }

        estudio.setMontoInscripcion(montoInscripcion);
        estudio.setMontoMensualidad(montoMensualidad);

        estudio.setAprobadoDirector(true);
        save(estudio);

        participante.setEstado(EstadoParticipante.ADMITIDO_SOCIOECONOMICO);
        participanteService.save(participante);
    }

    /**
     * Orquesta la aprobación final del formulario por parte de Orientación.
     * Este es un paso transaccional crítico que realiza varias acciones:
     * 1. Crea y guarda un nuevo Tutor con los datos del formulario.
     * 2. Asocia el Tutor recién creado al Participante.
     * 3. Cambia el estado del Participante a ADMITIDO_FORMULARIO.
     * 4. Dispara la generación del reporte de pagos para contabilidad.
     * @param estudioId El ID del estudio socioeconómico a aprobar.
     * @throws IllegalStateException Si el participante no ha sido aprobado previamente por el director.
     */
    @Transactional
    public void aprobarPorOrientacion(int estudioId) {
        AdmEstudioSocioeconomico estudio = estudioSocioeconomicoRepository.findById(estudioId)
                .orElseThrow(() -> new RuntimeException("Estudio no encontrado con ID: " + estudioId));

        AdmParticipante participante = estudio.getParticipante();
        if (participante.getEstado() != EstadoParticipante.ADMITIDO_SOCIOECONOMICO) {
            throw new IllegalStateException("El participante debe ser aprobado primero por el director.");
        }

        Tutor nuevoTutor = new Tutor();
        nuevoTutor.setNombreCompleto(estudio.getDatosTutorNombre());
        nuevoTutor.setApellidoCompleto(estudio.getDatosTutorApellido());
        nuevoTutor.setNumeroTelefono(estudio.getDatosTutorTelefono());
        nuevoTutor.setDireccion(estudio.getDatosTutorDireccion());
        Tutor tutorGuardado = tutorService.save(nuevoTutor);

        participante.setTutor(tutorGuardado);

        estudio.setAprobadoOrientacion(true);
        participante.setEstado(EstadoParticipante.ADMITIDO_FORMULARIO);

        estudioSocioeconomicoRepository.save(estudio);
        participanteService.save(participante);

        BigDecimal montoInscripcion = estudio.getMontoInscripcion();
        BigDecimal montoMensualidad = estudio.getMontoMensualidad();
        // Validamos que los montos no sean nulos antes de generar los pagos
        if (montoInscripcion == null || montoMensualidad == null) {
            throw new IllegalStateException("Los montos de inscripción y mensualidad no han sido definidos para este estudio.");
        }
        reportePagoService.generarReporteDePagos(participante, montoInscripcion, montoMensualidad);
    }

    /**
     * Actualiza los datos de un estudio socioeconómico existente.
     * @param id El ID del estudio a actualizar.
     * @param newData Un objeto AdmEstudioSocioeconomico con la nueva información a aplicar.
     * @return Un Optional con el estudio actualizado si se encontró, o vacío si no.
     */
    @Transactional
    public Optional<AdmEstudioSocioeconomico> update(int id, AdmEstudioSocioeconomico newData) {
        return estudioSocioeconomicoRepository.findById(id).map(estudio -> {
            estudio.setDatosTutorNombre(newData.getDatosTutorNombre());
            estudio.setDatosTutorApellido(newData.getDatosTutorApellido());
            estudio.setDatosTutorTelefono(newData.getDatosTutorTelefono());
            estudio.setDatosTutorDireccion(newData.getDatosTutorDireccion());
            estudio.setMontoInscripcion(newData.getMontoInscripcion());
            estudio.setMontoMensualidad(newData.getMontoMensualidad());
            return estudioSocioeconomicoRepository.save(estudio);
        });
    }

    /**
     * Elimina un estudio socioeconómico de la base de datos por su ID.
     * @param id El ID del estudio a eliminar.
     * @return {@code true} si el estudio fue encontrado y eliminado, {@code false} en caso contrario.
     */
    @Transactional
    public boolean delete(int id) {
        return getById(id).map(estudio -> {
            estudioSocioeconomicoRepository.deleteById(id);
            return true;
        }).orElse(false);
    }

    /**
     * Obtiene una lista de todos los estudios socioeconómicos registrados.
     * @return Una lista de todas las entidades AdmEstudioSocioeconomico.
     */
    public List<AdmEstudioSocioeconomico> getAll() {
        return estudioSocioeconomicoRepository.findAll();
    }
}