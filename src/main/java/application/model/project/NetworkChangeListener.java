package application.model.project;

/**
 * Interface for observing changes in the NetworkModel. This allows multiple components (Canvas,
 * Undo History, Tables) to react to model changes through a single centralized dispatcher.
 */
public interface NetworkChangeListener {
  /**
   * Called when a new element is added to the network model.
   *
   * @param element The component added (Barras, Lineas, Generadores, etc.)
   */
  void onAdded(Object element);

  /**
   * Called when an element is removed from the network model.
   *
   * @param element The component removed.
   */
  void onRemoved(Object element);
}
