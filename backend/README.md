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

## Pruebas

```text
mvn test
```

Las pruebas incluyen los límites del cálculo de pH.

Para detener la instancia MySQL aislada creada durante el desarrollo local, ejecuta `mysqladmin --protocol=tcp -h 127.0.0.1 -u root shutdown`. Esta instancia de prueba usa una cuenta sin contraseña y solo debe utilizarse localmente. No es una instalación para producción.

## Cuentas de prueba

Con `app.datos-demo=true` y la tabla `usuarios` vacía se crean `ana@bluerebhirt.test` / `ana123` y `admin@bluerebhirt.test` / `admin123`. Son únicamente para desarrollo. Los datos de demostración incluyen dos cultivos y nueve mediciones.

Nota de verificación: en este entorno Java devuelve `AccessDeniedException` al compilar, por lo que las pruebas automatizadas no se han podido completar. Las pruebas manuales realizadas se detallan en `odd/tasks/estado-desarrollo.md`.
