# 05 · Backend

## Proyecto
- Carpeta `backend/`, Maven, Java 17, Spring Boot 3.3.x (usa la última 3.3 estable).
- Dependencias: `spring-boot-starter-web`, `spring-boot-starter-thymeleaf`, `spring-boot-starter-security`, `thymeleaf-extras-springsecurity6`, `spring-boot-starter-validation`, `spring-boot-starter-jdbc`, `mysql-connector-j`, y para pruebas `spring-boot-starter-test` y `spring-security-test`.
- Grupo `co.bluerebhirt`, artefacto `bluerebhirt`.

## Estructura de paquetes (simple, una carpeta por tema)
```
co.bluerebhirt
├─ BlueRebhirtApplication.java
├─ config/        SeguridadConfig, DatosDemo
├─ usuario/       Usuario, UsuarioRepositorio, UsuarioServicio, RegistroForm, AuthControlador
├─ cultivo/       Cultivo, CultivoRepositorio, CultivoControlador
├─ medicion/      Medicion, MedicionRepositorio, MedicionForm, CalculoPh, MedicionControlador, HistorialControlador
├─ rango/         RangoPh, RangoRepositorio, RangoServicio, GuiaControlador
└─ admin/         AdminControlador
```
Comenta el código en español, con nombres claros. Prefiere clases pequeñas y métodos cortos. Los repositorios usan `JdbcTemplate` con SQL escrito a mano y parámetros (`?`).

## Configuración (`application.properties`)
```
spring.datasource.url=jdbc:mysql://localhost:3306/blue_rebhirt?useUnicode=true&characterEncoding=utf8&serverTimezone=America/Bogota
spring.datasource.username=${DB_USUARIO:root}
spring.datasource.password=${DB_CLAVE:}
spring.thymeleaf.cache=false
spring.web.locale=es_CO
app.nombre=Blue Rebhirt
app.datos-demo=true
server.error.include-stacktrace=never
```
La zona horaria de la aplicación es `America/Bogota`. Las fechas se muestran en español ("9 de noviembre", "lun 9 nov").

## Cálculo del estado (clase `CalculoPh`)
Entrada: pH ya validado y redondeado a 1 decimal. Salida: el `RangoPh` que lo contiene.
1. Carga los cinco rangos de la base de datos ordenados por `orden`.
2. Devuelve el único rango con `ph_desde <= ph <= ph_hasta`.
3. Si ninguno lo contiene (no debería pasar por la regla 4 de rangos), lanza un error claro de configuración.
4. La medición guarda `rango_id`, `estado` y `recomendacion` copiados de ese rango.

Los demás datos (nivel de agua, humedad, temperatura, hojas, abono, lluvia) **se guardan y se muestran, pero en esta versión no cambian el estado**; solo el pH lo decide. Si luego la profesora quiere reglas con ellos, se agregan en `CalculoPh` sin cambiar las pantallas.

Prueba unitaria obligatoria de `CalculoPh`: 0.0, 4.9, 5.0, 5.4, 5.5, 6.2, 7.0, 7.1, 7.5, 7.6, 14.0 → estados alerta, alerta, aviso, aviso, ok, ok, ok, aviso, aviso, alerta, alerta.

## Seguridad (`SeguridadConfig`)
- `formLogin` con página `/entrar`, parámetros `username` (el correo) y `password`; el formulario HTML usa `name="username"` y `name="password"`.
- `BCryptPasswordEncoder`.
- Rutas públicas: `/`, `/entrar`, `/registro`, `/guia`, `/css/**`, `/img/**`, `/error`.
- `/admin/**` solo `ROLE_ADMINISTRADOR`. Todo lo demás exige sesión (`ROLE_AGRICULTOR` o administrador según corresponda; el administrador no hace mediciones).
- CSRF activado (Thymeleaf lo añade a los `th:action`). Cierre de sesión por POST.
- Éxito de login: redirigir según rol (`/inicio` o `/admin`).
- Autorización por dueño: toda consulta de cultivos y mediciones incluye `usuario_id` del usuario en sesión. Un id ajeno responde 404.
- Un usuario con `activo = false` no puede entrar.

## Validación
Usa `@Valid` + clases `Form` con anotaciones y mensajes en español. Mensajes que ya existen en el diseño: "Un número entre 0 y 14.", "Escribe un valor entre 0 y 100.", "Mínimo 6 caracteres.". Muestra el error con el patrón `campo--error` + `campo__error` del diseño (ver `diseno/componentes/Campo.md`).

## Datos de prueba (`DatosDemo`)
`CommandLineRunner` activo con `app.datos-demo=true` que, si no hay usuarios, crea los usuarios y mediciones de `04-base-de-datos.md`.

## Pruebas mínimas
- `CalculoPh` (tabla de arriba).
- Validación de rangos del administrador (huecos, solapes, límites).
- Un agricultor no puede abrir la medición de otro (404).
- Un agricultor no entra a `/admin` (redirige).
- Registro con correo repetido muestra el error.

## README del proyecto
Escribe `backend/README.md` en español: requisitos (Java 17, Maven, MySQL 8), crear la base (`mysql -u root -p < ../db/schema.sql` y `seed.sql`), variables `DB_USUARIO`/`DB_CLAVE`, `mvn spring-boot:run`, usuarios de prueba, y cómo correr las pruebas.
