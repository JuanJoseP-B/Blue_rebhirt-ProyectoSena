# Estado de desarrollo

- Base de datos: esquema y cinco rangos verificados con MySQL 26.7 local; MySQL 8 sigue pendiente de verificación. En una base separada y vacía se comprobó la creación de dos usuarios, dos cultivos y nueve mediciones de demostración.
- Acceso y cultivos: registro, correo y contraseña, BCrypt, cierre de sesión, roles, creación/listado y aislamiento entre usuarios comprobados manualmente.
- Mediciones: formulario, validación, cálculo desde la base, guardado y resultado. Los once límites exigidos se probaron desde la interfaz. Una medición ajena devuelve 404 y se rechazó un cultivo ajeno al guardar.
- Historial y guía: muestran datos reales y rangos de la base. Se abrieron 28 combinaciones de ruta y ancho (1280 y 390 px) sin errores HTTP ni desbordamiento horizontal detectado en títulos. Queda pendiente la comparación visual completa con todas las referencias.
- Administración: rangos validados y guardados, listados y cambio de contraseña temporal. Se comprobó el rechazo de un hueco y la actualización de una recomendación en la guía; se comprobó también el cambio de contraseña temporal y el acceso posterior de la cuenta de prueba.
- Maven: `mvn test` sigue fallando en este entorno. Java informa `AccessDeniedException` al resolver rutas y no compila las pruebas; el servidor se inició con las pruebas omitidas después de una compilación parcial. No se considera verificación automatizada.
- Git: no se pudieron crear ramas ni commits porque la sesión deniega escrituras en .git. No hay cambios subidos.
- Pendientes: resolver la compilación limpia y ejecutar todas las pruebas, verificar MySQL 8, revisar visualmente todas las pantallas, completar detalles de navegación y documentación, y crear las ramas y commits solicitados.
- Administración: se añadió al código el último estado de cada cultivo y el estado con icono en las mediciones recientes; falta probar estos cambios en ejecución porque la compilación sigue bloqueada por permisos.
