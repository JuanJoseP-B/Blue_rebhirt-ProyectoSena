# Blue Rebhirt

Blue Rebhirt es una aplicación web para agricultores de plátano de Bella Vista, La Cumbre, Valle del Cauca. Ayuda a registrar el pH del agua de riego y otros datos del cultivo, consultar su evolución y recibir una recomendación según los rangos configurados por un administrador.

El proyecto fue desarrollado como trabajo escolar de Media Técnica SENA/Comfandi. Los rangos y las recomendaciones incluidos son ejemplos que deben ser revisados por la profesora o un técnico antes de utilizarlos para tomar decisiones sobre el cultivo.

## Requisitos

- Git.
- Java JDK 17.
- Maven 3.6 o superior.
- MySQL 8 ejecutándose en `localhost:3306` y un usuario con permiso para crear la base de datos.
- Conexión a internet para descargar las dependencias de Maven la primera vez.

Puedes comprobar las herramientas con `git --version`, `java -version`, `mvn -version` y `mysql --version`.

## Clonar el repositorio

En una terminal, ejecuta:

```bash
git clone https://github.com/JuanJoseP-B/Blue_rebhirt-ProyectoSena.git
cd Blue_rebhirt-ProyectoSena
```

## Crear la base de datos

Inicia MySQL y ejecuta estos comandos desde la carpeta raíz del repositorio, en **Símbolo del sistema de Windows (cmd)** o una terminal compatible con redirección de entrada:

```bash
mysql -u root -p < db/schema.sql
mysql -u root -p < db/seed.sql
```

Escribe la contraseña de MySQL cuando se solicite. Si usas otro usuario, reemplaza `root` por su nombre. Los scripts crean la base `blue_rebhirt`, sus tablas y cinco rangos de pH iniciales.

**Atención:** `schema.sql` elimina y vuelve a crear las tablas del proyecto. Ejecútalo solo para una instalación nueva o cuando quieras borrar sus datos existentes.

Para comprobar los rangos:

```bash
mysql -u root -p -e "SELECT zona, ph_desde, ph_hasta FROM blue_rebhirt.rangos_ph ORDER BY orden;"
```

La consulta debe mostrar cinco filas.

## Instalar dependencias y ejecutar

Entra en la carpeta del servidor:

```bash
cd backend
```

Por defecto, la aplicación se conecta a MySQL con el usuario `root` y contraseña vacía. Si tu instalación usa otros datos, configura las variables de entorno antes de arrancar. En PowerShell:

```powershell
$env:DB_USUARIO = "root"
$env:DB_CLAVE = "tu-contraseña-de-mysql"
```

En Símbolo del sistema de Windows (`cmd`):

```cmd
set DB_USUARIO=root
set DB_CLAVE=tu-contraseña-de-mysql
```

En macOS o Linux:

```bash
export DB_USUARIO=root
export DB_CLAVE='tu-contraseña-de-mysql'
```

En la misma terminal, ejecuta:

```bash
mvn spring-boot:run
```

Maven descarga automáticamente las dependencias declaradas en `backend/pom.xml`, compila el proyecto e inicia el servidor. Cuando termine de arrancar, abre [http://localhost:8080](http://localhost:8080). Para detenerlo, pulsa `Ctrl+C` en la terminal.

## Usuarios y contraseñas de prueba

En el primer arranque, si la tabla `usuarios` está vacía y `app.datos-demo=true`, la aplicación crea estas cuentas:

| Rol | Usuario (correo) | Contraseña |
| --- | --- | --- |
| Administrador | `admin@bluerebhirt.test` | `admin123` |
| Agricultor | `ana@bluerebhirt.test` | `ana123` |

En la pantalla de acceso, selecciona primero el rol correspondiente y después escribe el correo y la contraseña. Las cuentas de ejemplo se crean únicamente cuando no existen usuarios; si la base ya contiene cuentas, el arranque no las reemplaza ni restablece sus contraseñas. Al registrarte desde la página se crea una cuenta de agricultor.

## Cómo funciona

1. **Agricultor:** entra o crea una cuenta y registra uno o más cultivos.
2. Mide los datos con sus propios instrumentos y escribe el pH, el nivel de agua, la humedad del suelo y la temperatura. Después responde tres preguntas sobre el cultivo. Los sensores no se conectan a la aplicación.
3. La aplicación guarda la medición y compara el pH con los rangos de la base de datos. Muestra el estado —en rango, revisar o fuera de rango— y una recomendación. Los demás datos se guardan, pero no cambian el cálculo del estado.
4. En **Historial**, el agricultor consulta la gráfica y el resumen de sus mediciones. El botón **Ver datos demo** muestra una serie de ejemplo para explorar la evolución a lo largo del tiempo; no modifica las mediciones reales. Para volver, pulsa **Ver mis datos**.
5. **Administrador:** entra con su cuenta para editar los cinco rangos y sus recomendaciones, consultar usuarios, cultivos y mediciones recientes, y asignar una contraseña temporal a un agricultor. Los cambios de rangos se aplican a las mediciones nuevas; las anteriores conservan su estado y recomendación guardados.

La guía de abonos también está disponible desde la página pública. Los agricultores solo pueden consultar sus propios cultivos y mediciones.

## Ejecutar las pruebas

Desde la carpeta `backend`:

```bash
mvn test
```

La aplicación usa Java, Spring Boot, Thymeleaf, Spring Security, JDBC y MySQL. El diseño y los requisitos están en `diseno/` y `docs/`. Puedes consultar el estado de las verificaciones en [odd/tasks/estado-desarrollo.md](odd/tasks/estado-desarrollo.md).

La preparación para alojar la aplicación en Render con PostgreSQL de Supabase está documentada en [docs/09-despliegue-render-supabase.md](docs/09-despliegue-render-supabase.md). El despliegue y su prueba completa siguen pendientes; las instrucciones locales anteriores siguen usando MySQL.
