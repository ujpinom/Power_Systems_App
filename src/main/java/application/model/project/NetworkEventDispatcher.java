package application.model.project;

import java.util.ArrayList;
import java.util.List;

/** Gestiona los observadores (listeners) del modelo de red y notifica cambios. */
public class NetworkEventDispatcher {
  private final List<NetworkChangeListener> listeners = new ArrayList<>();

  public void addChangeListener(NetworkChangeListener l) {
    if (!listeners.contains(l)) {
      listeners.add(l);
    }
  }

  public void removeChangeListener(NetworkChangeListener l) {
    listeners.remove(l);
  }

  public void notifyAdded(Object element) {
    for (NetworkChangeListener l : listeners) {
      l.onAdded(element);
    }
  }

  public void notifyRemoved(Object element) {
    for (NetworkChangeListener l : listeners) {
      l.onRemoved(element);
    }
  }
}
