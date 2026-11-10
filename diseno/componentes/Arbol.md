Árbol animado de decoración para el protagonista de las pantallas de inicio: las ramas crecen, las hojas se mecen y las flores aparecen y desaparecen.

**Qué aporta quien lo usa:** un protagonista con la clase `hero` y el `svg.arbol` como hijo directo, al final del módulo. Todo el color sale de los tokens, así que no necesita imágenes.

- Úsalo en el protagonista de Inicio (visitante) y de Inicio del agricultor. No lo pongas en formularios, resultados ni administración.
- Pon un solo árbol por pantalla, abajo a la derecha. El texto del protagonista queda a la izquierda, con un máximo de 400px de ancho.
- Deja el `svg` con `aria-hidden="true"`: es decoración y no dice nada que el texto no diga.
- La animación es calmada y se repite sola. Con `prefers-reduced-motion` todo queda quieto y las flores se ven abiertas.
- No escribas texto encima del árbol y no cambies sus colores: ramas en `tinta` suave, hojas en `hoja-suave`, flores en `ocre-suave` y `arcilla-suave`.
