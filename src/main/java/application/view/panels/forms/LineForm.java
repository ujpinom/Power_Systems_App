package application.view.panels.forms;

import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import proyectoSistemasDePotencia.Lineas;

public class LineForm extends AbstractForm<Lineas> {

  @Override
  public TitledPane render(Lineas linea) {
    // --- 1. ID LÓGICO (Solo lectura) ---
    TextField txtLogico = new TextField(linea.getNombreLogico());
    txtLogico.setEditable(false);
    txtLogico.setDisable(true);
    addField("ID Lógico:", 0, txtLogico);

    // --- 2. ID MANUAL ---
    TextField txtNombre = new TextField(linea.getNombrePersonalizado());
    txtNombre.setPromptText("Ingrese un nombre manual...");
    txtNombre.setOnAction(
        e -> {
          linea.setNombrePersonalizado(txtNombre.getText().isEmpty() ? null : txtNombre.getText());
        });
    txtNombre
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) {
                linea.setNombrePersonalizado(
                    txtNombre.getText().isEmpty() ? null : txtNombre.getText());
              }
            });
    addField("ID Manual:", 1, txtNombre);

    // Actualizar el campo lógico si cambia
    linea.addPropertyChangeListener(
        evt -> {
          if ("nombreLinea".equals(evt.getPropertyName())) {
            javafx.application.Platform.runLater(
                () -> txtLogico.setText((String) evt.getNewValue()));
          }
        });

    // --- 2. CONECTIVIDAD ---
    TextField txtOrigen =
        new TextField(linea.getBarra1() != null ? linea.getBarra1().getNombreLogico() : "-");
    txtOrigen.setEditable(false);
    txtOrigen.setDisable(true);
    addField("Bus Origen:", 2, txtOrigen);

    TextField txtAnchor1 = new TextField(String.valueOf(linea.getAnchorIndex1()));
    txtAnchor1.setPromptText("-1 para automático");
    txtAnchor1.setOnAction(e -> actualizarAnchor1(linea, txtAnchor1));
    txtAnchor1
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) actualizarAnchor1(linea, txtAnchor1);
            });
    addField("Anchor Origen:", 3, txtAnchor1);

    TextField txtDestino =
        new TextField(linea.getBarra2() != null ? linea.getBarra2().getNombreLogico() : "-");
    txtDestino.setEditable(false);
    txtDestino.setDisable(true);
    addField("Bus Destino:", 4, txtDestino);

    TextField txtAnchor2 = new TextField(String.valueOf(linea.getAnchorIndex2()));
    txtAnchor2.setPromptText("-1 para automático");
    txtAnchor2.setOnAction(e -> actualizarAnchor2(linea, txtAnchor2));
    txtAnchor2
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) actualizarAnchor2(linea, txtAnchor2);
            });
    addField("Anchor Destino:", 5, txtAnchor2);

    // --- 2. IMPEDANCIA Z1 ---
    TextField txtZ1 = new TextField(String.valueOf(linea.getimpedanciaLineaZ1()));
    txtZ1.setOnAction(e -> actualizarZ1(linea, txtZ1));
    txtZ1
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) actualizarZ1(linea, txtZ1);
            });
    addField("Z1 (p.u.):", 6, txtZ1);

    // --- 3. IMPEDANCIA Z2 ---
    TextField txtZ2 = new TextField(String.valueOf(linea.getimpedanciaLineaZ2()));
    txtZ2.setOnAction(e -> actualizarZ2(linea, txtZ2));
    txtZ2
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) actualizarZ2(linea, txtZ2);
            });
    addField("Z2 (p.u.):", 7, txtZ2);

    // --- 4. IMPEDANCIA Z0 ---
    TextField txtZ0 = new TextField(String.valueOf(linea.getimpedanciaLineaZ0()));
    txtZ0.setOnAction(e -> actualizarZ0(linea, txtZ0));
    txtZ0
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) actualizarZ0(linea, txtZ0);
            });
    addField("Z0 (p.u.):", 8, txtZ0);

    // --- 5. RESISTENCIA ---
    TextField txtResistencia = new TextField(String.valueOf(linea.getResitencia()));
    txtResistencia.setOnAction(e -> actualizarResistencia(linea, txtResistencia));
    txtResistencia
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) actualizarResistencia(linea, txtResistencia);
            });
    addField("Resistencia (p.u.):", 9, txtResistencia);

    // --- 6. Y/2 (SHUNT ADMITTANCE) ---
    TextField txtYMedia = new TextField(String.valueOf(linea.getYMediaParalela()));
    txtYMedia.setOnAction(e -> actualizarYMedia(linea, txtYMedia));
    txtYMedia
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) actualizarYMedia(linea, txtYMedia);
            });
    addField("Y/2 (p.u.):", 10, txtYMedia);

    TitledPane pane = new TitledPane("Datos de Línea", grid);
    pane.setCollapsible(false);
    return pane;
  }

  private void actualizarZ1(Lineas linea, TextField txt) {
    double val = parseDouble(txt.getText(), linea.getimpedanciaLineaZ1());
    linea.setimpedanciaLineaZ1(val);
    txt.setText(String.valueOf(val));
  }

  private void actualizarZ2(Lineas linea, TextField txt) {
    double val = parseDouble(txt.getText(), linea.getimpedanciaLineaZ2());
    linea.setimpedanciaLineaZ2(val);
    txt.setText(String.valueOf(val));
  }

  private void actualizarZ0(Lineas linea, TextField txt) {
    double val = parseDouble(txt.getText(), linea.getimpedanciaLineaZ0());
    linea.setimpedanciaLineaZ0(val);
    txt.setText(String.valueOf(val));
  }

  private void actualizarResistencia(Lineas linea, TextField txt) {
    double val = parseDouble(txt.getText(), linea.getResitencia());
    linea.setResitencia(val);
    txt.setText(String.valueOf(val));
  }

  private void actualizarYMedia(Lineas linea, TextField txt) {
    double val = parseDouble(txt.getText(), linea.getYMediaParalela());
    linea.setYMediaParalela(val);
    txt.setText(String.valueOf(val));
  }

  private void actualizarAnchor1(Lineas linea, TextField txt) {
    int val = parseInt(txt.getText(), linea.getAnchorIndex1());
    linea.setAnchorIndex1(val);
    txt.setText(String.valueOf(val));
  }

  private void actualizarAnchor2(Lineas linea, TextField txt) {
    int val = parseInt(txt.getText(), linea.getAnchorIndex2());
    linea.setAnchorIndex2(val);
    txt.setText(String.valueOf(val));
  }
}
