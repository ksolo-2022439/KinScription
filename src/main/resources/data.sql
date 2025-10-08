INSERT IGNORE INTO Grados (nombre_grado) VALUES
('1ro Básico'), ('2do Básico'), ('3ro Básico'),
('4to Perito'), ('5to Perito'), ('6to Perito');

INSERT IGNORE INTO Secciones (nombre_seccion) VALUES ('A'), ('B'), ('C'), ('D');

INSERT IGNORE INTO Jornadas (nombre_jornada) VALUES ('Matutina'), ('Vespertina');

INSERT IGNORE INTO Cursos (nombre_curso) VALUES
('Física Fundamental'), ('Sociales'), ('Artes Plásticas'), ('Formación Músical'),
('Educación Física'), ('Idioma Inglés'), ('Emprendimiento para la productividad'),
('Idioma Español'), ('Cultura e Idiomas Mayas'), ('Matemáticas'), ('Programa de Actitudes'),
('Religión'), ('TICS'), ('Química'),
('Tecnología I'), ('Sociales I'), ('Inglés I'), ('Ética I'), ('Física I'),
('Lengua y Literatura I'), ('Matemáticas I'), ('Estadística I'), ('Taller I'), ('TICS I'),
('Tecnología II'), ('Sociales II'), ('Inglés II'), ('Ética II'), ('Física II'),
('Lengua y Literatura II'), ('Matemáticas II'), ('Química 5'), ('Taller II'), ('TICS II'),
('Tecnología III'), ('Sociales III'), ('Inglés III'), ('Ética III'), ('Física III'),
('Lengua y Literatura III'), ('Matemáticas III'), ('Biología 6'), ('Taller III'), ('TICS III');

INSERT IGNORE INTO Carreras (nombre_carrera) VALUES
('Informática'),
('Mecánica Automotriz'),
('Electricidad Industrial'),
('Dibujo Técnico de Ingeniería y Arquitectura'),
('Electrónica Industrial'),
('Electrónica en Computación');

-- -----------------------------------------------------
-- Asignación de Cursos para 1ro Básico
-- -----------------------------------------------------
INSERT IGNORE INTO grado_curso (id_grado, id_curso) VALUES
((SELECT id_grado FROM Grados WHERE nombre_grado = '1ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Sociales')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '1ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Artes Plásticas')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '1ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Formación Músical')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '1ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Educación Física')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '1ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Idioma Inglés')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '1ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Emprendimiento para la productividad')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '1ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Idioma Español')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '1ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Cultura e Idiomas Mayas')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '1ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Matemáticas')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '1ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Programa de Actitudes')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '1ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Religión')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '1ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'TICS'));

-- -----------------------------------------------------
-- Asignación de Cursos para 2do Básico
-- -----------------------------------------------------
INSERT IGNORE INTO grado_curso (id_grado, id_curso) VALUES
((SELECT id_grado FROM Grados WHERE nombre_grado = '2do Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Sociales')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '2do Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Artes Plásticas')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '2do Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Formación Músical')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '2do Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Educación Física')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '2do Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Idioma Inglés')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '2do Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Emprendimiento para la productividad')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '2do Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Idioma Español')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '2do Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Cultura e Idiomas Mayas')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '2do Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Matemáticas')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '2do Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Programa de Actitudes')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '2do Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Religión')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '2do Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'TICS'));

-- -----------------------------------------------------
-- Asignación de Cursos para 3ro Básico
-- -----------------------------------------------------
INSERT IGNORE INTO grado_curso (id_grado, id_curso) VALUES
((SELECT id_grado FROM Grados WHERE nombre_grado = '3ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Sociales')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '3ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Artes Plásticas')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '3ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Formación Músical')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '3ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Educación Física')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '3ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Idioma Inglés')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '3ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Emprendimiento para la productividad')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '3ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Idioma Español')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '3ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Cultura e Idiomas Mayas')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '3ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Matemáticas')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '3ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Programa de Actitudes')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '3ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Religión')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '3ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'TICS')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '3ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Física Fundamental')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '3ro Básico'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Química'));

-- -----------------------------------------------------
-- Asignación de Cursos para 4to Perito
-- -----------------------------------------------------
INSERT IGNORE INTO grado_curso (id_grado, id_curso) VALUES
((SELECT id_grado FROM Grados WHERE nombre_grado = '4to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Tecnología I')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '4to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Sociales I')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '4to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Inglés I')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '4to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Ética I')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '4to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Física I')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '4to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Lengua y Literatura I')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '4to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Matemáticas I')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '4to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Estadística I')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '4to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Taller I')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '4to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'TICS I')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '4to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Educación Física'));

