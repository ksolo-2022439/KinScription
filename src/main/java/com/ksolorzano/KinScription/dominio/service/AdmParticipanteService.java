package com.ksolorzano.KinScription.dominio.service;

import com.ksolorzano.KinScription.dominio.repository.AdmParticipanteRepository;
import com.ksolorzano.KinScription.persistence.entity.AdmParticipante;
import com.ksolorzano.KinScription.persistence.entity.Alumno;
import com.ksolorzano.KinScription.persistence.entity.EstadoParticipante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Constructor para inyección de dependencias.
     * @param repo Repositorio para la entidad AdmParticipante.
     * @param aluService Servicio para la entidad Alumno.
     */
    @Autowired
    public AdmParticipanteService(AdmParticipanteRepository repo, AlumnoService aluService) {
        this.participanteRepository = repo;
        this.alumnoService = aluService;
    }

    /**
     * Obtiene una lista de todos los participantes registrados en el sistema.
     * @return Una {@link List} de objetos {@link AdmParticipante}.
     */
    public List<AdmParticipante> getAll() {
        return participanteRepository.findAll();
    }

    /**
     * Busca un participante por su ID único.
     * @param id El ID del participante.
     * @return Un {@link Optional} que contiene al participante si se encuentra.
     */
    public Optional<AdmParticipante> getById(int id) {
        return participanteRepository.findById(id);
    }

    /**
     * Busca un participante por su nombre de usuario único utilizado para el login.
     * @param username El nombre de usuario del participante.
     * @return Un {@link Optional} que contiene al participante si se encuentra.
     */
    public Optional<AdmParticipante> getByUsername(String username) {
        return participanteRepository.findByUsername(username);
    }

    /**
     * Guarda un nuevo participante o actualiza uno existente.
     * @param participante El objeto AdmParticipante a persistir.
     * @return El participante guardado.
     */
    public AdmParticipante save(AdmParticipante participante) {
        return participanteRepository.save(participante);
    }

    /**
     * Recupera una lista de participantes que se encuentran en un estado específico.
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
     * @param participanteId El ID del participante a calificar.
     * @param nota La nota obtenida en el examen.
     * @return El participante con su estado y nota actualizados.
     * @throws IllegalStateException Si el participante no se encuentra en el estado PENDIENTE_EXAMEN.
     */
    @Transactional
    public AdmParticipante calificarExamen(int participanteId, double nota) {
        AdmParticipante participante = getById(participanteId)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado con ID: " + participanteId));

        if (participante.getEstado() != EstadoParticipante.PENDIENTE_EXAMEN) {
            throw new IllegalStateException("El participante no está en estado de pendiente de examen.");
        }

        participante.setNotaExamen(nota);
        if (nota >= 60) {
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
     * @param participanteId El ID del participante a finalizar.
     * @return El nuevo objeto {@link Alumno} que ha sido creado y guardado.
     * @throws IllegalStateException Si el participante no se encuentra en el estado ADMITIDO_CONTRATO.
     */
    @Transactional
    public Alumno finalizarProcesoYCrearAlumno(int participanteId) {
        AdmParticipante participante = getById(participanteId)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado con ID: " + participanteId));

        if (participante.getEstado() != EstadoParticipante.ADMITIDO_CONTRATO) {
            throw new IllegalStateException("El participante no ha sido admitido por contrato para poder finalizar el proceso.");
        }

        String baseEmail = participante.getNombreCompleto().split(" ")[0].toLowerCase() + "." + participante.getApellidos().split(" ")[0].toLowerCase();
        String emailAcademico = baseEmail + "@kinal.edu.gt";
        String contrasenaDefault = "password123";
        String carnet = "K" + java.time.Year.now().getValue() + "-" + participante.getId();

        Alumno nuevoAlumno = new Alumno();
        nuevoAlumno.setCarnetAlumno(carnet);
        nuevoAlumno.setNombreCompleto(participante.getNombreCompleto());
        nuevoAlumno.setApellidoCompleto(participante.getApellidos());
        nuevoAlumno.setEmailAcademico(emailAcademico);
        nuevoAlumno.setContrasena(contrasenaDefault);
        nuevoAlumno.setDireccion(participante.getDireccion());
        nuevoAlumno.setIdGrado(participante.getGradoAplica().getIdGrado());
        nuevoAlumno.setIdCarrera(participante.getCarreraAplica() != null ? participante.getCarreraAplica().getIdCarrera() : null);
        nuevoAlumno.setIdTutor(participante.getTutor().getIdTutor());

        Alumno alumnoCreado = alumnoService.save(nuevoAlumno);

        participante.setEstado(EstadoParticipante.FINALIZADO);
        save(participante);

        return alumnoCreado;
    }

    /**
     * Actualiza los datos personales de un participante existente.
     * @param id El ID del participante a modificar.
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
            return save(participante);
        });
    }

    /**
     * Elimina un participante de la base de datos.
     * @param id El ID del participante a eliminar.
     * @return {@code true} si se eliminó con éxito, {@code false} si no se encontró.
     */
    @Transactional
    public boolean delete(int id) {
        return getById(id).map(participante -> {
            participanteRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
}