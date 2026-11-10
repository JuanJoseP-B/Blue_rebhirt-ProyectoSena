# 01 · Producto

## Contexto
Proyecto productivo de la Media Técnica (SENA/Comfandi) para productores de plátano de la vereda Bella Vista, La Cumbre, Valle del Cauca. Objetivos de Desarrollo Sostenible 6, 12 y 15. El pH del agua de riego afecta al cultivo; el agricultor necesita saber si el agua está bien y qué hacer con el abono.

## Alcance
Los sensores no se conectan a la página. El agricultor mide con su sensor y **escribe los valores en un formulario**: pH, nivel de agua, humedad del suelo y temperatura, más tres preguntas sobre el cultivo. El sistema guarda la medición, calcula el estado y muestra qué hacer.

## Roles
| Rol | Qué puede hacer |
| --- | --- |
| Visitante (sin sesión) | Ver el inicio público y la Guía de abonos, entrar o crear cuenta. |
| Agricultor | Crear sus cultivos, hacer mediciones, ver el resultado, su historial y la guía. |
| Administrador | Editar los rangos de pH y sus recomendaciones; ver usuarios, cultivos y mediciones recientes. |

Quien se registra siempre es agricultor. El administrador se crea desde la base de datos (usuario de prueba, ver `05-backend.md`).

## Recorrido del agricultor
1. Entra por el inicio público y pulsa "Crear cuenta" (o "Entrar").
2. Tras crear la cuenta, llega a **Mis cultivos** para crear su primer lote. Tras entrar con cuenta existente, llega a su **Inicio**.
3. Desde su Inicio, "Nueva medición" es lo principal. Llena el formulario y pulsa "Calcular".
4. Ve el **Resultado**: estado, escala de 0 a 14, qué hacer y los datos guardados.
5. Desde el resultado: "Hacer otra medición" o "Ver historial".

## Escala del pH (0 a 14, a temperatura estándar)
- Ácido: pH menor que 7.
- Neutro: pH igual a 7.
- Alcalino o básico: pH mayor que 7.
- Para el plátano, la franja ideal de ejemplo es 5.5 a 7.0. La escala visual usa tres tramos: ácido hasta 5.5 (arcilla), ideal de 5.5 a 7.0 (hoja), alcalino desde 7.0 (ocre).

## Valores de ejemplo que debe validar la profesora o el técnico
Estos números y textos son puntos de partida, no datos oficiales. Por eso viven en la base de datos y el administrador puede cambiarlos:
- Rango ideal 5.5–7.0 y los umbrales de aviso (5.0–5.4 y 7.1–7.5).
- Las recomendaciones y los pasos de "Qué hacer".
- Las preguntas del formulario y sus opciones.