-- -----------------------------------------------------
-- Asignación de Cursos para 5to Perito
-- -----------------------------------------------------
INSERT IGNORE INTO grado_curso (id_grado, id_curso) VALUES
((SELECT id_grado FROM Grados WHERE nombre_grado = '5to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Tecnología II')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '5to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Sociales II')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '5to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Inglés II')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '5to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Ética II')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '5to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Física II')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '5to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Lengua y Literatura II')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '5to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Matemáticas II')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '5to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Química 5')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '5to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Taller II')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '5to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'TICS II'));

-- -----------------------------------------------------
-- Asignación de Cursos para 6to Perito
-- -----------------------------------------------------
INSERT IGNORE INTO grado_curso (id_grado, id_curso) VALUES
((SELECT id_grado FROM Grados WHERE nombre_grado = '6to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Tecnología III')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '6to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Sociales III')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '6to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Inglés III')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '6to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Ética III')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '6to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Física III')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '6to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Lengua y Literatura III')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '6to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Matemáticas III')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '6to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Biología 6')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '6to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'Taller III')),
((SELECT id_grado FROM Grados WHERE nombre_grado = '6to Perito'), (SELECT id_curso FROM Cursos WHERE nombre_curso = 'TICS III'));

-- =============================================================
-- INSERCIÓN DE ADMINISTRADORES
-- =============================================================
INSERT IGNORE INTO Administradores (nombre_completo, email, password, rol) VALUES
('Super Admin', 'superadmin@kinal.edu.gt', 'password123', 'SUPER_ADMIN'),
('Admin Inscripciones', 'admin@kinal.edu.gt', 'password123', 'ADMIN_INSCRIPCION'),
('Director Administrativo', 'director@kinal.edu.gt', 'password123', 'DIRECTOR_ADMIN'),
('Orientacion Academica', 'orientacion@kinal.edu.gt', 'password123', 'ORIENTACION'),
('Secretaria General', 'secretaria@kinal.edu.gt', 'password123', 'SECRETARIA');

-- =============================================================
-- INSERCIÓN DE CICLO DE ADMISIÓN
-- =============================================================
INSERT IGNORE INTO adm_ciclos_academicos (id, nombre, fecha_inicio, fecha_fin) VALUES (1, 'Admisión 2026', '2025-10-01', '2026-03-31');

-- =============================================================
-- INSERCIÓN DE PARTICIPANTES (ASPIRANTES)
-- =============================================================
-- #1 Pendiente de Examen
INSERT IGNORE INTO adm_participantes (username, password, nombre_completo, apellidos, fecha_nacimiento, direccion, estado, id_ciclo_academico, id_grado_aplica) VALUES
('aspirante01', 'password123', 'Juan Carlos', 'Pérez Gómez', '2008-05-10', 'Zona 7, Ciudad de Guatemala', 'PENDIENTE_EXAMEN', 1, 4);

-- #2 Examen Realizado (listo para calificar)
INSERT IGNORE INTO adm_participantes (username, password, nombre_completo, apellidos, fecha_nacimiento, direccion, estado, id_ciclo_academico, id_grado_aplica, id_carrera_aplica) VALUES
('aspirante02', 'password123', 'Ana Sofía', 'Martínez López', '2008-03-15', 'Zona 11, Mixco', 'EXAMEN_REALIZADO', 1, 4, 1);

-- #3 Examen Admitido (listo para llenar socioeconómico)
INSERT IGNORE INTO adm_participantes (username, password, nombre_completo, apellidos, fecha_nacimiento, direccion, estado, nota_examen, id_ciclo_academico, id_grado_aplica) VALUES
('aspirante03', 'password123', 'Luis Fernando', 'García Ramírez', '2009-01-20', 'Zona 1, Villa Nueva', 'ADMITIDO_EXAMEN', 85.50, 1, 1);

-- #4 Socioeconómico Enviado (listo para revisar por Director)
INSERT IGNORE INTO adm_participantes (username, password, nombre_completo, apellidos, fecha_nacimiento, direccion, estado, nota_examen, id_ciclo_academico, id_grado_aplica, id_carrera_aplica) VALUES
('aspirante04', 'password123', 'María José', 'Hernández Díaz', '2008-11-30', 'Zona 10, Guatemala', 'SOCIOECONOMICO_ENVIADO', 92.00, 1, 4, 5);
INSERT IGNORE INTO adm_estudios_socioeconomicos (id_participante, datos_tutor_nombre, datos_tutor_apellido, datos_tutor_telefono, datos_tutor_direccion) VALUES
(4, 'Jorge', 'Hernández', '5555-1111', 'Misma dirección que el aspirante');

