-- Blue Rebhirt · esquema de base de datos (MySQL 8 / MariaDB 10.5+)
-- Ejecutar completo sobre una base vacía: mysql -u root -p < db/schema.sql
-- Si el proyecto usa PostgreSQL, ver docs/04-base-de-datos.md (sección "Cambiar de motor").

CREATE DATABASE IF NOT EXISTS blue_rebhirt CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE blue_rebhirt;

DROP TABLE IF EXISTS mediciones;
DROP TABLE IF EXISTS cultivos;
DROP TABLE IF EXISTS rangos_ph;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
  id               INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre           VARCHAR(80)  NOT NULL,
  correo           VARCHAR(120) NOT NULL UNIQUE,
  contrasena_hash  VARCHAR(100) NOT NULL,                       -- BCrypt, nunca texto plano
  rol              ENUM('agricultor','administrador') NOT NULL DEFAULT 'agricultor',
  activo           BOOLEAN      NOT NULL DEFAULT TRUE,
  creado_en        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cultivos (
  id             INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  usuario_id     INT UNSIGNED NOT NULL,
  nombre         VARCHAR(80)  NOT NULL,
  lugar          VARCHAR(120),
  fecha_siembra  DATE,
  creado_en      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_cultivo_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  CONSTRAINT uq_cultivo_nombre UNIQUE (usuario_id, nombre)
);

-- Rangos que edita el administrador. El cálculo los lee de aquí; no se escriben en el código.
CREATE TABLE rangos_ph (
  id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  zona            ENUM('acido','bajo','ideal','alto','alcalino') NOT NULL UNIQUE,
  estado          ENUM('ok','aviso','alerta') NOT NULL,
  ph_desde        DECIMAL(3,1) NOT NULL,
  ph_hasta        DECIMAL(3,1) NOT NULL,
  recomendacion   VARCHAR(255) NOT NULL,
  pasos           VARCHAR(600) NOT NULL,                        -- los pasos de "Qué hacer", separados por salto de línea
  orden           TINYINT      NOT NULL,
  CONSTRAINT ck_rango_limites CHECK (ph_desde >= 0 AND ph_hasta <= 14 AND ph_desde <= ph_hasta)
);

CREATE TABLE mediciones (
  id               INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  cultivo_id       INT UNSIGNED NOT NULL,
  ph               DECIMAL(3,1) NOT NULL,
  nivel_agua_cm    DECIMAL(5,1) NOT NULL,
  humedad_pct      DECIMAL(5,1) NOT NULL,
  temperatura_c    DECIMAL(4,1) NOT NULL,
  hojas            ENUM('verdes','amarillas','manchas')                 NOT NULL,
  ultimo_abono     ENUM('menos_2_semanas','2_a_4_semanas','mas_1_mes','nunca') NOT NULL,
  llovio_3_dias    BOOLEAN      NOT NULL,
  rango_id         INT UNSIGNED NOT NULL,
  estado           ENUM('ok','aviso','alerta') NOT NULL,         -- copia del estado al momento de medir
  recomendacion    VARCHAR(255) NOT NULL,                        -- copia del texto al momento de medir
  medido_en        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_med_cultivo FOREIGN KEY (cultivo_id) REFERENCES cultivos(id) ON DELETE CASCADE,
  CONSTRAINT fk_med_rango   FOREIGN KEY (rango_id)   REFERENCES rangos_ph(id),
  CONSTRAINT ck_med_ph      CHECK (ph BETWEEN 0 AND 14),
  CONSTRAINT ck_med_hum     CHECK (humedad_pct BETWEEN 0 AND 100),
  CONSTRAINT ck_med_nivel   CHECK (nivel_agua_cm >= 0),
  CONSTRAINT ck_med_temp    CHECK (temperatura_c BETWEEN -5 AND 60),
  INDEX ix_med_cultivo_fecha (cultivo_id, medido_en)
);
