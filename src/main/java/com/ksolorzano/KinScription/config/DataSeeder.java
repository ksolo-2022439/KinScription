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

            Administrador admin2 = new Administrador();
            admin2.setNombreCompleto("Director Administrativo");
            admin2.setEmail("admin2@kinal.edu.gt");
            admin2.setPassword("password123"); // Contraseña en texto plano
            admin2.setRol(RolAdministrador.DIRECTOR_ADMIN);
            administradorRepository.save(admin2);

            Administrador admin3 = new Administrador();
            admin3.setNombreCompleto("Orientador");
            admin3.setEmail("admin3@kinal.edu.gt");
            admin3.setPassword("password123"); // Contraseña en texto plano
            admin3.setRol(RolAdministrador.ORIENTACION);
            administradorRepository.save(admin3);

            Administrador admin4 = new Administrador();
            admin4.setNombreCompleto("Secretaria");
            admin4.setEmail("admin4@kinal.edu.gt");
            admin4.setPassword("password123"); // Contraseña en texto plano
            admin4.setRol(RolAdministrador.SECRETARIA);
            administradorRepository.save(admin4);

            Administrador superadmin = new Administrador();
            superadmin.setNombreCompleto("Secretaria");
            superadmin.setEmail("superadmin@kinal.edu.gt");
            superadmin.setPassword("password123"); // Contraseña en texto plano
            superadmin.setRol(RolAdministrador.SUPER_ADMIN);
            administradorRepository.save(superadmin);

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