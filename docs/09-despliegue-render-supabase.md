# Despliegue con Render y Supabase

Esta configuración conserva Spring Boot y usa PostgreSQL en Supabase. Los cambios locales deben publicarse en el repositorio que Render vaya a conectar. El despliegue aún debe verificarse con una conexión real a la base y mediante los recorridos completos.

## Base de datos

El proyecto Supabase `blue-rebhirt` ya fue creado y se ejecutó `db/supabase-inicial.sql` en él. El script es para una base nueva: no debe volver a ejecutarse sobre ese proyecto. Los datos de MySQL local no se han copiado.

Las tablas están en el esquema `bluerebhirt`, separado del esquema público. En la conexión JDBC es necesario indicar `currentSchema=bluerebhirt`. El servidor debe usar una conexión privada y cifrada; no añadas la contraseña, la URL de conexión con contraseña ni otras claves al repositorio.

## Aplicación en Render

El archivo `render.yaml` describe un servicio web gratuito con Docker y solicita estos valores en la configuración privada de Render:

| Variable | Contenido |
| --- | --- |
| `DB_URL` | URL JDBC de PostgreSQL en modo **Session pooler** de Supabase, con `sslmode=require` y `currentSchema=bluerebhirt`. Ejemplo de formato: `jdbc:postgresql://HOST:5432/postgres?sslmode=require&currentSchema=bluerebhirt`. Obtén el host exacto desde **Connect** en Supabase. |
| `DB_USUARIO` | Usuario del Session pooler indicado por Supabase, con la forma `postgres.PROJECT_REF`. |
| `DB_CLAVE` | Contraseña de la base de datos, configurada de forma privada en Render. |
| `DATOS_DEMO` | `false`, ya configurado en `render.yaml`. |
| `APP_ADMIN_INICIAL_CORREO` | Correo elegido para el administrador inicial. |
| `APP_ADMIN_INICIAL_CLAVE` | Contraseña privada elegida para el administrador inicial, de al menos 12 caracteres y máximo 72 bytes UTF-8. |

La aplicación crea el administrador inicial con BCrypt solo cuando la tabla de usuarios está vacía. Tras comprobar que puede entrar, elimina las dos variables `APP_ADMIN_INICIAL_*` del servicio. Las cuentas de prueba públicas del README no se crean con `DATOS_DEMO=false`. El botón de datos demo del historial sigue disponible sin guardar datos de prueba.

Render suministra `PORT` automáticamente. La aplicación lo utiliza y, en local, sigue usando el puerto 8080 y MySQL de forma predeterminada.

## Verificación pendiente

Comprueba el inicio de la aplicación, los roles, el aislamiento entre usuarios, la creación de cultivos y mediciones, las recomendaciones, el historial y los cambios de rangos. Verifica la persistencia después de reiniciar y revisa la seguridad antes de compartir el enlace. Render Free puede detenerse tras 15 minutos de inactividad y tardar alrededor de un minuto en responder de nuevo; Supabase Free puede pausar proyectos con poca actividad.
