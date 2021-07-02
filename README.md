# Power_Systems_App

Una app pensada para el cálculo de elementos del sistema de potencia, eventos de falla y flujos de potencia ( la app aún sigue en construcción y el objetivo es agregar más 
funcionalidades.

Elementos dibujables y editables: Barras, Lineas, Transformadores, Cargas, Bancos, Compensadores Estáticos y Generadores.

### Cálculo de Fallas. (N barras)
Se puede seleccionar entre los siguientes tipos de fallas: falla trifásica, monofásica, línea a tierra y línea a línea.
La Falla se puede ubicar ya sea en las barras o en la linea. 
Como resultado se obtiene la corriente en el punto de falla, la corriente que fluye por cada una de las líneas y la contribución de cada una de las máquinas rotatorias a la falla.

### Flujo de potencia. (N barras)
Solución al problema de flujos potencia por alguno de los dos métodos hasta ahora implementados: Gauss-Seidel o Newton-Raphson
Se obtiene como resultado voltajes, angulos en cada una de las barras y potencias en slack-bus y barras de voltaje controlado. Flujo de potencia compleja por cada una de las
líneas del sistema, así como sus respectivas pérdidas.
