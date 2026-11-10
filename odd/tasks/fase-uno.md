# Fase uno: base del proyecto

Objetivo: preparar Spring Boot y la base de datos MySQL siguiendo los archivos originales del proyecto.

Ruta: implementación directa por petición del usuario. Estimación inferior a 400 líneas; entrega en una rama de funcionalidad. El espejo Engram está pendiente de disponibilidad.

- [x] F1-1 Crear el proyecto y configurar las dependencias y la conexión.
- [x] F1-2 Arrancar la aplicación y cargar el esquema y los datos iniciales en una base aislada.
- [x] F1-3 Comprobar las cuatro tablas y los cinco rangos iniciales.

Verificación: `mvn test` terminó correctamente sin pruebas todavía; `mvn spring-boot:run` inició el servidor; MySQL 26.7 local confirmó cuatro tablas y cinco rangos. MySQL 8 sigue siendo el motor objetivo y no se verificó en esta máquina. El servicio instalado no pudo iniciarse por permisos, por lo que se utilizó una instancia aislada local.
La conexión JDBC también devolvió cinco rangos. La instancia aislada usa root sin contraseña y escucha solo en localhost; es únicamente para pruebas locales.
