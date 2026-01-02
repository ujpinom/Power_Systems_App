package application.view.panels.forms;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import proyectoSistemasDePotencia.Barras;

public class BusForm extends AbstractForm<Barras> {

    @Override
    public TitledPane render(Barras barra) {
        // --- 1. NOMBRE ---
        TextField txtNombre = new TextField(barra.getNombreBarra());
        txtNombre.setOnAction(e -> {
            barra.setNombrePersonalizado(txtNombre.getText());
        });
        addField("Nombre ID:", 0, txtNombre);

        // --- 2. VOLTAJE (Magnitud) ---
        TextField txtVoltaje = new TextField(String.valueOf(barra.getVoltajePrefalla()));
        txtVoltaje.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) actualizarVoltaje(barra, txtVoltaje);
        });
        txtVoltaje.setOnAction(e -> actualizarVoltaje(barra, txtVoltaje));
        addField("Voltaje (p.u.):", 1, txtVoltaje);

        // --- 3. ÁNGULO ---
        TextField txtAngulo = new TextField(String.valueOf(barra.getAnguloVoltajeBarra()));
        txtAngulo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) actualizarAngulo(barra, txtAngulo);
        });
        txtAngulo.setOnAction(e -> actualizarAngulo(barra, txtAngulo));
        addField("Ángulo (°):", 2, txtAngulo);

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
        addFullRow(chkSwing, 3);

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
