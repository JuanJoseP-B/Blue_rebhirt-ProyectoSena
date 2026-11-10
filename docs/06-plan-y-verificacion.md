# 06 · Plan por pasos y verificación

Haz un paso, verifica, y recién entonces sigue. Haz commits pequeños (uno por paso o por pantalla).

## Pasos
1. **Base**: proyecto Spring Boot arrancando, conexión a MySQL, `schema.sql` y `seed.sql` ejecutados. Verifica: `SELECT * FROM rangos_ph` devuelve 5 filas.
2. **Estáticos**: CSS e imágenes en `static/`, y una plantilla de prueba que cargue el diseño. Verifica: se ve igual que `referencia/index-escritorio.png`.
3. **Seguridad y cuentas**: registro, entrada, salida, roles y redirecciones. Verifica con los usuarios de prueba.
4. **Menú por rol**: fragmento con las tres variantes y `aria-current`.
5. **Cultivos**: crear y listar.
6. **Cálculo y medición**: `CalculoPh` + pruebas, formulario, guardado, resultado.
7. **Historial**: lista, resumen y gráfica SVG con datos reales.
8. **Guía**: contenido leído de `rangos_ph`.
9. **Administrador**: edición de rangos con validación, usuarios, cultivos, mediciones recientes, contraseña temporal.
10. **Cierre**: datos de demostración, README, revisión de seguridad, revisión visual completa.

## Revisión visual (obligatoria)
Para cada pantalla de `referencia/`, abre la página real con los usuarios de prueba, a 1280 px y a 390 px de ancho, y compara con la imagen. Corrige diferencias de estructura, jerarquía, color, tipografía y espacios. No se acepta una página con apariencia distinta.

## Lista de aceptación
- [ ] Todas las pantallas del diseño existen y se ven como `referencia/` (escritorio y móvil).
- [ ] Todo el texto en español, en "tú".
- [ ] Un solo `btn--primario` por pantalla (salvo el botón de menú "Nueva medición", que cuenta como el primario de las páginas donde aparece y se omite en esa página si ya hay otro).
- [ ] Estados con color + icono + palabra.
- [ ] Se puede crear cuenta, entrar, crear cultivo, medir, ver resultado e historial.
- [ ] Cambiar un rango como administrador cambia el cálculo de las mediciones nuevas y la Guía, y **no** cambia las mediciones antiguas.
- [ ] Un agricultor no ve datos de otro; no entra a `/admin`.
- [ ] Contraseñas en BCrypt; ninguna consulta concatena texto del usuario.
- [ ] No hay valores de rangos fijos en el código Java ni en las plantillas.
- [ ] `mvn test` pasa.
- [ ] El proyecto arranca desde cero siguiendo solo el README.

## Preguntas abiertas (déjalas como constantes/configuración, no las decidas por tu cuenta)
- Nombre definitivo: "Blue Rebirth" (logo) o "Blue Rebhirt" (textos). Cambia en un solo lugar: `app.nombre`.
- Rangos, umbrales, recomendaciones y preguntas del formulario: valores de ejemplo, a validar con la profesora o el técnico.
