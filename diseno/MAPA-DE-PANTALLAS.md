# Pantallas y foco

Cada pantalla tiene un protagonista, que es lo primero que el usuario hace allí. El resto baja de nivel. Los componentes están descritos en el README.

## Menú por rol

| Rol | Opciones del menú | Botón destacado |
| --- | --- | --- |
| Visitante | Inicio, Guía de abonos | Entrar |
| Agricultor | Inicio, Mis cultivos, Historial, Guía de abonos | Nueva medición |
| Administrador | Panel, Usuarios, Cultivos, Rangos | ninguno |

## Mapa de foco

| Pantalla | Protagonista (nivel 1) | Apoyo (nivel 2) | Enlaces (nivel 3) |
| --- | --- | --- | --- |
| Inicio público | "Mide el pH del agua de tus plataneras" con el botón Entrar | Qué es Blue Rebhirt (misión en dos líneas) | Crear cuenta, Guía de abonos |
| Entrar | La tarjeta de acceso | ninguno | Crear cuenta |
| Crear cuenta | La tarjeta de registro | ninguno | Ya tengo cuenta |
| Inicio del agricultor | Nueva medición | Última medición (`ResultadoPh` compacto), Mis cultivos | Historial, Guía de abonos |
| Nueva medición | El formulario: cultivo, pH, nivel de agua, humedad, temperatura y preguntas | Cómo medir bien | ninguno |
| Resultado | `ResultadoPh` grande con `EscalaPh` | Qué hacer ahora | Hacer otra medición, Ver historial |
| Historial | La gráfica del pH en el tiempo | Últimas mediciones | Filtrar por cultivo |
| Guía de abonos | Qué hacer según el pH | Rangos de pH del plátano | Volver al inicio |
| Panel del administrador | Rangos y recomendaciones | Usuarios, Cultivos | Mediciones recientes |

## Reglas de recorrido

- Lleva al visitante nuevo de Inicio a Crear cuenta a Mis cultivos. Si no tiene cultivos, el protagonista es "Crea tu primer cultivo".
- Lleva al agricultor con sesión directo a su inicio, con "Nueva medición" como protagonista.
- Lleva al administrador directo a su panel. Si un agricultor abre una pantalla de administrador, devuélvelo a su inicio.
- Termina cada acción en una pantalla donde el siguiente paso sea obvio: tras Calcular, el Resultado; tras el Resultado, otra medición o el Historial.
