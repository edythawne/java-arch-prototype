-- ==============================================================================
-- SCRIPT DE CREACIÓN E INICIALIZACIÓN DE BASE DE DATOS PARA MARIADB
-- Proyecto: Sistema de Gestión de Atención Médica y Acompañamiento Psicológico
-- Idioma de la BD (Tablas y Columnas): Inglés (Snake Case)
-- Documentación / Comentarios: Español
-- Reglas de Estructura:
--   - Nombres de tablas en singular (role, user, etc.), salvo relaciones (user_has_role).
--   - Llave Primaria (PK) de cada tabla nombrada strictly como 'id'.
--   - Columnas por defecto en cada tabla: 'created_at' e 'is_active'.
--   - Estatus y Tipos de consulta como tablas catálogo (appointment_status, appointment_type).
-- ==============================================================================

CREATE DATABASE IF NOT EXISTS db_sgc
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE db_sgc;

-- ------------------------------------------------------------------------------
-- 1. TABLA ROLE (Roles de Usuario)
-- Cataloga los roles de acceso al sistema: ESTUDIANTE, MEDICO, PSICOLOGO
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS role
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NULL,
    is_active   BOOLEAN   DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- 2. TABLA USER (Usuarios del Sistema)
-- Almacena los datos básicos del usuario, incluyendo matrícula, correo institucional
-- y el aula/ubicación habitual dentro del plantel escolar.
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS user
(
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    email              VARCHAR(100) NOT NULL UNIQUE,
    password_hash      VARCHAR(255) NOT NULL,
    first_name         VARCHAR(50)  NOT NULL,
    paternal_last_name VARCHAR(50)  NOT NULL,
    maternal_last_name VARCHAR(50),
    student_number     VARCHAR(20)  NOT NULL UNIQUE,
    classroom_location VARCHAR(100) NULL, -- Aula donde se encuentra habitualmente
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP NULL,
    is_active          BOOLEAN   DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- 3. TABLA USER_HAS_ROLE (Relación Muchos a Muchos entre Usuarios y Roles)
-- Vincula los usuarios con uno o varios roles mediante sus id
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS user_has_role
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT NOT NULL,
    role_id    INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY unique_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- 4. TABLA MEDICAL_RECORD (Expediente Médico Base)
-- Almacena la información médica inicial del estudiante registrada desde su ingreso
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS medical_record
(
    id                      INT AUTO_INCREMENT PRIMARY KEY,
    student_id              INT NOT NULL UNIQUE,
    blood_type              VARCHAR(5),
    allergies               TEXT,
    chronic_conditions      TEXT,
    emergency_contact_name  VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP NULL,
    is_active               BOOLEAN   DEFAULT TRUE,
    FOREIGN KEY (student_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- 5. TABLA APPOINTMENT_TYPE (Catálogo de Tipos de Solicitud de Cita)
-- Clasifica la modalidad de atención (Programada Médica/Psicológica o Emergencia)
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS appointment_type
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NULL,
    is_active   BOOLEAN   DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- 6. TABLA APPOINTMENT_STATUS (Catálogo de Estatus de Solicitud)
-- Define los estados posibles de la solicitud de cita o atención de emergencia
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS appointment_status
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NULL,
    is_active   BOOLEAN   DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- 7. TABLA APPOINTMENT_REQUEST (Solicitud de Cita / Emergencia)
-- Centraliza las solicitudes de citas programadas y de atención de emergencia
-- Utiliza classroom_location para registrar la ubicación exacta del aula
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS appointment_request
(
    id                    INT AUTO_INCREMENT PRIMARY KEY,
    student_id            INT  NOT NULL,
    health_provider_id    INT NULL,
    appointment_type_id   INT  NOT NULL,
    appointment_status_id INT  NOT NULL,
    requested_date        DATETIME NULL,     -- Fecha/hora agendada (para citas programadas)
    classroom_location    VARCHAR(100) NULL, -- Ubicación del aula enviada en la solicitud
    location_details      VARCHAR(100) NULL, -- Observaciones específicas de ubicación (edificio, cubículo, etc.)
    reason                TEXT NOT NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP NULL,
    is_active             BOOLEAN   DEFAULT TRUE,
    FOREIGN KEY (student_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (health_provider_id) REFERENCES user (id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (appointment_type_id) REFERENCES appointment_type (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (appointment_status_id) REFERENCES appointment_status (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- 8. TABLA CONSULTATION_NOTE (Notas de Seguimiento)
-- Almacena el expediente e historial clínico posterior a cada atención realizada
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS consultation_note
(
    id                     INT AUTO_INCREMENT PRIMARY KEY,
    appointment_request_id INT  NOT NULL UNIQUE,
    diagnosis              TEXT NOT NULL,
    treatment_plan         TEXT,
    observations           TEXT,
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP NULL,
    is_active              BOOLEAN   DEFAULT TRUE,
    FOREIGN KEY (appointment_request_id) REFERENCES appointment_request (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==============================================================================
-- INSERCIÓN DE DATOS INICIALES (POBLADO DE CATÁLOGOS)
-- ==============================================================================

-- Poblado de Roles
INSERT INTO role (name, description)
VALUES ('ESTUDIANTE', 'Rol para estudiantes con permisos para agendar citas y solicitar atención de emergencia'),
       ('MEDICO', 'Rol para médicos de medicina general para gestionar citas y expedientes'),
       ('PSICOLOGO', 'Rol para psicólogos para gestionar consultas de acompañamiento emocional y notas de seguimiento');

-- Poblado de Catálogo de Tipos de Solicitud
INSERT INTO appointment_type (name, description)
VALUES ('PROGRAMADA_MEDICA', 'Consulta médica general agendada con al menos 24 horas de anticipación'),
       ('PROGRAMADA_PSICOLOGICA',
        'Consulta de acompañamiento psicológico agendada con al menos 24 horas de anticipación'),
       ('EMERGENCIA', 'Solicitud de atención médica o psicológica de urgencia dentro del plantel escolar');

-- Poblado de Catálogo de Estatus de Solicitud
INSERT INTO appointment_status (name, description)
VALUES ('PENDING', 'Solicitud registrada pendiente de revisión'),
       ('ACCEPTED', 'Solicitud aceptada y programada en la agenda'),
       ('RESCHEDULED', 'Solicitud reprogramada a una nueva fecha y hora'),
       ('REJECTED', 'Solicitud rechazada por falta de disponibilidad u otra razón'),
       ('COMPLETED', 'Atención brindada y consulta concluida exitosamente'),
       ('CANCELLED', 'Solicitud cancelada por el usuario o la institución');

-- ==============================================================================
-- INSERCIÓN DE DATOS DE PRUEBA (20 USUARIOS)
-- Matrículas desde 14610011 hasta 14610030 con dominio @ut.edu.mx
-- ==============================================================================

INSERT INTO user (email, password_hash, first_name, paternal_last_name, maternal_last_name, student_number, classroom_location)
VALUES ('14610011@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Juan', 'Pérez', 'Gómez',
        '14610011', 'Aula A-101'),
       ('14610012@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Maria', 'López',
        'Hernández', '14610012', 'Aula A-102'),
       ('14610013@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Carlos', 'García',
        'Martínez', '14610013', 'Aula B-201'),
       ('14610014@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Ana', 'Rodríguez',
        'Sánchez', '14610014', 'Aula B-202'),
       ('14610015@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Luis', 'González',
        'Ramírez', '14610015', 'Laboratorio L-01'),
       ('14610016@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Sofia', 'Fernández',
        'Torres', '14610016', 'Aula C-301'),
       ('14610017@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Diego', 'Díaz', 'Flores',
        '14610017', 'Aula C-302'),
       ('14610018@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Laura', 'Vásquez',
        'Castro', '14610018', 'Laboratorio L-02'),
       ('14610019@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Jorge', 'Morales',
        'Ortiz', '14610019', 'Aula A-103'),
       ('14610020@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Elena', 'Gutiérrez',
        'Ruiz', '14610020', 'Aula B-203'),
       ('14610021@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Pedro', 'Álvarez',
        'Jiménez', '14610021', 'Cubículo M-01'), -- Personal Médico
       ('14610022@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Patricia', 'Romero',
        'Moreno', '14610022', 'Cubículo P-01'),  -- Personal Psicólogo
       ('14610023@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Gabriel', 'Alonso',
        'Muñoz', '14610023', 'Aula A-104'),
       ('14610024@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Lucía', 'Navarro',
        'Delgado', '14610024', 'Aula B-204'),
       ('14610025@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Fernando', 'Mendoza',
        'Iglesias', '14610025', 'Aula C-303'),
       ('14610026@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Isabel', 'Ramos',
        'Blanco', '14610026', 'Laboratorio L-03'),
       ('14610027@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Ricardo', 'Sanz',
        'Medina', '14610027', 'Aula A-105'),
       ('14610028@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Carmen', 'Gil', 'Vega',
        '14610028', 'Aula B-205'),
       ('14610029@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Alejandro', 'Serrano',
        'Lara', '14610029', 'Cubículo M-02'),    -- Personal Médico
       ('14610030@ut.edu.mx', '$2y$10$e8Z4w1Hq4Zk5T2Y.R7gKquJ5N/O4O2.a4X0f3n.c4r7V8m9L0k1J2', 'Valeria', 'Molina',
        'Soler', '14610030', 'Cubículo P-02');
-- Personal Psicólogo

-- Asignación de Roles a Usuarios de Prueba (user_has_role)
INSERT INTO user_has_role (user_id, role_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1),
       (7, 1),
       (8, 1),
       (9, 1),
       (10, 1),
       (11, 2), -- Pedro (MEDICO)
       (12, 3), -- Patricia (PSICOLOGO)
       (13, 1),
       (14, 1),
       (15, 1),
       (16, 1),
       (17, 1),
       (18, 1),
       (19, 2), -- Alejandro (MEDICO)
       (20, 3);
-- Valeria (PSICOLOGO)

-- ==============================================================================
-- INSERCIÓN DE HISTORIALES MÉDICOS BASE PARA ALUMNOS (MEDICAL_RECORD)
-- ==============================================================================
INSERT INTO medical_record (student_id, blood_type, allergies, chronic_conditions, emergency_contact_name,
                            emergency_contact_phone)
VALUES (1, 'O+', 'Penicilina', 'Ninguna', 'Roberto Pérez', '7661000001'),
       (2, 'A+', 'Polen', 'Rinitis alérgica', 'Rosa Hernández', '7661000002'),
       (3, 'B+', 'Ninguna', 'Asma bronquial leve', 'Carlos García Sr.', '7661000003'),
       (4, 'O-', 'Ibuprofeno', 'Ninguna', 'Marta Sánchez', '7661000004'),
       (5, 'AB+', 'Mariscos', 'Ninguna', 'José González', '7661000005'),
       (6, 'O+', 'Ninguna', 'Ninguna', 'Sofia Torres Sr.', '7661000006'),
       (7, 'A-', 'Aspirina', 'Gastritis crónica', 'Fernando Díaz', '7661000007'),
       (8, 'O+', 'Lácteos', 'Intolerancia a la lactosa', 'Laura Castro Sr.', '7661000008'),
       (9, 'B-', 'Ninguna', 'Diabetes Tipo 1', 'Jorge Morales Sr.', '7661000009'),
       (10, 'O+', 'Sulfa', 'Ninguna', 'Elena Ruiz Sr.', '7661000010'),
       (13, 'A+', 'Ninguna', 'Hipotiroidismo', 'Gabriel Muñoz Sr.', '7661000013'),
       (14, 'O+', 'Polvo / Ácaros', 'Rinitis alérgica', 'Lucía Delgado Sr.', '7661000014'),
       (15, 'B+', 'Ninguna', 'Ninguna', 'Fernando Mendoza Sr.', '7661000015'),
       (16, 'O-', 'Penicilina', 'Ninguna', 'Isabel Blanco Sr.', '7661000016'),
       (17, 'AB-', 'Ninguna', 'Migraña crónica', 'Ricardo Sanz Sr.', '7661000017'),
       (18, 'O+', 'Nueces', 'Ninguna', 'Carmen Vega Sr.', '7661000018');

