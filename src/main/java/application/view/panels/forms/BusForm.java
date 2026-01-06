package application.view.panels.forms;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import proyectoSistemasDePotencia.Barras;

public class BusForm extends AbstractForm<Barras> {

    @Override
    public TitledPane render(Barras barra) {
        // --- 1. ID LÓGICO (Solo lectura) ---
        TextField txtLogico = new TextField(barra.isBarraCompensacion() ? "Slack" : barra.getNombreLogico());
        txtLogico.setEditable(false);
        txtLogico.setDisable(true);
        addField("ID Lógico:", 0, txtLogico);

        // --- 2. NOMBRE PERSONALIZADO ---
        TextField txtNombre = new TextField(barra.getNombrePersonalizado());
        txtNombre.setPromptText("Ingrese un nombre manual...");
        txtNombre.setOnAction(e -> {
            barra.setNombrePersonalizado(txtNombre.getText().isEmpty() ? null : txtNombre.getText());
        });
        txtNombre.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                barra.setNombrePersonalizado(txtNombre.getText().isEmpty() ? null : txtNombre.getText());
            }
        });
        addField("ID Manual:", 1, txtNombre);

        // Actualizar el campo lógico si cambia (aunque sea solo lectura en el form,
        // refrescar si se requiere)
        barra.addPropertyChangeListener(evt -> {
            String prop = evt.getPropertyName();
            if ("nombreBarra".equals(prop) || "barraCompensacion".equals(prop)) {
                javafx.application.Platform.runLater(
                        () -> txtLogico.setText(barra.isBarraCompensacion() ? "Slack" : barra.getNombreLogico()));
            }
        });

        // --- 2. VOLTAJE (Magnitud) ---
        TextField txtVoltaje = new TextField(String.valueOf(barra.getVoltajePrefalla()));
        txtVoltaje.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal)
                actualizarVoltaje(barra, txtVoltaje);
        });
        txtVoltaje.setOnAction(e -> actualizarVoltaje(barra, txtVoltaje));
        addField("Voltaje (p.u.):", 2, txtVoltaje);

        // --- 3. ÁNGULO ---
        TextField txtAngulo = new TextField(String.valueOf(barra.getAnguloVoltajeBarra()));
        txtAngulo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal)
                actualizarAngulo(barra, txtAngulo);
        });
        txtAngulo.setOnAction(e -> actualizarAngulo(barra, txtAngulo));
        addField("Ángulo (°):", 3, txtAngulo);

        // --- 4. TIPO DE BARRA (Swing/Compensación) ---
        CheckBox chkSwing = new CheckBox("Barra de Compensación (Slack)");
        chkSwing.setSelected(barra.isBarraCompensacion());
        chkSwing.setOnAction(e -> {
            boolean esSwing = chkSwing.isSelected();

            if (esSwing) {
                // Desmarcar cualquier otra barra que fuera Slack
                for (Barras b : application.model.project.NetworkModel.getInstance().getBarras()) {
                    if (b != barra && b.isBarraCompensacion()) {
                        b.setBarraCompensacion(false);
                    }
                }

                // Configurar la actual como Slack
                barra.setBarraCompensacion(true);
                // Una barra Slack define V y Theta, no P y Q ni P y V
                barra.setBarraPV(false);
                barra.setBarraPQ(false);
            } else {
                barra.setBarraCompensacion(false);
            }
        });
        addFullRow(chkSwing, 4);

        TitledPane pane = new TitledPane("Datos de Barra", grid);
        pane.setCollapsible(false);
        return pane;
    }

    private void actualizarVoltaje(Barras barra, TextField txt) {
        double val = parseDouble(txt.getText(), barra.getVoltajePrefalla());
        barra.setVoltajePrefalla(val);
        // Update text field in case it was invalid input
        if (val != Double.parseDouble(txt.getText())) {
            txt.setText(String.valueOf(val));
        }
    }

    private void actualizarAngulo(Barras barra, TextField txt) {
        double val = parseDouble(txt.getText(), barra.getAnguloVoltajeBarra());
        barra.setAnguloVoltajeBarra(val);
        if (val != Double.parseDouble(txt.getText())) {
            txt.setText(String.valueOf(val));
        }
    }
}
