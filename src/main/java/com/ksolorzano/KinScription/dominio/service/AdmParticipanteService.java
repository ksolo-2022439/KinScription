package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.AdmParticipanteRepository;
import com.ksolorzano.KinScription.dominio.repository.AlumnoRepository;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.Alumno;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Servicio central que gestiona toda la lógica de negocio relacionada con los participantes.
 * Controla los cambios de estado, la calificación y el proceso final de conversión a Alumno.
 */
@Service
public class AdmParticipanteService {

    private final AdmParticipanteRepository participanteRepository;
    private final AlumnoService alumnoService;
    private final AlumnoRepository alumnoRepository;

    /**
     * Constructor para inyección de dependencias.
     *
     * @param repo       Repositorio para la entidad AdmParticipante.
     * @param aluService Servicio para la entidad Alumno.
     */
    @Autowired
    public AdmParticipanteService(AdmParticipanteRepository repo, AlumnoService aluService, AlumnoRepository alumRepo) {
        this.participanteRepository = repo;
        this.alumnoService = aluService;
        this.alumnoRepository = alumRepo;
    }

    /**
     * Obtiene una lista de todos los participantes registrados en el sistema.
     *
     * @return Una {@link List} de objetos {@link AdmParticipante}.
     */
    public List<AdmParticipante> getAll() {
        return participanteRepository.findAll();
    }

    /**
     * Busca un participante por su ID único.
     *
     * @param id El ID del participante.
     * @return Un {@link Optional} que contiene al participante si se encuentra.
     */
    public Optional<AdmParticipante> getById(int id) {
        return participanteRepository.findByIdActivo(id);
    }

    /**
     * Busca un participante por su nombre de usuario único utilizado para el login.
     *
     * @param username El nombre de usuario del participante.
     * @return Un {@link Optional} que contiene al participante si se encuentra.
     */
    public Optional<AdmParticipante> getByUsername(String username) {
        return participanteRepository.findByUsername(username);
    }

    /**
     * Guarda un nuevo participante o actualiza uno existente.
     *
     * @param participante El objeto AdmParticipante a persistir.
     * @return El participante guardado.
     */
    public AdmParticipante save(AdmParticipante participante) {
        return participanteRepository.save(participante);
    }

    /**
     * Recupera una lista de participantes que se encuentran en un estado específico.
     *
     * @param estado El {@link EstadoParticipante} por el cual filtrar.
     * @return Una {@link List} de participantes que coinciden con el estado.
     */
    public List<AdmParticipante> getByEstado(EstadoParticipante estado) {
        return participanteRepository.findByEstado(estado);
    }

    /**
     * Procesa la calificación del examen de admisión para un participante.
     * Cambia el estado del participante a ADMITIDO_EXAMEN si la nota es >= 60,
     * o a DESAPROBADO en caso contrario.
     *
     * @param participanteId El ID del participante a calificar.
     * @param nota           La nota obtenida en el examen.
     * @return El participante con su estado y nota actualizados.
     * @throws IllegalStateException Si el participante no se encuentra en el estado PENDIENTE_EXAMEN.
     */
    @Transactional
    public AdmParticipante calificarExamen(int participanteId, BigDecimal nota) {
        AdmParticipante participante = getById(participanteId)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado con ID: " + participanteId));

        if (participante.getEstado() != EstadoParticipante.EXAMEN_REALIZADO) {
            throw new IllegalStateException("El participante no se encuentra en el estado correcto para ser calificado.");
        }

        participante.setNotaExamen(nota);

        if (nota.compareTo(new BigDecimal("60")) >= 0) {
            participante.setEstado(EstadoParticipante.ADMITIDO_EXAMEN);
        } else {
            participante.setEstado(EstadoParticipante.DESAPROBADO);
        }

        return save(participante);
    }

