package application.view.panels.forms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import proyectoSistemasDePotencia.Transformador;

public class TrafoForm extends AbstractForm<Transformador> {

  @Override
  public TitledPane render(Transformador trafo) {
    // --- 1. ID LÓGICO (Solo lectura) ---
    TextField txtLogico = new TextField(trafo.getNombreLogico());
    txtLogico.setEditable(false);
    txtLogico.setDisable(true);
    addField("ID Lógico:", 0, txtLogico);

    // --- 2. ID MANUAL ---
    TextField txtNombre = new TextField(trafo.getNombrePersonalizado());
    txtNombre.setPromptText("Ingrese un nombre manual...");
    txtNombre.setOnAction(
        e ->
            trafo.setNombrePersonalizado(
                txtNombre.getText().isEmpty() ? null : txtNombre.getText()));
    txtNombre
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) {
                trafo.setNombrePersonalizado(
                    txtNombre.getText().isEmpty() ? null : txtNombre.getText());
              }
            });
    addField("ID Manual:", 1, txtNombre);

    // --- 3. CONECTIVIDAD (Solo lectura) ---
    TextField txtOrigen =
        new TextField(trafo.getBarra1() != null ? trafo.getBarra1().getNombreLogico() : "-");
    txtOrigen.setEditable(false);
    txtOrigen.setDisable(true);
    addField("Bus Origen:", 2, txtOrigen);

    TextField txtDestino =
        new TextField(trafo.getBarra2() != null ? trafo.getBarra2().getNombreLogico() : "-");
    txtDestino.setEditable(false);
    txtDestino.setDisable(true);
    addField("Bus Destino:", 3, txtDestino);

    // --- 4. IMPEDANCIAS ---
    TextField txtZ1 = new TextField(String.valueOf(trafo.getimpedanciaLineaZ1()));
    txtZ1.setOnAction(e -> actualizarZ1(trafo, txtZ1));
    txtZ1
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) actualizarZ1(trafo, txtZ1);
            });
    addField("Z1 (p.u.):", 4, txtZ1);

    TextField txtZ2 = new TextField(String.valueOf(trafo.getimpedanciaLineaZ2()));
    txtZ2.setOnAction(e -> actualizarZ2(trafo, txtZ2));
    txtZ2
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) actualizarZ2(trafo, txtZ2);
            });
    addField("Z2 (p.u.):", 5, txtZ2);

    TextField txtZ0 = new TextField(String.valueOf(trafo.getimpedanciaLineaZ0()));
    txtZ0.setOnAction(e -> actualizarZ0(trafo, txtZ0));
    txtZ0
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) actualizarZ0(trafo, txtZ0);
            });
    addField("Z0 (p.u.):", 6, txtZ0);

    TextField txtResistencia = new TextField(String.valueOf(trafo.getResitencia()));
    txtResistencia.setOnAction(e -> actualizarResistencia(trafo, txtResistencia));
    txtResistencia
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) actualizarResistencia(trafo, txtResistencia);
            });
    addField("Resistencia (p.u.):", 7, txtResistencia);

    // --- 5. CONEXIONES (ComboBox) ---
    ObservableList<String> opciones =
        FXCollections.observableArrayList(
            "YN-" + trafo.getBarra1().getNombreBarra(),
            "YN-" + trafo.getBarra2().getNombreBarra(),
            "Y-" + trafo.getBarra1().getNombreBarra(),
            "Y-" + trafo.getBarra2().getNombreBarra(),
            "DELTA-" + trafo.getBarra1().getNombreBarra(),
            "DELTA-" + trafo.getBarra2().getNombreBarra());

    ComboBox<String> cboPrimaria = new ComboBox<>(opciones);
    cboPrimaria.setValue(trafo.getConexionPrimaria());
    addField("Conexión Primaria:", 8, cboPrimaria);

    Label lblGroundedPrim = createLabel("Z Aterrizamiento Primaria (p.u.):");
    TextField txtGroundedPrim =
        new TextField(String.valueOf(trafo.getImpedanciaAterrizamientoPrimaria()));
    VBox groundedPrimBox = new VBox(5, lblGroundedPrim, txtGroundedPrim);
    actualizarVisibilidadTierra(trafo.getConexionPrimaria(), groundedPrimBox);

    txtGroundedPrim.setOnAction(e -> actualizarGroundedPrim(trafo, txtGroundedPrim));
    txtGroundedPrim
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) actualizarGroundedPrim(trafo, txtGroundedPrim);
            });

    cboPrimaria.setOnAction(
        e -> {
          String val = cboPrimaria.getValue();
          trafo.setConexionPrimaria(val);
          actualizarVisibilidadTierra(val, groundedPrimBox);
        });
    addFullRow(groundedPrimBox, 9);

    ComboBox<String> cboSecundaria = new ComboBox<>(opciones);
    cboSecundaria.setValue(trafo.getConexionSecundaria());
    addField("Conexión Secundaria:", 10, cboSecundaria);

    Label lblGroundedSec = createLabel("Z Aterrizamiento Secundaria (p.u.):");
    TextField txtGroundedSec =
        new TextField(String.valueOf(trafo.getImpedanciaAterrizamientoSecundaria()));
    VBox groundedSecBox = new VBox(5, lblGroundedSec, txtGroundedSec);
    actualizarVisibilidadTierra(trafo.getConexionSecundaria(), groundedSecBox);

    txtGroundedSec.setOnAction(e -> actualizarGroundedSec(trafo, txtGroundedSec));
    txtGroundedSec
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) actualizarGroundedSec(trafo, txtGroundedSec);
            });

    cboSecundaria.setOnAction(
        e -> {
          String val = cboSecundaria.getValue();
          trafo.setConexionSecundaria(val);
          actualizarVisibilidadTierra(val, groundedSecBox);
        });
    addFullRow(groundedSecBox, 11);

    // --- 6. SECCIÓN TAP (Dinámica) ---
    VBox tapBox = new VBox(10);
    CheckBox chkTap = new CheckBox("¿Posee Tap?");
    chkTap.setStyle("-fx-text-fill: #333333; -fx-font-weight: bold;");
    chkTap.setSelected(trafo.isHasTap());

    Label lblMag = createLabel("Magnitud Tap:");
    TextField txtMag = new TextField(String.valueOf(trafo.getMagTab()));
    Label lblAng = createLabel("Ángulo Tap:");
    TextField txtAng = new TextField(String.valueOf(trafo.getAngtab()));

    Label lblTapBus = createLabel("Ubicación del Tap:");
    ComboBox<String> cboTapBus =
        new ComboBox<>(
            FXCollections.observableArrayList(
                trafo.getBarra1().getNombreBarra(), trafo.getBarra2().getNombreBarra()));

    if (trafo.getBarraConTap() != null) {
      cboTapBus.setValue(trafo.getBarraConTap().getNombreBarra());
    }

    cboTapBus.setOnAction(
        e -> {
          String val = cboTapBus.getValue();
          if (val != null) {
            trafo.setBarraConTap(
                val.equals(trafo.getBarra1().getNombreBarra())
                    ? trafo.getBarra1()
                    : trafo.getBarra2());
          }
        });

    // Lógica de visibilidad
    VBox tapFields = new VBox(5, lblMag, txtMag, lblAng, txtAng, lblTapBus, cboTapBus);
    tapFields.setVisible(trafo.isHasTap());
    tapFields.setManaged(trafo.isHasTap());

    chkTap.setOnAction(
        e -> {
          boolean selected = chkTap.isSelected();
          trafo.setHasTap(selected);
          tapFields.setVisible(selected);
          tapFields.setManaged(selected);
        });

    txtMag.setOnAction(e -> actualizarMagTap(trafo, txtMag));
    txtMag
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) actualizarMagTap(trafo, txtMag);
            });

    txtAng.setOnAction(e -> actualizarAngTap(trafo, txtAng));
    txtAng
        .focusedProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (!newVal) actualizarAngTap(trafo, txtAng);
            });

    tapBox.getChildren().addAll(chkTap, tapFields);
    addFullRow(tapBox, 12);

    javafx.scene.control.ScrollPane scroll = new javafx.scene.control.ScrollPane(grid);
    scroll.setFitToWidth(true);
    scroll.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
    scroll.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scroll.setStyle(
        "-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");

    // El TitledPane por defecto tiene un padding que puede comer espacio
    TitledPane pane = new TitledPane("Datos de Transformador", scroll);
    pane.setCollapsible(false);
    pane.setPadding(javafx.geometry.Insets.EMPTY);
    return pane;
  }

  private void actualizarZ1(Transformador t, TextField txt) {
    double val = parseDouble(txt.getText(), t.getimpedanciaLineaZ1());
    t.setimpedanciaLineaZ1(val);
    txt.setText(String.valueOf(val));
  }

  private void actualizarZ2(Transformador t, TextField txt) {
    double val = parseDouble(txt.getText(), t.getimpedanciaLineaZ2());
    t.setimpedanciaLineaZ2(val);
    txt.setText(String.valueOf(val));
  }

  private void actualizarZ0(Transformador t, TextField txt) {
    double val = parseDouble(txt.getText(), t.getimpedanciaLineaZ0());
    t.setimpedanciaLineaZ0(val);
    txt.setText(String.valueOf(val));
  }

  private void actualizarResistencia(Transformador t, TextField txt) {
    double val = parseDouble(txt.getText(), t.getResitencia());
    t.setResitencia(val);
    txt.setText(String.valueOf(val));
  }

  private void actualizarMagTap(Transformador t, TextField txt) {
    double val = parseDouble(txt.getText(), t.getMagTab());
    t.setMagTab(val);
    txt.setText(String.valueOf(val));
  }

  private void actualizarAngTap(Transformador t, TextField txt) {
    double val = parseDouble(txt.getText(), t.getAngtab());
    t.setAngtab(val);
    txt.setText(String.valueOf(val));
  }

  private void actualizarGroundedPrim(Transformador t, TextField txt) {
    double val = parseDouble(txt.getText(), t.getImpedanciaAterrizamientoPrimaria());
    t.setImpedanciaAterrizamientoPrimaria(val);
    txt.setText(String.valueOf(val));
  }

  private void actualizarGroundedSec(Transformador t, TextField txt) {
    double val = parseDouble(txt.getText(), t.getImpedanciaAterrizamientoSecundaria());
    t.setImpedanciaAterrizamientoSecundaria(val);
    txt.setText(String.valueOf(val));
  }

  private void actualizarVisibilidadTierra(String conexion, VBox box) {
    boolean isYN = conexion != null && conexion.startsWith("YN");
    box.setVisible(isYN);
    box.setManaged(isYN);
  }
}
