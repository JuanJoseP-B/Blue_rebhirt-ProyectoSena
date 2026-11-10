# 04 · Base de datos

Motor: MySQL 8 (o MariaDB 10.5+). Scripts: `db/schema.sql` y `db/seed.sql`. **Revísalos, pruébalos contra un servidor real y corrígelos si hace falta** (no se han ejecutado todavía).

## Tablas
| Tabla | Para qué |
| --- | --- |
| `usuarios` | Cuentas. Rol `agricultor` o `administrador`. Contraseña en BCrypt. |
| `cultivos` | Lotes de plátano de cada agricultor. Un nombre no se repite dentro del mismo usuario. |
| `rangos_ph` | Cinco franjas editables por el administrador: `acido`, `bajo`, `ideal`, `alto`, `alcalino`. Cada una tiene estado, pH desde/hasta, recomendación y pasos. |
| `mediciones` | Cada medición con los datos escritos, las respuestas, y una **copia** del estado y la recomendación del momento. |

Relaciones: `usuarios 1—N cultivos 1—N mediciones`; `mediciones N—1 rangos_ph`.

## Por qué mediciones guarda una copia del estado
Si el administrador cambia un rango mañana, las mediciones de ayer no deben cambiar de estado. Por eso `mediciones` guarda `estado` y `recomendacion` aparte de `rango_id`.

## pH con un decimal
Todo pH se guarda con un decimal (`DECIMAL(3,1)`); el servidor redondea (mitad hacia arriba) lo que escriba el usuario. Así los rangos no dejan huecos entre, por ejemplo, 4.9 y 5.0.

## Reglas de los rangos (el servidor las valida al guardar)
1. Son exactamente cinco, en este orden: acido, bajo, ideal, alto, alcalino. El administrador cambia límites y textos, no agrega ni borra filas.
2. El primero empieza en 0.0 y el último termina en 14.0.
3. Cada `desde` es mayor o igual que 0 y menor o igual que su `hasta`.
4. **Sin huecos ni solapes**: el `desde` de cada fila es exactamente el `hasta` de la anterior más 0.1.
5. Estados fijos: acido = alerta, bajo = aviso, ideal = ok, alto = aviso, alcalino = alerta.
6. Las recomendaciones no pueden estar vacías (máximo 255 caracteres).
7. Los `pasos` los edita el administrador en una versión futura; por ahora el formulario solo edita límites y recomendación, y los pasos quedan como en el seed.

## Valores de ejemplo (a validar con la profesora o el técnico)
Rango ideal 5.5–7.0; avisos 5.0–5.4 y 7.1–7.5; alertas fuera de eso. Textos en `seed.sql`.

## Usuarios de prueba
No pongas hashes en el SQL. El backend crea, al arrancar y solo si `usuarios` está vacía, estos usuarios con BCrypt:
- `admin@bluerebhirt.test` / `admin123` (administrador)
- `ana@bluerebhirt.test` / `ana123` (agricultora, con 2 cultivos: "Lote La Cumbre 1" y "Lote del río", y 8 mediciones de ejemplo para "Lote La Cumbre 1" entre el 12 de octubre y el 7 de noviembre, con valores 6.1, 6.3, 5.9, 5.4, 4.9, 5.6, 6.4, 6.0 y la medición del "Lote del río" con pH 4.8). Calcula el estado de cada una con la misma lógica de producción.
Esto se activa solo con `app.datos-demo=true` (activado en desarrollo, desactivado en producción).

## Cambiar de motor
Si prefieres PostgreSQL: `ENUM` → `VARCHAR` + `CHECK ... IN (...)`; `AUTO_INCREMENT` → `GENERATED ALWAYS AS IDENTITY`; `BOOLEAN` es igual; `TIMESTAMP DEFAULT CURRENT_TIMESTAMP` es igual; quita `CREATE DATABASE ... CHARACTER SET` y `USE`. Cambia el driver en `pom.xml`. No cambies nada más del diseño.
