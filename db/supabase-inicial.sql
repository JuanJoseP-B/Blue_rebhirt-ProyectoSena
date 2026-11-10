-- Ejecutar una sola vez en un proyecto Supabase nuevo, después de revisar el destino.
-- No elimina tablas ni modifica los datos de una instalación existente.
BEGIN;
CREATE SCHEMA bluerebhirt;
REVOKE ALL ON SCHEMA bluerebhirt FROM PUBLIC, anon, authenticated;

CREATE TABLE bluerebhirt.usuarios (
  id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  nombre VARCHAR(80) NOT NULL,
  correo VARCHAR(120) NOT NULL,
  contrasena_hash VARCHAR(100) NOT NULL,
  rol VARCHAR(20) NOT NULL DEFAULT 'agricultor' CHECK (rol IN ('agricultor', 'administrador')),
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  creado_en TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'America/Bogota')
);
CREATE UNIQUE INDEX uq_usuarios_correo ON bluerebhirt.usuarios (lower(correo));

CREATE TABLE bluerebhirt.cultivos (
  id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  usuario_id INTEGER NOT NULL REFERENCES bluerebhirt.usuarios(id) ON DELETE CASCADE,
  nombre VARCHAR(80) NOT NULL,
  lugar VARCHAR(120),
  fecha_siembra DATE,
  creado_en TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'America/Bogota')
);
CREATE UNIQUE INDEX uq_cultivo_nombre ON bluerebhirt.cultivos (usuario_id, lower(nombre));

CREATE TABLE bluerebhirt.rangos_ph (
  id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  zona VARCHAR(12) NOT NULL UNIQUE CHECK (zona IN ('acido', 'bajo', 'ideal', 'alto', 'alcalino')),
  estado VARCHAR(10) NOT NULL CHECK (estado IN ('ok', 'aviso', 'alerta')),
  ph_desde DECIMAL(3,1) NOT NULL,
  ph_hasta DECIMAL(3,1) NOT NULL,
  recomendacion VARCHAR(255) NOT NULL,
  pasos VARCHAR(600) NOT NULL,
  orden SMALLINT NOT NULL,
  CONSTRAINT ck_rango_limites CHECK (ph_desde >= 0 AND ph_hasta <= 14 AND ph_desde <= ph_hasta)
);

CREATE TABLE bluerebhirt.mediciones (
  id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  cultivo_id INTEGER NOT NULL REFERENCES bluerebhirt.cultivos(id) ON DELETE CASCADE,
  ph DECIMAL(3,1) NOT NULL CHECK (ph BETWEEN 0 AND 14),
  nivel_agua_cm DECIMAL(5,1) NOT NULL CHECK (nivel_agua_cm >= 0),
  humedad_pct DECIMAL(5,1) NOT NULL CHECK (humedad_pct BETWEEN 0 AND 100),
  temperatura_c DECIMAL(4,1) NOT NULL CHECK (temperatura_c BETWEEN -5 AND 60),
  hojas VARCHAR(20) NOT NULL CHECK (hojas IN ('verdes', 'amarillas', 'manchas')),
  ultimo_abono VARCHAR(20) NOT NULL CHECK (ultimo_abono IN ('menos_2_semanas', '2_a_4_semanas', 'mas_1_mes', 'nunca')),
  llovio_3_dias BOOLEAN NOT NULL,
  rango_id INTEGER NOT NULL REFERENCES bluerebhirt.rangos_ph(id),
  estado VARCHAR(10) NOT NULL CHECK (estado IN ('ok', 'aviso', 'alerta')),
  recomendacion VARCHAR(255) NOT NULL,
  medido_en TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'America/Bogota')
);
CREATE INDEX ix_med_cultivo_fecha ON bluerebhirt.mediciones (cultivo_id, medido_en);

-- La aplicación accede mediante su conexión de servidor. No se abren políticas públicas.
ALTER TABLE bluerebhirt.usuarios ENABLE ROW LEVEL SECURITY;
ALTER TABLE bluerebhirt.cultivos ENABLE ROW LEVEL SECURITY;
ALTER TABLE bluerebhirt.rangos_ph ENABLE ROW LEVEL SECURITY;
ALTER TABLE bluerebhirt.mediciones ENABLE ROW LEVEL SECURITY;

INSERT INTO bluerebhirt.rangos_ph (zona, estado, ph_desde, ph_hasta, recomendacion, pasos, orden) VALUES
('acido', 'alerta', 0.0, 4.9, 'El agua está ácida. Revisa el abono y vuelve a medir mañana.', E'Revisa el abono que estás usando.\nVuelve a medir mañana a la misma hora.\nSi sigue por debajo de 5.5, consulta con tu técnico.', 1),
('bajo', 'aviso', 5.0, 5.4, 'Está cerca del límite bajo. Vuelve a medir mañana antes de abonar.', E'No abones todavía.\nVuelve a medir mañana a la misma hora.\nSi baja de 5.0, consulta con tu técnico.', 2),
('ideal', 'ok', 5.5, 7.0, 'El agua está bien. Sigue con tu riego normal.', E'Sigue con tu riego y tu abono normales.\nMide de nuevo en unos días.', 3),
('alto', 'aviso', 7.1, 7.5, 'Está cerca del límite alto. Vuelve a medir mañana.', E'Vuelve a medir mañana a la misma hora.\nRevisa que el agua de riego no tenga cal ni cenizas.', 4),
('alcalino', 'alerta', 7.6, 14.0, 'El agua es muy alcalina. Revisa el abono y consulta con tu técnico.', E'Revisa el abono que estás usando.\nConsulta con tu técnico.\nVuelve a medir mañana.', 5);
COMMIT;
