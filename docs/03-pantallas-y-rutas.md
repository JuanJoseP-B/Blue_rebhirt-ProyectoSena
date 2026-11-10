# 03 · Pantallas, rutas y comportamiento

Convenciones: GET muestra la pantalla; POST procesa el formulario. Los ejemplos de datos en los HTML ("Ana", "Lote del río", pH 4.8) son de muestra: en la versión real vienen de la base de datos.

## Acceso y redirecciones
- Sin sesión, cualquier ruta de agricultor o administrador redirige a `/entrar`.
- Tras entrar: agricultor → `/inicio`; administrador → `/admin`.
- `/` con sesión iniciada redirige al inicio del rol.
- Un agricultor que abre `/admin/**` vuelve a `/inicio`.
- "Salir" es un POST a `/salir` (cierra la sesión y lleva a `/`).

## `/` Inicio público (`index.html`)
Protagonista verde con título "Mide el pH del agua de tus plataneras" y botón "Crear cuenta". Apoyo: tarjeta con logo y "Qué es Blue Rebhirt"; "Cómo funciona" en tres pasos. Enlaces: Guía de abonos, Ya tengo cuenta. Estático, sin datos.

## `/entrar` (`entrar.html`)
Campos: correo, contraseña. POST `/entrar` (Spring Security `formLogin`). Error: mensaje bajo el campo con el estilo `campo--error` ("Correo o contraseña incorrectos."). No digas cuál de los dos falló.

## `/registro` (`registro.html`)
Campos: nombre (obligatorio, hasta 80), correo (válido, único, hasta 120), contraseña (mínimo 6). POST `/registro`: crea usuario con rol `agricultor`, inicia sesión y redirige a `/cultivos`. Correo repetido: error en el campo ("Ese correo ya tiene una cuenta.").

## `/inicio` Inicio del agricultor (`panel.html`)
- Saludo: "Hola, {nombre}" y la fecha de hoy en español ("Hoy es lunes 9 de noviembre.").
- Protagonista: "Nueva medición" con el árbol animado y botón inverso hacia `/mediciones/nueva`.
- Apoyo "Última medición": estado, pH, cultivo y "Hace N días" / "Hoy" / "Ayer". Si no hay mediciones: "Todavía no has medido." sin estado.
- Apoyo "Mis cultivos": "N cultivos registrados".
- Enlaces: Historial, Guía de abonos.
- **Sin cultivos**: el protagonista cambia a "Crea tu primer cultivo" (mismo componente, botón hacia `/cultivos`) y el botón de "Nueva medición" desaparece del menú y de la pantalla.

## `/cultivos` (`cultivos.html`)
- Protagonista: formulario "Agregar un cultivo": nombre (obligatorio, único por usuario), dónde queda (opcional, ayuda "Una referencia para reconocer el lote."), fecha de siembra (opcional, no futura). POST `/cultivos`, vuelve a `/cultivos`.
- Apoyo: una tarjeta por cultivo del usuario: nombre, "Plátano · sembrado el 12 de marzo", último estado "✓ pH 6.2 · En rango" y "Última medición hace 2 días" (o "Sin mediciones").
- Enlace: Ver historial.

## `/mediciones/nueva` (`medicion.html`)
Protagonista: formulario con "Datos de hoy".
| Campo | Tipo | Validación |
| --- | --- | --- |
| Cultivo | select de los cultivos del usuario | obligatorio, debe ser suyo |
| pH del agua | número, paso 0.1, unidad "pH" | 0 a 14; se redondea a 1 decimal |
| Nivel de agua | número, unidad "cm" | 0 a 500 |
| Humedad del suelo | número, unidad "%" | 0 a 100 |
| Temperatura | número, unidad "°C" | -5 a 60 |
| ¿Cómo se ven las hojas? | select | `verdes` "Verdes y firmes", `amarillas` "Amarillas", `manchas` "Con manchas" |
| ¿Cuándo abonaste por última vez? | select | `menos_2_semanas` "Hace menos de 2 semanas", `2_a_4_semanas` "Hace 2 a 4 semanas", `mas_1_mes` "Hace más de un mes", `nunca` "Todavía no he abonado" |
| ¿Llovió en los últimos 3 días? | select | No / Sí |

