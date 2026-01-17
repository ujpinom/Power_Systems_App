# ⚡ Power Systems App - Technical Documentation

This document serves as a technical guide for developers and AI agents to understand the architecture, design patterns, and core systems of the Power Systems App.

## 1. Core Architecture (MVC)

The application follows a decoupled Model-View-Controller pattern, heavily utilizing the **Observer Pattern** to synchronize state between the logical model and the visual representation.

### Model Layer (`application.model.project`)

- **`NetworkModel`**: The "Single Source of Truth". It holds all power system components (Barras, Lineas, etc.).
- **`NetworkEventDispatcher`**: Manages listeners and notifies when elements are added or removed.
- **`NetworkHistoryManager`**: Handles Undo/Redo logic by tracking model changes.
- **`NetworkValidator`**: Centralized business logic validation.

### View Layer (`application.view`)

- **`DiagramManager`**: The brain of the canvas. It listens to the `NetworkModel` and manages the creation/reconnection of elements.
- **`NetworkShape<T>`**: Base class for all visual components on the canvas. Handles selection, hover effects, and basic interaction.
- **`BusShape`**, **`LineShape`**, etc.: Specific visual implementations for power components.

---

## 2. The `Connectable` System

To support generic connections between different types of components (Lines, Transformers, Generators, Loads), we use the `Connectable` interface.

### `Connectable` Interface

Located in `proyectoSistemasDePotencia.Connectable`, this interface ensures that any component connecting to a bus has:

- A reference to the connected bus(es).
- An `anchorIndex` to identify the specific visual connection point.

```java
public interface Connectable {
  Barras getBarra1();
  int getAnchorIndex1();
  // ... similar for terminal 2
  boolean isSingleTerminal();
}
```

---

## 3. Communication & State Management

### Event Flow

1. **User Action**: User draws a line or moves a component.
2. **Model Update**: `NetworkModel` is updated.
3. **Notification**: `NetworkEventDispatcher` fires an `onAdded` or `onRemoved` event.
4. **UI Update**: `DiagramManager` receives the event and updates the `PannableCanvas`.

### Property Changes

Components implement `PropertyChangeSupport`. When a property (like a name or a parameter) changes in the model, the corresponding `NetworkShape` reacts immediately via `PropertyChangeListener`.

---

## 4. UI Best Practices

- **`UIUtils`**: Use `application.view.utils.UIUtils.showValidationWarning(ValidationResult)` to report errors to the user consistently.
- **ReadOnly Forms**: Connectivity properties (like Anchor Indexes) should be **read-only** in property panels. Users should change them visually via the "Reconnect" feature in the diagram's context menu.
- **Snap-to-Grid**: Interaction logic in `NetworkShape` and `DiagramManager` follows a 10px grid alignment.

---