-- #5 Admitido por Director (listo para revisar por Orientación)
INSERT IGNORE INTO adm_participantes (username, password, nombre_completo, apellidos, fecha_nacimiento, direccion, estado, nota_examen, id_ciclo_academico, id_grado_aplica, id_carrera_aplica) VALUES
('aspirante05', 'password123', 'Carlos David', 'Sánchez Torres', '2007-07-07', 'Carretera a El Salvador', 'ADMITIDO_SOCIOECONOMICO', 78.00, 1, 4, 3);
INSERT IGNORE INTO adm_estudios_socioeconomicos (id_participante, datos_tutor_nombre, datos_tutor_apellido, datos_tutor_telefono, datos_tutor_direccion, monto_inscripcion, monto_mensualidad, aprobado_director) VALUES
(5, 'Elena', 'Torres', '5555-2222', 'Misma dirección', 500.00, 850.00, TRUE);

-- #6 Formulario Admitido (listo para subir papelería)
INSERT IGNORE INTO adm_participantes (username, password, nombre_completo, apellidos, fecha_nacimiento, direccion, estado, nota_examen, id_ciclo_academico, id_grado_aplica, id_tutor_generado) VALUES
('aspirante06', 'password123', 'Laura Valentina', 'González Castillo', '2009-02-14', 'Zona 16, Guatemala', 'ADMITIDO_FORMULARIO', 95.00, 1, 1, 1);
INSERT IGNORE INTO adm_estudios_socioeconomicos (id_participante, datos_tutor_nombre, datos_tutor_apellido, datos_tutor_telefono, datos_tutor_direccion, monto_inscripcion, monto_mensualidad, aprobado_director, aprobado_orientacion) VALUES
(6, 'Carlos', 'González', '55123456', '4a Calle 12-34 Zona 5', 600.00, 900.00, TRUE, TRUE);

-- #7 Papelería Enviada (lista para revisar por Secretaría)
INSERT IGNORE INTO adm_participantes (username, password, nombre_completo, apellidos, fecha_nacimiento, direccion, estado, nota_examen, id_ciclo_academico, id_grado_aplica, id_tutor_generado) VALUES
('aspirante07', 'password123', 'Diego Alejandro', 'Morales Reyes', '2008-09-01', 'Zona 2, Guatemala', 'PAPELERIA_ENVIADA', 88.00, 1, 4, 2);
INSERT IGNORE INTO adm_documentos_requeridos (id_participante, nombre_documento, url_archivo, estado_revision) VALUES
(7, 'Acta de Nacimiento', 'placeholder.pdf', 'PENDIENTE');

-- #8 Papelería Admitida (listo para subir contrato)
INSERT IGNORE INTO adm_participantes (username, password, nombre_completo, apellidos, fecha_nacimiento, direccion, estado, nota_examen, id_ciclo_academico, id_grado_aplica, id_carrera_aplica, id_tutor_generado) VALUES
('aspirante08', 'password123', 'Sofía Isabel', 'Guzmán Ortiz', '2007-12-25', 'Zona 15, Guatemala', 'ADMITIDO_PAPELERIA', 91.00, 1, 5, 6, 3);

-- #9 Contrato Enviado (listo para revisión final)
INSERT IGNORE INTO adm_participantes (username, password, nombre_completo, apellidos, fecha_nacimiento, direccion, estado, nota_examen, id_ciclo_academico, id_grado_aplica, id_carrera_aplica, id_tutor_generado) VALUES
('aspirante09', 'password123', 'Javier Andrés', 'Valdez Salazar', '2008-08-18', 'Zona 9, Guatemala', 'CONTRATO_ENVIADO', 75.00, 1, 4, 4, 4);
INSERT IGNORE INTO adm_contratos (id_participante, nombre_abogado, colegiado_abogado, url_pdf_contrato, aprobado) VALUES
(9, 'Lic. Ricardo Méndez', '98765', 'placeholder.pdf', FALSE);

-- #10 Participante Desaprobado
INSERT IGNORE INTO adm_participantes (username, password, nombre_completo, apellidos, fecha_nacimiento, direccion, estado, nota_examen, id_ciclo_academico, id_grado_aplica) VALUES
('aspirante10', 'password123', 'Lucía Fernanda', 'Castillo Mendoza', '2009-04-12', 'Zona 5, Guatemala', 'DESAPROBADO', 45.00, 1, 1);