Los campos numéricos empiezan **vacíos** en la versión real (los valores del HTML son de muestra). Apoyo "Cómo medir bien": tres pasos fijos. POST `/mediciones`: valida, calcula (ver `05-backend.md`), guarda y redirige a `/mediciones/{id}`. Si hay errores, vuelve al formulario conservando lo escrito y con el estilo `campo--error` y mensajes como "Escribe un valor entre 0 y 100." en cada campo.

## `/mediciones/{id}` Resultado (`resultado.html`)
Solo el dueño (otro usuario recibe 404). Encabezado: "Resultado de tu medición" y "{cultivo} · {hoy, 9 de noviembre | 7 de noviembre}". Protagonista: `ResultadoPh` grande con pH, estado, recomendación y escala con la marca. Apoyo "Qué hacer": los `pasos` del rango, como lista numerada (separados por salto de línea). Apoyo "Datos de esta medición": nivel de agua, humedad, temperatura. Enlaces: Hacer otra medición, Ver historial. Muestra el estado y la recomendación **guardados en la medición**, no los recalculados (si el administrador cambia rangos después, el historial no debe cambiar).

## `/historial` (`historial.html`)
Parámetro `?cultivo={id}`; por defecto el cultivo con la medición más reciente. Protagonista: select de cultivo (al cambiar, recarga la página; puede hacerse con un botón "Ver" dentro de un form GET o con `onchange="this.form.submit()"`) y la gráfica de las últimas 8 mediciones (ver geometría en `02`). Apoyo "Últimas mediciones": lista con "26 oct · 4.9" y el estado. Apoyo "Resumen": "El pH promedio fue X. N mediciones salieron del rango ideal." (cuenta las de estado distinto de `ok`). Sin mediciones: mensaje "Este cultivo todavía no tiene mediciones." y sin gráfica. Enlaces: Ver mis cultivos, Guía de abonos.

## `/guia` (`guia.html`)
Pública. Los tres bloques (ácido, ideal, alcalino) y los números de la escala se **leen de `rangos_ph`**, igual que el administrador los edite. Incluye la escala sin marca y "Cómo leer el pH". El botón "Nueva medición" del menú solo aparece si hay sesión de agricultor.

## `/admin` Panel del administrador (`admin.html`)
Protagonista: formulario "Rangos y recomendaciones" con las 5 filas de `rangos_ph` (estado, desde, hasta, recomendación). POST `/admin/rangos` con reglas de validación (ver `04-base-de-datos.md`); si fallan, vuelve con el mensaje de error y no guarda nada. Apoyo: "Usuarios: N agricultores y N administrador(es)", "Cultivos: N cultivos". Enlaces: "Mediciones recientes" y "Restablecer una contraseña".

Pantallas adicionales que el diseño no dibujó (constrúyelas **solo con componentes existentes**: `modulo--apoyo`, `lista`, `campo`, `btn`, sin estilos nuevos):
- `/admin/usuarios`: lista de usuarios (nombre, correo, rol) y, por cada agricultor, un formulario pequeño para poner una contraseña temporal (mínimo 6, se guarda con BCrypt). Aquí vive "Restablecer una contraseña".
- `/admin/cultivos`: lista de cultivos con dueño y último estado.
- `/admin/mediciones`: las últimas 30 mediciones de todos (fecha, cultivo, pH, estado).
- En el menú del administrador, "Panel" y "Rangos" apuntan a `/admin`.

## Errores
- 404 y 403: una página sencilla con el menú, un título ("No encontramos esa página") y un enlace al inicio, con las clases existentes.
- Nunca muestres trazas de Java al usuario.
