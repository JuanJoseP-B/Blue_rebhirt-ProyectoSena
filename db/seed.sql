-- Datos iniciales. Ejecutar después de schema.sql.
-- Los rangos y textos son EJEMPLOS: la profesora/técnico debe validarlos (ver docs/04-base-de-datos.md).
USE blue_rebhirt;

INSERT INTO rangos_ph (zona, estado, ph_desde, ph_hasta, recomendacion, pasos, orden) VALUES
('acido',   'alerta', 0.0, 4.9, 'El agua está ácida. Revisa el abono y vuelve a medir mañana.',
 'Revisa el abono que estás usando.\nVuelve a medir mañana a la misma hora.\nSi sigue por debajo de 5.5, consulta con tu técnico.', 1),
('bajo',    'aviso',  5.0, 5.4, 'Está cerca del límite bajo. Vuelve a medir mañana antes de abonar.',
 'No abones todavía.\nVuelve a medir mañana a la misma hora.\nSi baja de 5.0, consulta con tu técnico.', 2),
('ideal',   'ok',     5.5, 7.0, 'El agua está bien. Sigue con tu riego normal.',
 'Sigue con tu riego y tu abono normales.\nMide de nuevo en unos días.', 3),
('alto',    'aviso',  7.1, 7.5, 'Está cerca del límite alto. Vuelve a medir mañana.',
 'Vuelve a medir mañana a la misma hora.\nRevisa que el agua de riego no tenga cal ni cenizas.', 4),
('alcalino','alerta', 7.6, 14.0, 'El agua es muy alcalina. Revisa el abono y consulta con tu técnico.',
 'Revisa el abono que estás usando.\nConsulta con tu técnico.\nVuelve a medir mañana.', 5);

-- Usuarios de prueba. Las contraseñas deben guardarse con BCrypt: el backend las crea al arrancar
-- si la tabla está vacía (ver docs/05-backend.md, "Datos de prueba"). No pongas hashes a mano.
