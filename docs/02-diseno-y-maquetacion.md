# 02 · Diseño y maquetación

Objetivo: las páginas finales deben verse **igual** que las de `referencia/`.

## Qué hay en `diseno/`
- `css/tokens.css`: colores, espacios, radios, tipografías y estilos de texto (variables CSS). Es la fuente de verdad.
- `css/componentes.css`: botones, campos, acceso, módulos, resultado, escala, menú, árbol animado, baldosas de enlace. Importa las tipografías de Google Fonts.
- `css/pantallas.css`: maquetación de páginas (contenedor, saludo, listas, estados, pasos, gráfica, filas de rangos, muestras).
- `tokens.json`: los mismos tokens en formato de datos.
- `SISTEMA-DE-DISENO.md` y `componentes/*.md`: reglas de uso de cada componente. **Léelos antes de maquetar.**
- `MAPA-DE-PANTALLAS.md`: qué es protagonista, apoyo y enlace en cada pantalla, y el menú por rol.
- `assets/`: `logo-completo.png` y `logo-emblema.png` (fondo transparente) y `LOGO.md` con sus reglas.
- `pantallas/*.html`: las 11 pantallas completas y estáticas.

## Cómo copiarlo al proyecto
1. Copia `diseno/css/*.css` a `backend/src/main/resources/static/css/` **sin editarlos**.
2. Copia `diseno/assets/*.png` a `backend/src/main/resources/static/img/`.
3. En las plantillas, cambia las rutas: `../css/x.css` → `/css/x.css`, `../assets/logo-*.png` → `/img/logo-*.png`.
4. Si hace falta un estilo nuevo, agrégalo en un archivo aparte `static/css/app.css` cargado **después** de los tres anteriores, usando solo variables de `tokens.css`. No uses colores hexadecimales sueltos.

## Pantallas
| Archivo | Ruta | Rol |
| --- | --- | --- |
| `index.html` | `/` | Visitante (si hay sesión, redirige a su inicio) |
| `entrar.html` | `/entrar` | Visitante |
| `registro.html` | `/registro` | Visitante |
| `panel.html` | `/inicio` | Agricultor |
| `medicion.html` | `/mediciones/nueva` | Agricultor |
| `resultado.html` | `/mediciones/{id}` | Agricultor (dueño) |
| `cultivos.html` | `/cultivos` | Agricultor |
| `historial.html` | `/historial` | Agricultor |
| `guia.html` | `/guia` | Todos |
| `admin.html` | `/admin` | Administrador |
| `elementos.html` | no se publica | Solo referencia visual de todos los componentes |

## Reglas de diseño que debes respetar
- Tono claro con colores de tierra. Dos tipografías: **Fraunces** (títulos y cifras) y **Source Sans 3** (texto).
- Jerarquía: cada pantalla tiene **un protagonista** (nivel 1), módulos de apoyo (nivel 2) y enlaces (nivel 3). No apiles módulos iguales en filas iguales.
- El login queda mínimo: título, correo, contraseña, un botón primario y un enlace a crear cuenta. Sin logos, textos de marketing ni "olvidé mi clave".
- Estados siempre con **color + icono + palabra**: `✓ En rango` (hoja), `! Revisar` (ocre), `▲ Fuera de rango` (arcilla). Nunca solo color.
- Controles de al menos 48 px de alto. Foco visible. Respeta `prefers-reduced-motion`.
- El árbol animado vive solo en el protagonista verde del Inicio del agricultor y del inicio público; no lo copies a otras pantallas.
- El logo completo va en el inicio público dentro de una tarjeta de apoyo; el emblema va en el menú. Nunca sobre fondo verde.

## Del HTML estático a plantilla Thymeleaf
- Mantén exactamente la estructura y las clases. Cambia solo lo que es dato (`th:text`, `th:each`, `th:href`, `th:action`) y los enlaces.
- Haz un **fragmento de menú** (`fragments/menu.html`) con tres variantes según el rol (tabla en `MAPA-DE-PANTALLAS.md`) y marca la página actual con `aria-current="page"`.
- El estado de un resultado define la clase: `resultado--ok | resultado--aviso | resultado--alerta` y `estado--ok | estado--aviso | estado--alerta`, con su icono (`✓`, `!`, `▲`).
- La escala del pH: la marca se coloca con `left: <ph / 14 * 100>%`. Los tramos fijos y los números 0, 5.5, 7 y 14 salen del diseño; si el administrador cambia los rangos, los números de la escala deben leerse de `rangos_ph` (límite ideal inferior y superior) y los anchos de tramo recalcularse con la misma fórmula (`ancho = (hasta - desde) / 14 * 100`).
- La gráfica del historial es un SVG `viewBox="0 0 660 310"`. Área de dibujo x de 48 a 640, y de 20 a 260. Eje y de pH 4 a 9 (`y = 260 - (ph - 4) * 48`, limita el pH a ese intervalo para dibujar). Hasta 8 puntos, espaciados desde x = 64 hasta x = 600 (`x = 64 + i * 536 / (n - 1)`). La banda ideal es el rectángulo entre los pH ideales de la base de datos. Cada punto usa `g-ok`, `g-aviso` o `g-alerta` según el estado guardado. Genera el SVG en la plantilla con los datos reales; no uses librerías de gráficos.
- Los selects de cultivo se llenan con los cultivos del usuario.
- Los formularios usan `method="post"` y botón `type="submit"` con la misma clase `btn btn--primario`.
