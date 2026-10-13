package ni.edu.uam.practicahilos;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class ProcesadorController {

    // --- Componentes Práctica Guiada (Paso 1 - 6) ---
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
    private Button btnBloqueante;

    private Thread hiloProceso;

    // --- Componentes Reto Final (Clientes y Productos) ---
    @FXML
    private Label lblEstadoClientes;

    @FXML
    private Label lblPorcentajeClientes;

    @FXML
    private Label lblDetalleClientes;

    @FXML
    private ProgressBar barraProgresoClientes;

    @FXML
    private Button btnIniciarClientes;

    @FXML
    private Button btnDetenerClientes;

    @FXML
    private Button btnReiniciarClientes;

    @FXML
    private Label lblEstadoProductos;

    @FXML
    private Label lblPorcentajeProductos;

    @FXML
    private Label lblDetalleProductos;

    @FXML
    private ProgressBar barraProgresoProductos;

    @FXML
    private Button btnIniciarProductos;

    @FXML
    private Button btnDetenerProductos;

    @FXML
    private Button btnReiniciarProductos;

    @FXML
    private Button btnIniciarAmbos;

    @FXML
    private Button btnDetenerAmbos;

    @FXML
    private Button btnReiniciarAmbos;

    @FXML
    private Label lblResumenGeneral;

    private Thread hiloClientes;
    private Thread hiloProductos;

    // ==========================================
    // MÉTODOS DE LA PRÁCTICA GUIADA
    // ==========================================

    @FXML
    private void iniciarProceso() {
        if (hiloProceso != null && hiloProceso.isAlive()) {
            return;
        }

        barraProgreso.setProgress(0);
        lblPorcentaje.setText("0%");
        lblEstado.setText("Procesando registros...");

        btnIniciar.setDisable(true);
        btnDetener.setDisable(false);
        btnReiniciar.setDisable(true);

        Runnable tarea = () -> {
            try {
                for (int i = 1; i <= 100; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    Thread.sleep(100);

                    final int avance = i;
                    Platform.runLater(() -> {
                        barraProgreso.setProgress(avance / 100.0);
                        lblPorcentaje.setText(avance + "%");
                    });
                }

                Platform.runLater(() -> {
                    lblEstado.setText("Proceso finalizado");
                    habilitarBotones();
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Platform.runLater(() -> {
                    lblEstado.setText("Proceso detenido");
                    habilitarBotones();
                });
            }
        };

        hiloProceso = new Thread(tarea);
        hiloProceso.setName("Hilo-Procesamiento");
        hiloProceso.setDaemon(true);
        hiloProceso.start();
    }

    @FXML
    private void detenerProceso() {
        if (hiloProceso != null && hiloProceso.isAlive()) {
            btnDetener.setDisable(true);
            lblEstado.setText("Solicitando detención...");
            hiloProceso.interrupt();
        }
    }

    @FXML
    private void reiniciarProceso() {
        if (hiloProceso != null && hiloProceso.isAlive()) {
            return;
        }

        barraProgreso.setProgress(0);
        lblPorcentaje.setText("0%");
        lblEstado.setText("Esperando inicio");
        habilitarBotones();
    }

    @FXML
    private void procesoBloqueante() {
        lblEstado.setText("Procesando en hilo principal (bloqueo)...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        lblEstado.setText("Finalizado (hilo principal desbloqueado)");
    }

    private void habilitarBotones() {
        btnIniciar.setDisable(false);
        btnDetener.setDisable(true);
        btnReiniciar.setDisable(false);
    }

    // ==========================================
    // RETO FINAL: CONCURRENCIA CLIENTES Y PRODUCTOS
    // ==========================================

    @FXML
    private void iniciarProcesoClientes() {
        if (hiloClientes != null && hiloClientes.isAlive()) {
            return;
        }

        barraProgresoClientes.setProgress(0);
        lblPorcentajeClientes.setText("0%");
        lblDetalleClientes.setText("0 / 100 procesados");
        lblEstadoClientes.setText("Procesando clientes...");

        btnIniciarClientes.setDisable(true);
        btnDetenerClientes.setDisable(false);
        btnReiniciarClientes.setDisable(true);
        actualizarEstadoResumen();

        Runnable tareaClientes = () -> {
            try {
                for (int i = 1; i <= 100; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    // 80 ms por registro simulando proceso de clientes
                    Thread.sleep(80);

                    final int avance = i;
                    Platform.runLater(() -> {
                        barraProgresoClientes.setProgress(avance / 100.0);
                        lblPorcentajeClientes.setText(avance + "%");
                        lblDetalleClientes.setText(avance + " / 100 procesados");
                    });
                }

                Platform.runLater(() -> {
                    lblEstadoClientes.setText("Clientes completados");
                    btnIniciarClientes.setDisable(false);
                    btnDetenerClientes.setDisable(true);
                    btnReiniciarClientes.setDisable(false);
                    actualizarEstadoResumen();
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Platform.runLater(() -> {
                    lblEstadoClientes.setText("Proceso detenido");
                    btnIniciarClientes.setDisable(false);
                    btnDetenerClientes.setDisable(true);
                    btnReiniciarClientes.setDisable(false);
                    actualizarEstadoResumen();
                });
            }
        };

        hiloClientes = new Thread(tareaClientes, "Hilo-Clientes");
        hiloClientes.setDaemon(true);
        hiloClientes.start();
    }

    @FXML
    private void detenerProcesoClientes() {
        if (hiloClientes != null && hiloClientes.isAlive()) {
            btnDetenerClientes.setDisable(true);
            lblEstadoClientes.setText("Solicitando detención...");
            hiloClientes.interrupt();
        }
    }

    @FXML
    private void reiniciarProcesoClientes() {
        if (hiloClientes != null && hiloClientes.isAlive()) {
            return;
        }

        barraProgresoClientes.setProgress(0);
        lblPorcentajeClientes.setText("0%");
        lblDetalleClientes.setText("0 / 100 procesados");
        lblEstadoClientes.setText("Esperando inicio");
        btnIniciarClientes.setDisable(false);
        btnDetenerClientes.setDisable(true);
        btnReiniciarClientes.setDisable(false);
        actualizarEstadoResumen();
    }

    @FXML
    private void iniciarProcesoProductos() {
        if (hiloProductos != null && hiloProductos.isAlive()) {
            return;
        }

        barraProgresoProductos.setProgress(0);
        lblPorcentajeProductos.setText("0%");
        lblDetalleProductos.setText("0 / 50 procesados");
        lblEstadoProductos.setText("Procesando productos...");

        btnIniciarProductos.setDisable(true);
        btnDetenerProductos.setDisable(false);
        btnReiniciarProductos.setDisable(true);
        actualizarEstadoResumen();

        Runnable tareaProductos = () -> {
            try {
                for (int i = 1; i <= 50; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    // 150 ms por producto (ritmo diferente a clientes)
                    Thread.sleep(150);

                    final int avance = i;
                    final int porcentaje = (avance * 100) / 50;
                    Platform.runLater(() -> {
                        barraProgresoProductos.setProgress(avance / 50.0);
                        lblPorcentajeProductos.setText(porcentaje + "%");
                        lblDetalleProductos.setText(avance + " / 50 procesados");
                    });
                }

                Platform.runLater(() -> {
                    lblEstadoProductos.setText("Productos completados");
                    btnIniciarProductos.setDisable(false);
                    btnDetenerProductos.setDisable(true);
                    btnReiniciarProductos.setDisable(false);
                    actualizarEstadoResumen();
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Platform.runLater(() -> {
                    lblEstadoProductos.setText("Proceso detenido");
                    btnIniciarProductos.setDisable(false);
                    btnDetenerProductos.setDisable(true);
                    btnReiniciarProductos.setDisable(false);
                    actualizarEstadoResumen();
                });
            }
        };

        hiloProductos = new Thread(tareaProductos, "Hilo-Productos");
        hiloProductos.setDaemon(true);
        hiloProductos.start();
    }

    @FXML
    private void detenerProcesoProductos() {
        if (hiloProductos != null && hiloProductos.isAlive()) {
            btnDetenerProductos.setDisable(true);
            lblEstadoProductos.setText("Solicitando detención...");
            hiloProductos.interrupt();
        }
    }

    @FXML
    private void reiniciarProcesoProductos() {
        if (hiloProductos != null && hiloProductos.isAlive()) {
            return;
        }

        barraProgresoProductos.setProgress(0);
        lblPorcentajeProductos.setText("0%");
        lblDetalleProductos.setText("0 / 50 procesados");
        lblEstadoProductos.setText("Esperando inicio");
        btnIniciarProductos.setDisable(false);
        btnDetenerProductos.setDisable(true);
        btnReiniciarProductos.setDisable(false);
        actualizarEstadoResumen();
    }

    @FXML
    private void iniciarAmbosProcesos() {
        iniciarProcesoClientes();
        iniciarProcesoProductos();
    }

    @FXML
    private void detenerAmbosProcesos() {
        detenerProcesoClientes();
        detenerProcesoProductos();
    }

    @FXML
    private void reiniciarAmbosProcesos() {
        reiniciarProcesoClientes();
        reiniciarProcesoProductos();
    }

    private void actualizarEstadoResumen() {
        boolean clientesActivo = hiloClientes != null && hiloClientes.isAlive();
        boolean productosActivo = hiloProductos != null && hiloProductos.isAlive();

        if (clientesActivo && productosActivo) {
            lblResumenGeneral.setText("Ejecutando concurrentemente: Clientes y Productos");
        } else if (clientesActivo) {
            lblResumenGeneral.setText("Ejecutando: Clientes | Productos inactivo");
        } else if (productosActivo) {
            lblResumenGeneral.setText("Ejecutando: Productos | Clientes inactivo");
        } else {
            lblResumenGeneral.setText("Procesos detenidos o listos para iniciar");
        }
    }

    public void detenerAlCerrar() {
        if (hiloProceso != null && hiloProceso.isAlive()) {
            hiloProceso.interrupt();
        }
        if (hiloClientes != null && hiloClientes.isAlive()) {
            hiloClientes.interrupt();
        }
        if (hiloProductos != null && hiloProductos.isAlive()) {
            hiloProductos.interrupt();
        }
    }
}
