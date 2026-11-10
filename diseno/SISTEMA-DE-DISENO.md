Blue Rebhirt es una página para productores de plátano de Bella Vista, La Cumbre. Se abre, se mide el pH del agua y se sabe qué hacer. El diseño es claro, de colores de tierra y con una sola cosa importante por pantalla.

Cada regla de abajo nombra un token de `tokens.json` o una clase de `components/bundle.css`. Cárgalos así: enlaza `bundle.css` después de `tokens.css` y añade la fuente de Google Fonts que `bundle.css` ya importa.

## Principios

- Pon un solo protagonista por pantalla. Quien abre la página debe saber por dónde empezar sin leer todo.
- Escribe como habla un productor, no como un manual. Usa palabras de finca: agua, cultivo, abono.
- Mantén el aspecto de proyecto de colegio bien hecho: formas simples, un radio por rol, sin degradados, sin ilustraciones recargadas, sin emojis.
- No agregues nada que el usuario no necesite en ese momento. Cada elemento extra le quita atención al protagonista.

## Color

- Pinta la página con `fondo` (arena) y las tarjetas con `superficie`. Usa `hundido` solo para zonas hundidas como la unidad de un campo.
- Usa `hoja` como único color de marca: el protagonista, el botón primario y la marca del menú. No lo repartas por toda la pantalla.
- Usa `arcilla` solo para alertas y errores. Usa `ocre` solo para avisos y `agua` para información, foco y el nivel de agua.
- Pon texto en `tinta`. Para texto secundario usa `tinta-suave`, nunca sobre `hundido`.
- Sobre `hoja`, `arcilla` y `agua` escribe en `sobre-color`. Sobre `ocre` escribe en `tinta`.
- Cuando un color signifique un estado, añade siempre una palabra y un icono. El color nunca va solo: `hoja` y `arcilla` no se distinguen bien para todos.

## Tipografía

- Usa dos familias con roles fijos. `display` (Fraunces) para títulos y cifras. `text` (Source Sans 3) para todo lo demás.
- Usa `.enfasis` dentro de títulos para una palabra o frase corta en cursiva; no la uses en párrafos, controles, estados ni cifras.
- Pon el título de cada módulo en `titulo-lg` y el de la página de inicio en `titulo-xl`. Los títulos de apoyo van en `titulo-md`.
- Escribe el valor del pH en `valor-ph`. No uses esa cifra grande para nada más.
- Escribe el cuerpo en `cuerpo` (17px, nunca menos para texto que debe leerse) y las ayudas en `apoyo`.
- Usa `etiqueta` en mayúsculas para un rótulo corto sobre un módulo, como "PASO 1".

## Espacio y forma

- Separa módulos con `space-6` y bloques grandes con `space-12`.
- Da a botones y campos `alto-control` (48px) para que se toquen bien en el celular.
- Redondea botones y campos con `radius-md`. Redondea módulos, resultado y acceso con `radius-lg`.
- Usa `sombra-suave` solo en la tarjeta de acceso. Separa los demás módulos con color y `borde`.

## Jerarquía de componentes

Cada pantalla reparte sus componentes en tres niveles. Cada nivel tiene un tamaño y un peso distintos, y eso es lo que evita que todo parezca igual.

| Nivel | Cuántos por pantalla | Cómo se ve | Componente |
| --- | --- | --- | --- |
| 1. Protagonista | Exactamente uno | Relleno `hoja`, el más grande, ocupa 7 de 12 columnas | `Modulo` protagonista |
| 2. Apoyo | Uno o dos | `superficie` con `borde`, más pequeño, 5 de 12 columnas | `Modulo` apoyo |
| 3. Enlace | Los que hagan falta | Baldosa baja de color suave con trama y flecha, sin sombra | `Modulo` enlace |

- Pon el botón primario solo dentro del protagonista. Un botón `btn--primario` aparece una sola vez por pantalla.
- Pon en el protagonista lo que el usuario vino a hacer. Lo demás baja de nivel.
- Usa `ResultadoPh` y `EscalaPh` como contenido de un módulo, nunca como módulos sueltos.

## Composición de pantallas

- Arma el panel con `.panel`: el protagonista a la izquierda ocupa dos filas y los módulos de apoyo se apilan a su derecha, más bajos que él. Los enlaces van debajo, en una sola línea.
- No repitas módulos del mismo tamaño en filas iguales. Si dos módulos miden lo mismo, uno de los dos sobra o es un enlace.
- En el celular el panel pasa a una columna con el orden: protagonista, apoyo, enlaces.
- Pon máximo cuatro opciones en el menú. Ponle a quien tiene sesión un solo botón destacado, "Nueva medición".

## Acceso

- Pon en la tarjeta de acceso solo: título, campos, un botón y un enlace al otro formulario. Entrar lleva correo y contraseña. Registrarse suma nombre.
- Deja fuera de la tarjeta: logo grande, ilustración, "recordarme", botones de redes sociales, texto de bienvenida y términos largos.
- Centra la tarjeta (`.acceso`, 400px) sobre `fondo`. El nombre del proyecto vive en el menú, no dentro de la tarjeta.
- Muestra un error junto al campo con `campo--error` y una frase que diga cómo arreglarlo: "Falta el correo."

## Voz y textos

- Usa mayúscula solo al inicio de la frase. Nombra los botones con el verbo de lo que hacen: "Entrar", "Crear cuenta", "Calcular", "Guardar medición".
- Escribe el resultado con la acción primero. Ejemplos reales: "El pH está en rango. Sigue con tu riego normal." / "El pH está bajo (agua ácida). Revisa el abono y vuelve a medir mañana."
- Evita las siglas sin explicar. Escribe "pH del agua", "nivel de agua", "humedad del suelo".
- Usa los mismos nombres en toda la página: "Mis cultivos", "Nueva medición", "Historial", "Guía de abonos".

## Iconografía

- No hay set de iconos. Usa los signos de texto ✓ (en rango), ! (revisar), ▲ (fuera de rango) y → (enlace), siempre dentro de un círculo de color o junto a una palabra.
- Usa el logo del grupo `Logos`: `logo-completo.png` (emblema y nombre) en el inicio y `logo-emblema.png` (solo el círculo) en el menú, a 40px. Mira `assets/Logos/README.md` para las reglas. No dibujes otro logo.
- Escribe el nombre "Blue Rebhirt" en `display` peso 800 con `hoja-oscuro` junto al emblema del menú.

## Accesibilidad

- Mantén contraste de 4.5:1 en todo el texto y 3:1 en bordes de campos y anillos de foco.
- Deja visible el anillo de foco (`foco`, 3px) en todo lo que se pueda tocar.
- Haz cada estado legible sin color: palabra + icono + color.
