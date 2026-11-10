Campo de formulario con etiqueta arriba, ayuda opcional, unidad opcional y estado de error.

**Qué aporta quien lo usa:** un `id` único, una etiqueta visible (nunca solo un placeholder) y el `type` correcto del input (`email`, `password`, `number`).

- Pon la etiqueta siempre sobre el campo, en `campo__etiqueta`. Un placeholder no la reemplaza.
- Para valores de sensores (pH, humedad, temperatura) usa `campo__control--unidad` con `campo__unidad` ("pH", "%", "°C"), para que el usuario sepa qué unidad escribir.
- Para elegir entre cultivos usa un `select` con la clase `campo__input`.
- En el error, añade `campo--error` y una frase en `campo__error` que diga qué arreglar: "Escribe un pH entre 0 y 14."
- No pongas ayudas largas. Una línea en `campo__ayuda` o nada.
