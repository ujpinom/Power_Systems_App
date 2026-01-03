# ⚡ Power Systems App - Registro de Mejoras (Modernización UI/UX)

Este documento detalla las mejoras arquitectónicas, visuales y de experiencia de usuario implementadas para transformar la aplicación en una herramienta de análisis de sistemas de potencia profesional (estilo CAD/ETAP).

## 1. Arquitectura y Ventana Principal (`Main.java`)
- [x] **Redimensionamiento Habilitado:** Se eliminó la restricción `setResizable(false)` para permitir el uso en monitores grandes.
- [x] **Dimensiones Mínimas:** Se establecieron límites (Min: 800x600) para evitar que la interfaz colapse.
- [x] **Carga Optimizada:** Se eliminó la doble instanciación del `SPController` (antes se creaba uno en el `FXMLLoader` y otro manualmente que quedaba "zombie").
- [x] **Inyección de CSS:** Vinculación robusta del archivo de estilos global al iniciar la escena.

## 2. Estilo Visual y Tema (`application.css`)
- [x] **Tema "Industrial Clean":** Cambio del gris por defecto de JavaFX (`modena`) a una paleta profesional (`#f4f4f4` para paneles, blanco para lienzo).
- [x] **Grid CAD Vectorial:** Implementación de una cuadrícula milimetrada de fondo en el área de dibujo utilizando **CSS Gradients** (sin imágenes externas), optimizado para rendimiento.
- [x] **Tipografía:**
  - UI General: *Segoe UI / Helvetica* (Legibilidad).
  - Datos/Logs: *Consolas / Monospaced* (Alineación de tablas de texto).
- [x] **Feedback Visual:** Estados `Hover` (Azul tenue `#e6f7ff`) y `Pressed` para todos los botones.

## 3. Diseño de Interfaz (`SP.fxml`)

### A. Barra Superior (Ribbon)
- [x] **Iconos SVG:** Reemplazo de botones de texto por iconos vectoriales escalables (Play, Rayo, Documento).
- [x] **Organización Semántica:** Agrupación de comandos en pestañas ("Inicio" para edición, "Análisis" para simulación).
- [x] **Estilo de Botones:** Botones principales grandes (Icono arriba) y menús desplegables compactos.

### B. Panel Izquierdo (Paleta de Componentes)
- [x] **Simbología Estandarizada:** Creación de iconos vectoriales (`SVGPath`) siguiendo normas IEC/ANSI para:
  - Barras (Busbar).
  - Transformadores (Doble círculo).
  - Generadores (Onda senoidal).
  - Elementos Shunt y Cargas.
- [x] **Escalabilidad:** Implementación de un `ScrollPane` para soportar una librería creciente de componentes.
- [x] **Layout Compacto:** Uso de `FlowPane` para organizar las herramientas en una cuadrícula flexible.

### C. Área Central (Lienzo de Dibujo)
- [x] **Navegación Avanzada:** Implementación de estructura `ScrollPane` + `StackPane` para permitir **Paneo (Scroll)** en diagramas grandes.
- [x] **Lienzo Infinito:** Ampliación del área de trabajo virtual a 2000x2000 píxeles.
- [x] **Controles Flotantes:** Adición de botones de Zoom (+ / -) y etiqueta de porcentaje flotando sobre el lienzo.

### D. Panel Inferior (Resultados)
- [x] **Resultados Tabulares:** Reemplazo de `TextArea` simple por un `TabPane` con:
  - **Consola:** Logs del sistema.
  - **Tabla de Barras:** `TableView` preparada para mostrar Voltajes (p.u.) y Ángulos.
  - **Tabla de Ramas:** `TableView` para Flujos de Potencia y Pérdidas.
- [x] **Barra de Estado (Status Bar):** Diseño estilo VS Code (Azul `#007acc`) con coordenadas del mouse y estado del sistema.

## 4. Lógica de Control (`SPController.java`)
- [x] **Motor de Zoom:** Implementación del método `updateZoom()` utilizando transformaciones `Scale` de JavaFX.
- [x] **Gestión de Vistas:** Inyección `@FXML` de los nuevos contenedores (`scrollContainer`, `zoomContainer`) y tablas (`tablaBarras`, `tablaRamas`).
- [x] **Coordenadas:** Ajuste de lógica para mantener la funcionalidad de dibujo mouse-click dentro del nuevo sistema de coordenadas escalado.
- [x] **Validación de Proximidad:** Implementación de `validarProximidad(x, y)` para evitar la creación de barras superpuestas (distancia mínima de 50px).

---

## 5. Modernización de Objetos Gráficos (Fase 2)

### A. Arquitectura de Figuras (`NetworkShape`)
- [x] **Clase Base `NetworkShape<T>`:** Creación de una superclase genérica que hereda de `Group`.
    - **Zoom on Hover:** Efecto automático de escalado (1.2x) y elevación (Z-Index) al pasar el mouse.
    - **Gestión de Etiquetas:** Lógica centralizada para crear y posicionar etiquetas de texto.
    - **Estados de Selección:** Métodos abstractos para estandarizar la apariencia de selección.
- [x] **Refactorización de `BusShape`:** Actualización para heredar de `NetworkShape`.
    - Uso de posicionamiento absoluto (eliminación de `StackPane`).
    - Etiqueta de texto forzada a color negro y posición fija para garantizar visibilidad.
    - Efecto de selección con sombra `CYAN`.

### B. Interacción Avanzada
- [x] **Drag & Drop (Arrastrar y Soltar):** Implementación nativa en `NetworkShape`.
    - **Snap-to-Grid:** Ajuste automático a cuadrícula de 10px para alineación perfecta.
    - **Sincronización Bidireccional:** El movimiento gráfico actualiza automáticamente las coordenadas del modelo lógico (`Barras`).
- [x] **Gestor de Diagrama Genérico (`DiagramManager`):** Actualización para manejar `NetworkShape<?>` en lugar de clases concretas, permitiendo escalabilidad futura.

### C. Sistema de Propiedades (Observer Pattern)
- [x] **Panel de Propiedades Reactivo:** Implementación de `PropertyChangeSupport` en el modelo (`Barras.java`).
- [x] **Sincronización Automática:** `BusShape` se suscribe a cambios en el modelo.
    - Si se cambia el nombre en el panel derecho, la etiqueta en el diagrama se actualiza al instante.
- [x] **Lógica de Barra Slack:** Implementación de exclusión mutua en `BusForm`. Solo una barra puede ser marcada como Slack a la vez; las demás se desmarcan automáticamente.

---

## 6. Próximos Pasos Sugeridos
1.  **Líneas Dinámicas:** Actualizar visualmente las líneas conectadas cuando se arrastra una barra (Binding de coordenadas).
2.  **Formularios Faltantes:** Crear formularios (`LineForm`, `TrafoForm`, `GenForm`) usando la nueva arquitectura `AbstractForm`.
3.  **Conexión de Datos:** Vincular los resultados del cálculo (Newton-Raphson) a las nuevas `TableView`.
