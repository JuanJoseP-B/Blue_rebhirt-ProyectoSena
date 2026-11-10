Módulo de pantalla en tres niveles (protagonista, apoyo y enlace) que ordena qué mirar primero.

**Qué aporta quien lo usa:** el título, un texto corto y, solo en el protagonista, un botón. El nivel se elige con la clase: `modulo--protagonista`, `modulo--apoyo` o `modulo--enlace`.

- Pon un único `modulo--protagonista` por pantalla. Va a la izquierda, con relleno `hoja`, el título en `titulo-lg` y un botón `btn--inverso`.
- Pon uno o dos `modulo--apoyo` a su derecha, apilados y más bajos. Llevan `etiqueta`, un `titulo-md` y un dato corto.
- Pon el resto como `modulo--enlace` en `panel__enlaces`, debajo de todo. Son baldosas bajas de color suave (hoja, ocre y agua, en ese orden) con trama de rayas finas y una flecha en un círculo. No llevan sombra hasta pasar el cursor.
- Compón la pantalla con `.panel`: 7 columnas para el protagonista y 5 para el apoyo. En pantallas angostas pasa a una columna.
- No pongas tres módulos del mismo tamaño en una fila. Si lo hiciste, convierte uno en protagonista y los demás en apoyo o enlace.
- Cuando el protagonista es un formulario largo, usa `modulo--formulario`: cabecera `hoja` con `modulo__cabecera` y cuerpo `superficie` con `modulo__formulario`. Así el verde marca el inicio sin llenar toda la pantalla.
- No metas un módulo dentro de otro.
