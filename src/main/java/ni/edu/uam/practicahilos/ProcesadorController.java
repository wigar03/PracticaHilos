package ni.edu.uam.practicahilos;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class ProcesadorController {

    @FXML
    private Label lblEstado;

    @FXML
    private Label lblPorcentaje;

    @FXML
    private ProgressBar barraProgreso;

    @FXML
    private Button btnIniciar;

    @FXML
    private Button btnDetener;

    @FXML
    private Button btnReiniciar;

    @FXML
    private void iniciarProceso() {
        // Implementación de hilo en Commit 4
    }

    @FXML
    private void detenerProceso() {
        // Manejo de interrupción en Commit 5
    }

    @FXML
    private void reiniciarProceso() {
        // Manejo de reinicio en Commit 5
    }

    public void detenerAlCerrar() {
        // Cierre controlado de hilo en Commit 5
    }
}
