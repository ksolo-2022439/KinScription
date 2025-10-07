package com.ksolorzano.KinScription.config;

import com.ksolorzano.KinScription.dominio.repository.*;
import com.ksolorzano.KinScription.persistence.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AdmParticipanteRepository participanteRepository;
    private final AdministradorRepository administradorRepository;
    private final GradoRepository gradoRepository;
    private final CarreraRepository carreraRepository;
    private final AdmCicloAcademicoRepository cicloAcademicoRepository;
    private final AlumnoRepository alumnoRepository;
    private final TutorRepository tutorRepository;

    @Autowired
    public DataSeeder(AdmParticipanteRepository pRepo, AdministradorRepository aRepo,
                      GradoRepository gRepo, CarreraRepository cRepo,
                      AdmCicloAcademicoRepository cicloRepo,
                      AlumnoRepository aluRepo, TutorRepository tRepo) {
        this.participanteRepository = pRepo;
        this.administradorRepository = aRepo;
        this.gradoRepository = gRepo;
        this.carreraRepository = cRepo;
        this.cicloAcademicoRepository = cicloRepo;
        this.alumnoRepository = aluRepo;
        this.tutorRepository = tRepo;
    }


    @Override
    public void run(String... args) throws Exception {
        // Solo ejecutar si la base de datos está vacía para no crear duplicados
        if (participanteRepository.count() == 0 && administradorRepository.count() == 0) {
            System.out.println("Base de datos vacía. Sembrando datos de prueba...");

            // 1. Crear entidades de catálogo
            Grado grado = new Grado();
            grado.setNombreGrado("Bachillerato en Ciencias y Letras");
            Grado gradoGuardado = gradoRepository.save(grado);

            Carrera carrera = new Carrera();
            carrera.setNombreCarrera("Orientación en Computación");
            Carrera carreraGuardada = carreraRepository.save(carrera);

            AdmCicloAcademico ciclo = new AdmCicloAcademico();
            ciclo.setNombre("Admisión 2026");
            AdmCicloAcademico cicloGuardado = cicloAcademicoRepository.save(ciclo);

            // 2. Crear un Administrador de prueba
            Administrador admin = new Administrador();
            admin.setNombreCompleto("Admin Inscripción");
            admin.setEmail("admin@kinal.edu.gt");
            admin.setPassword("password123"); // Contraseña en texto plano
            admin.setRol(RolAdministrador.ADMIN_INSCRIPCION);
            administradorRepository.save(admin);

            // 3. Crear un Participante de prueba
            AdmParticipante participante = new AdmParticipante();
            participante.setUsername("aspirante01");
            participante.setPassword("password123"); // Contraseña en texto plano
            participante.setNombreCompleto("Juan Carlos");
            participante.setApellidos("Pérez Gómez");
            participante.setFechaNacimiento(LocalDate.of(2008, 5, 10));
            participante.setDireccion("Ciudad de Guatemala, Zona 7");
            participante.setEstado(EstadoParticipante.PENDIENTE_EXAMEN); // El estado inicial
            participante.setActivo(true);

            // Asignar las entidades de catálogo
            participante.setGradoAplica(gradoGuardado);
            participante.setCarreraAplica(carreraGuardada);
            participante.setCicloAcademico(cicloGuardado);

            participanteRepository.save(participante);

            System.out.println("=====================================================");
            System.out.println("DATOS DE PRUEBA CREADOS:");
            System.out.println("  -> Participante | Usuario: aspirante01 | Pass: password123");
            System.out.println("  -> Administrador| Usuario: admin@kinal.edu.gt | Pass: password123");
            System.out.println("=====================================================");
        }
    }
}