# Blue Rebhirt · instrucciones para construir el proyecto

Lee este archivo completo antes de escribir código. Es el punto de entrada; los detalles están en `docs/`.

## Qué se construye
Una página web para agricultores de plátano de Bella Vista, La Cumbre (Valle del Cauca). El agricultor escribe el pH del agua y otros datos que mide a mano (los sensores NO se conectan a la app), responde tres preguntas, y el sistema calcula un estado (en rango / revisar / fuera de rango) y una recomendación usando rangos guardados en la base de datos. El administrador edita esos rangos y los textos.

Es un proyecto de colegio (SENA/Comfandi, Media Técnica). Debe ser **funcional pero sencillo, con código que un estudiante pueda leer y explicar**. Nada de capas innecesarias.

## Stack (fijo)
- Frontend: HTML + CSS ya diseñados. Se convierten en plantillas del servidor. No uses React, Tailwind, Bootstrap ni JavaScript de frameworks.
- Backend: Java 17 + Spring Boot 3 + Maven + Thymeleaf + Spring Security (sesión + BCrypt) + JdbcTemplate (sin JPA/Hibernate).
- Base de datos: MySQL 8 (SQL en `db/`). El esquema y los datos iniciales ya están escritos; revísalos y úsalos.
- JavaScript permitido: solo el mínimo (por ejemplo, cambiar de cultivo en el historial). La página debe funcionar con formularios normales.

## Orden de trabajo
1. Lee `docs/01-producto.md` y `docs/03-pantallas-y-rutas.md`.
2. Crea el proyecto Spring Boot en `backend/` (ver `docs/05-backend.md`).
3. Copia los estilos y logos de `diseno/` a `backend/src/main/resources/static/` **sin modificar los CSS** (ver `docs/02-diseno-y-maquetacion.md`).
4. Crea la base de datos con `db/schema.sql` y `db/seed.sql`.
5. Convierte cada pantalla de `diseno/pantallas/*.html` en una plantilla Thymeleaf. El HTML y las clases CSS deben quedar idénticos; solo cambian los datos y los enlaces.
6. Implementa el cálculo del pH, el login por roles y los formularios.
7. Verifica con `docs/06-plan-y-verificacion.md` comparando cada pantalla con las imágenes de `referencia/`.

## Reglas que no se negocian
- **Diseño**: no inventes colores, tamaños ni componentes. Todo sale de `diseno/css/` y de `diseno/SISTEMA-DE-DISENO.md`. Si necesitas algo nuevo, pregunta antes.
- **Texto**: todo el texto va en español, tratando al usuario de **tú** ("Escribe el pH", "Tu cultivo"). Usa los textos exactos de las pantallas.
- **Un solo botón primario por pantalla** (`btn--primario`). No agregues más.
- **pH de 0 a 14** (ácido < 7, neutro = 7, alcalino > 7). Rango ideal del plátano: 5.5 a 7.0 (valor de ejemplo, editable por el administrador).
- **Los rangos y recomendaciones viven en la base de datos** (tabla `rangos_ph`). Nunca los escribas fijos en el código.
- **Seguridad básica obligatoria**: contraseñas con BCrypt, consultas con parámetros (nunca concatenar SQL), validar todo en el servidor, un agricultor solo ve sus propios cultivos y mediciones, solo el administrador entra a `/admin/**`.
- **Nombre del proyecto**: el logo dice "Blue Rebirth" y los textos dicen "Blue Rebhirt". Aún no está decidido. Pon el nombre en una sola constante (`application.properties`: `app.nombre`) para cambiarlo en un lugar.
- No agregues nada fuera del alcance (pagos, correo, recuperación de clave por email, API REST pública, roles extra).

## Entregable final
Un proyecto que se ejecuta con `mvn spring-boot:run` después de crear la base de datos, con un README de instalación en español, usuarios de prueba y todas las pantallas funcionando.