-- =============================================================
-- INSERCIÓN DE TUTORES
-- =============================================================
INSERT IGNORE INTO Tutores (nombre_completo, apellido_completo, numero_telefono, direccion) VALUES
('Carlos', 'González', '55123456', '4a Calle 12-34 Zona 5, Guatemala'),
('Ana', 'Martínez', '42987654', 'Avenida Las Américas 20-21 Zona 13, Guatemala'),
('Luis', 'Hernández', '31225588', 'Calzada Roosevelt 5-55 Zona 11, Mixco'),
('María', 'López', '50018899', '15 Av. 4-23 Zona 1, Villa Nueva'),
('Jorge', 'Pérez', '47654321', 'Boulevard Los Próceres 18-09 Zona 10, Guatemala'),
('Sofía', 'Ramírez', '33445566', 'Calle Real 1-15 Zona 1, Antigua Guatemala'),
('Javier', 'García', '59871234', '5a Av. Norte 2-30 Zona 2, Quetzaltenango'),
('Laura', 'Díaz', '44556677', 'Diagonal 6, 10-01 Zona 10, Guatemala'),
('David', 'Sánchez', '52109876', '20 Calle 8-45 Zona 16, Guatemala'),
('Elena', 'Torres', '30098765', 'Anillo Periférico 22-51 Zona 7, Guatemala');

-- =============================================================
-- INSERCIÓN DE PROFESORES
-- =============================================================
INSERT IGNORE INTO Profesores (nombre_completo, apellido_completo, direccion, numero_telefono, email, contrasena) VALUES
('Roberto', 'Mendoza', '10a Av. 5-30 Zona 9, Guatemala', '58765432', 'roberto.mendoza@kinscript.org', 'profe123'),
('Patricia', 'Reyes', '8a Calle 14-56 Zona 1, Guatemala', '41239876', 'patricia.reyes@kinscript.org', 'profe123'),
('Fernando', 'Castillo', 'Calle Mariscal Cruz 9-87 Zona 5, Guatemala', '32104567', 'fernando.castillo@kinscript.org', 'profe123'),
('Gabriela', 'Morales', 'Avenida Reforma 7-23 Zona 9, Guatemala', '55566677', 'gabriela.morales@kinscript.org', 'profe123'),
('Andrés', 'Ortiz', 'Calzada Atanasio Tzul 19-90 Zona 12, Guatemala', '49876543', 'andres.ortiz@kinscript.org', 'profe123'),
('Verónica', 'Guzmán', 'Boulevard Vista Hermosa 20-45 Zona 15, Guatemala', '31122334', 'veronica.guzman@kinscript.org', 'profe123');

-- =============================================================
-- INSERCIÓN DE COORDINADORES
-- =============================================================
INSERT IGNORE INTO Coordinadores (nombre_completo, apellido_completo, email, contrasena, id_grado) VALUES
('Mónica', 'Salazar', 'monica.salazar@kinscript.org', 'coordinador123', (SELECT id_grado FROM Grados WHERE nombre_grado = '1ro Básico')),
('Ricardo', 'Valdez', 'ricardo.valdez@kinscript.org', 'coordinador123', (SELECT id_grado FROM Grados WHERE nombre_grado = '4to Perito'));

-- =============================================================
-- INSERCIÓN DE ALUMNOS
-- =============================================================
-- 1ro Básico
INSERT IGNORE INTO Alumnos (carnet_alumno, nombre_completo, apellido_completo, email_academico, contrasena, direccion, id_grado, id_seccion, id_jornada, id_carrera, id_tutor) VALUES
('2025001', 'Juan', 'Pérez López', 'juan.perez@kinscript.edu.gt', 'alumno123', 'Colonia El Milagro, Zona 6, Mixco', 1, 1, 1, NULL, 1),
('2025002', 'Maria', 'Gomez Hernandez', 'maria.gomez@kinscript.edu.gt', 'alumno123', 'Residenciales San José, Zona 1, San José Pinula', 1, 2, 1, NULL, 2),
('2025003', 'Pedro', 'Chavez Garcia', 'pedro.chavez@kinscript.edu.gt', 'alumno123', 'Condominio Las Victorias, Zona 10, Guatemala', 1, 1, 2, NULL, 3);

-- 2do Básico
INSERT IGNORE INTO Alumnos (carnet_alumno, nombre_completo, apellido_completo, email_academico, contrasena, direccion, id_grado, id_seccion, id_jornada, id_carrera, id_tutor) VALUES
('2025004', 'Lucia', 'Morales Ramirez', 'lucia.morales@kinscript.edu.gt', 'alumno123', '4a Calle Poniente, Antigua Guatemala', 2, 1, 1, NULL, 4),
('2025005', 'Jose', 'Santos Diaz', 'jose.santos@kinscript.edu.gt', 'alumno123', 'Avenida Petapa 34-01, Zona 12, Guatemala', 2, 2, 2, NULL, 5);

