# Blue Rebhirt: backend

La aplicación incluye registro y acceso, cultivos, mediciones, historial, guía y administración de rangos.

## Requisitos

- Java 17
- Maven
- MySQL 8 en `localhost:3306`

## Preparar la base de datos

Los siguientes comandos deben ejecutarse desde la carpeta `backend`. **`schema.sql` elimina las tablas existentes del proyecto y sus datos**: úsalo solo con una base nueva o vacía.

En una terminal compatible con redirección de entrada, como `cmd`:

```cmd
mysql -u root -p < ..\db\schema.sql
mysql -u root -p < ..\db\seed.sql
mysql -u root -p -e "SELECT * FROM blue_rebhirt.rangos_ph;"
```

La última consulta debe mostrar cinco filas. Configura `DB_USUARIO` y `DB_CLAVE` si tu usuario o contraseña son distintos del usuario `root` sin contraseña usado por defecto. En PowerShell, por ejemplo:

```powershell
$env:DB_USUARIO = "root"
$env:DB_CLAVE = "tu-contraseña"
mvn spring-boot:run
```

La aplicación escucha en `http://localhost:8080`.

Para una instalación alojada, `DB_URL` permite indicar la URL JDBC de la base de datos y `PORT` el puerto asignado por el proveedor. `DB_USUARIO` y `DB_CLAVE` configuran las credenciales; `DATOS_DEMO=false` evita crear automáticamente cuentas y mediciones de ejemplo. Las credenciales deben configurarse en el proveedor, no escribirse en el repositorio. La URL JDBC y el esquema deben corresponder al motor soportado por la aplicación: actualmente MySQL.

Al entrar, selecciona primero agricultor o administrador y después escribe las credenciales correspondientes. El rol se comprueba contra el guardado en la cuenta. En el historial, el botón «Ver datos demo» muestra una serie de ejemplo calculada con los rangos actuales; no crea ni modifica mediciones. Usa «Ver mis datos» para volver al historial real.

## Pruebas

```text
mvn test
```

Las pruebas incluyen los límites del cálculo de pH y el rechazo de rangos inválidos.

## Cuentas de prueba

Con `app.datos-demo=true` y la tabla `usuarios` vacía se crean `ana@bluerebhirt.test` / `ana123` y `admin@bluerebhirt.test` / `admin123`. Son únicamente para desarrollo. Los datos de demostración incluyen dos cultivos y nueve mediciones.

El estado de las verificaciones manuales y los pendientes se detalla en `odd/tasks/estado-desarrollo.md`.
