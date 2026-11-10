Barra del pH de 0 a 14 con tres tramos (ácido, ideal, alcalino), números de referencia, leyenda y una marca en el valor medido.

**Qué aporta quien lo usa:** el valor medido para colocar la marca. Su posición en porcentaje es valor ÷ 14 × 100, y se escribe en `style="left: …%"` de `escala__marca`. Los límites de los tramos vienen de la tabla de rangos de la base de datos.

- Muestra siempre la escala completa de 0 a 14: menos de 7 es ácido, 7 es neutro y más de 7 es alcalino. El tramo ideal para el plátano (5.5 a 7.0) es de ejemplo hasta que el profesor lo confirme.
- Pon la marca con el número encima. El número repite el valor de `valor-ph`, así que la escala se entiende sin leer el color.
- Rotula los números 0, 5.5, 7 y 14 en `escala__numeros` y describe cada tramo con palabras en `escala__leyenda`. El color nunca va solo.
- Úsala debajo de `ResultadoPh` en la pantalla de Resultado, y en la Guía de abonos sin marca.
- No la pongas en el panel de inicio ni la repitas por cada medición del historial.