-- 3ro Básico
INSERT IGNORE INTO Alumnos (carnet_alumno, nombre_completo, apellido_completo, email_academico, contrasena, direccion, id_grado, id_seccion, id_jornada, id_carrera, id_tutor) VALUES
('2025006', 'Ana', 'Castillo Torres', 'ana.castillo@kinscript.edu.gt', 'alumno123', 'Km 15 Carretera a El Salvador, Fraijanes', 3, 1, 1, NULL, 6),
('2025007', 'Miguel', 'Reyes Mendoza', 'miguel.reyes@kinscript.edu.gt', 'alumno123', 'Santa Catarina Pinula, Guatemala', 3, 1, 2, NULL, 7);

-- 4to Perito en Informática
INSERT IGNORE INTO Alumnos (carnet_alumno, nombre_completo, apellido_completo, email_academico, contrasena, direccion, id_grado, id_seccion, id_jornada, id_carrera, id_tutor) VALUES
('2025008', 'Sofia', 'Guzman Ortiz', 'sofia.guzman@kinscript.edu.gt', 'alumno123', 'Zona 4, Quetzaltenango', 4, 1, 1, 1, 8),
('2025009', 'Daniel', 'Salazar Valdez', 'daniel.salazar@kinscript.edu.gt', 'alumno123', '1ra Calle 1-23 Zona 1, Cobán', 4, 2, 1, 1, 9);

-- 5to Perito en Mecánica
INSERT IGNORE INTO Alumnos (carnet_alumno, nombre_completo, apellido_completo, email_academico, contrasena, direccion, id_grado, id_seccion, id_jornada, id_carrera, id_tutor) VALUES
('2025010', 'Camila', 'Gomez Hernandez', 'camila.gomez@kinscript.edu.gt', 'alumno123', 'Residenciales San José, Zona 1, San José Pinula', 5, 1, 2, 2, 2);

-- 6to Perito en Electrónica
INSERT IGNORE INTO Alumnos (carnet_alumno, nombre_completo, apellido_completo, email_academico, contrasena, direccion, id_grado, id_seccion, id_jornada, id_carrera, id_tutor) VALUES
('2025011', 'Mateo', 'Pérez López', 'mateo.perez@kinscript.edu.gt', 'alumno123', '4a Calle 12-34 Zona 5, Guatemala', 6, 1, 1, 5, 1);


-- =============================================================
-- INSERCIÓN DE NOTAS
-- =============================================================

-- Notas para Juan Pérez (id_alumno=1, id_grado=1)
INSERT IGNORE INTO Notas (id_alumno, id_curso, bimestre1, bimestre2, bimestre3, bimestre4) VALUES
(1, 2, 85.50, 90.00, 88.75, 92.50), -- Sociales
(1, 3, 78.00, 82.50, 80.00, 79.50), -- Artes Plásticas
(1, 10, 92.00, 88.50, 95.00, 93.00); -- Matemáticas

-- Notas para Maria Gomez (id_alumno=2, id_grado=1)
INSERT IGNORE INTO Notas (id_alumno, id_curso, bimestre1, bimestre2, bimestre3, bimestre4) VALUES
(2, 2, 90.00, 91.50, 89.00, 94.00), -- Sociales
(2, 3, 88.00, 85.00, 86.50, 90.00), -- Artes Plásticas
(2, 10, 95.00, 96.50, 94.00, 97.00); -- Matemáticas

-- Notas para Lucia Morales (id_alumno=4, id_grado=2)
INSERT IGNORE INTO Notas (id_alumno, id_curso, bimestre1, bimestre2, bimestre3, bimestre4) VALUES
(4, 2, 76.00, 80.50, 79.00, 81.00), -- Sociales
(4, 10, 82.50, 85.00, 88.00, 86.50); -- Matemáticas

-- Notas para Sofia Guzman (id_alumno=8, id_grado=4)
INSERT IGNORE INTO Notas (id_alumno, id_curso, bimestre1, bimestre2, bimestre3, bimestre4) VALUES
(8, 15, 88.00, 92.50, 90.00, 94.00), -- Tecnología I
(8, 21, 91.50, 89.00, 93.00, 95.50), -- Matemáticas I
(8, 23, 85.00, 88.00, 86.50, 90.00); -- Taller I