    /**
     * Orquesta el paso final del proceso de admisión: convierte un participante admitido en un Alumno oficial.
     * Esta operación transaccional realiza lo siguiente:
     * 1. Genera un carnet, un correo institucional y una contraseña por defecto.
     * 2. Crea y guarda una nueva entidad {@link Alumno} con los datos del participante.
     * 3. Actualiza el estado del participante a {@link EstadoParticipante#FINALIZADO}.
     *
     * @param participanteId El ID del participante a finalizar.
     * @return El nuevo objeto {@link Alumno} que ha sido creado y guardado.
     * @throws IllegalStateException Si el participante no se encuentra en el estado ADMITIDO_CONTRATO.
     */
    @Transactional
    public Alumno finalizarProcesoYCrearAlumno(int participanteId) {
        AdmParticipante participante = getById(participanteId)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado con ID: " + participanteId));

        if (participante.getEstado() != EstadoParticipante.ADMITIDO_CONTRATO) {
            throw new IllegalStateException("El participante no ha sido admitido por contrato.");
        }

        String anioActual = String.valueOf(java.time.Year.now().getValue());
        String ultimoCarnet = alumnoRepository.findTopByCarnetAlumnoStartingWithOrderByCarnetAlumnoDesc(anioActual);

        int nuevoNumero = 1;
        if (ultimoCarnet != null) {
            int ultimoNumero = Integer.parseInt(ultimoCarnet.substring(4));
            nuevoNumero = ultimoNumero + 1;
        }

        String nuevoCarnet = anioActual + String.format("%03d", nuevoNumero);
        if (alumnoRepository.existsByCarnetAlumno(nuevoCarnet)) {
            throw new RuntimeException("Error de concurrencia: El carnet " + nuevoCarnet + " ya fue generado.");
        }

        String primeraLetraNombre = participante.getNombreCompleto().substring(0, 1).toLowerCase();
        String primerApellido = participante.getApellidos().split(" ")[0].toLowerCase();
        String emailAcademico = String.format("%s%s-%s@kinal.edu.gt",
                primeraLetraNombre,
                primerApellido,
                nuevoCarnet);

        if (alumnoRepository.existsByEmailAcademico(emailAcademico)) {
            throw new RuntimeException("Error: El correo electrónico " + emailAcademico + " ya existe.");
        }

        String contrasenaDefault = "password123";
        Alumno nuevoAlumno = new Alumno();
        nuevoAlumno.setCarnetAlumno(nuevoCarnet);
        nuevoAlumno.setEmailAcademico(emailAcademico);
        nuevoAlumno.setNombreCompleto(participante.getNombreCompleto());
        nuevoAlumno.setApellidoCompleto(participante.getApellidos());
        nuevoAlumno.setContrasena(contrasenaDefault);
        nuevoAlumno.setDireccion(participante.getDireccion());
        nuevoAlumno.setGrado(participante.getGradoAplica());
        nuevoAlumno.setCarrera(participante.getCarreraAplica());
        nuevoAlumno.setTutor(participante.getTutor());

        nuevoAlumno.setAdmParticipante(participante);
        Alumno alumnoCreado = alumnoService.save(nuevoAlumno);
        participante.setEstado(EstadoParticipante.FINALIZADO);
        save(participante);

        return alumnoCreado;
    }

    /**
     * Actualiza los datos personales de un participante existente.
     *
     * @param id      El ID del participante a modificar.
     * @param newData Un objeto AdmParticipante con la nueva información.
     * @return Un {@link Optional} con el participante actualizado.
     */
    @Transactional
    public Optional<AdmParticipante> update(int id, AdmParticipante newData) {
        return getById(id).map(participante -> {
            participante.setNombreCompleto(newData.getNombreCompleto());
            participante.setApellidos(newData.getApellidos());
            participante.setFechaNacimiento(newData.getFechaNacimiento());
            participante.setDireccion(newData.getDireccion());
            if (newData.getPassword() != null && !newData.getPassword().trim().isEmpty()) {
                participante.setPassword(newData.getPassword());
            }
            return save(participante);
        });
    }

    /**
     * Desactiva un participante en la base de datos.
     *
     * @param id El ID del participante a eliminar.
     */
    @Transactional
    public boolean desactivar(int id) {
        return participanteRepository.findById(id).map(participante -> {
            participante.setActivo(false);
            save(participante);
            return true;
        }).orElse(false);
    }
}