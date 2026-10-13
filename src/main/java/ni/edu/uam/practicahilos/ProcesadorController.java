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
    private Button btnDetener; // Se transforma dinámicamente entre Pausar y Reanudar

    @FXML
    private Button btnReiniciar;

    @FXML
    private Button btnBloqueante;

    private Thread hiloProceso;
    private int progresoActual = 0; // Registro actual (1 a 100)

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
    private int progresoClientes = 0;   // 1 a 100
    private int progresoProductos = 0;  // 1 a 50

    // ==========================================
    // MÉTODOS DE LA PRÁCTICA GUIADA
    // ==========================================

    @FXML
    private void iniciarProceso() {
        if (hiloProceso != null && hiloProceso.isAlive()) {
            return;
        }

        if (progresoActual >= 100) {
            progresoActual = 0;
            barraProgreso.setProgress(0);
            lblPorcentaje.setText("0%");
        }

        ejecutarHiloProceso();
    }

    private void ejecutarHiloProceso() {
        lblEstado.setText(progresoActual > 0 ? "Reanudando procesamiento..." : "Procesando registros...");
        btnIniciar.setDisable(true);
        btnDetener.setDisable(false);
        btnDetener.setText("⏸ Pausar");
        btnReiniciar.setDisable(true);

        final int inicio = progresoActual + 1;

        Runnable tarea = () -> {
            try {
                for (int i = inicio; i <= 100; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    Thread.sleep(100);

                    final int avance = i;
                    progresoActual = avance;

                    Platform.runLater(() -> {
                        barraProgreso.setProgress(avance / 100.0);
                        lblPorcentaje.setText(avance + "%");
                    });
                }

                Platform.runLater(() -> {
                    lblEstado.setText("Proceso finalizado");
                    progresoActual = 100;
                    btnIniciar.setDisable(false);
                    btnDetener.setDisable(true);
                    btnDetener.setText("⏸ Pausar");
                    btnReiniciar.setDisable(false);
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Platform.runLater(() -> {
                    lblEstado.setText("Proceso pausado (" + progresoActual + "% completado)");
                    btnIniciar.setDisable(true);
                    btnDetener.setDisable(false);
                    btnDetener.setText("▶ Reanudar");
                    btnReiniciar.setDisable(false);
                });
            }
        };

        hiloProceso = new Thread(tarea, "Hilo-Procesamiento");
        hiloProceso.setDaemon(true);
        hiloProceso.start();
    }

    @FXML
    private void detenerProceso() {
        if (hiloProceso != null && hiloProceso.isAlive()) {
            lblEstado.setText("Pausando proceso...");
            btnDetener.setDisable(true);
            hiloProceso.interrupt();
        } else if (progresoActual > 0 && progresoActual < 100) {
            // El botón se convirtió en Reanudar
            ejecutarHiloProceso();
        }
    }

    @FXML
    private void reiniciarProceso() {
        if (hiloProceso != null && hiloProceso.isAlive()) {
            hiloProceso.interrupt();
        }

        progresoActual = 0;
        barraProgreso.setProgress(0);
        lblPorcentaje.setText("0%");
        lblEstado.setText("Esperando inicio");
        btnIniciar.setDisable(false);
        btnDetener.setDisable(true);
        btnDetener.setText("⏸ Pausar");
        btnReiniciar.setDisable(false);
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

    // ==========================================
    // RETO FINAL: CONCURRENCIA CLIENTES Y PRODUCTOS
    // ==========================================

    @FXML
    private void iniciarProcesoClientes() {
        if (hiloClientes != null && hiloClientes.isAlive()) {
            return;
        }

        if (progresoClientes >= 100) {
            progresoClientes = 0;
            barraProgresoClientes.setProgress(0);
            lblPorcentajeClientes.setText("0%");
            lblDetalleClientes.setText("0 / 100 procesados");
        }

        ejecutarHiloClientes();
    }

    private void ejecutarHiloClientes() {
        lblEstadoClientes.setText(progresoClientes > 0 ? "Reanudando clientes..." : "Procesando clientes...");
        btnIniciarClientes.setDisable(true);
        btnDetenerClientes.setDisable(false);
        btnDetenerClientes.setText("⏸ Pausar Clientes");
        btnReiniciarClientes.setDisable(true);
        actualizarEstadoResumen();

        final int inicio = progresoClientes + 1;

        Runnable tareaClientes = () -> {
            try {
                for (int i = inicio; i <= 100; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    Thread.sleep(80);

                    final int avance = i;
                    progresoClientes = avance;

                    Platform.runLater(() -> {
                        barraProgresoClientes.setProgress(avance / 100.0);
                        lblPorcentajeClientes.setText(avance + "%");
                        lblDetalleClientes.setText(avance + " / 100 procesados");
                    });
                }

                Platform.runLater(() -> {
                    lblEstadoClientes.setText("Clientes completados");
                    progresoClientes = 100;
                    btnIniciarClientes.setDisable(false);
                    btnDetenerClientes.setDisable(true);
                    btnDetenerClientes.setText("⏸ Pausar Clientes");
                    btnReiniciarClientes.setDisable(false);
                    actualizarEstadoResumen();
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Platform.runLater(() -> {
                    lblEstadoClientes.setText("Pausado en " + progresoClientes + "/100");
                    btnIniciarClientes.setDisable(true);
                    btnDetenerClientes.setDisable(false);
                    btnDetenerClientes.setText("▶ Reanudar Clientes");
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
            lblEstadoClientes.setText("Pausando clientes...");
            btnDetenerClientes.setDisable(true);
            hiloClientes.interrupt();
        } else if (progresoClientes > 0 && progresoClientes < 100) {
            ejecutarHiloClientes();
        }
    }

    @FXML
    private void reiniciarProcesoClientes() {
        if (hiloClientes != null && hiloClientes.isAlive()) {
            hiloClientes.interrupt();
        }

        progresoClientes = 0;
        barraProgresoClientes.setProgress(0);
        lblPorcentajeClientes.setText("0%");
        lblDetalleClientes.setText("0 / 100 procesados");
        lblEstadoClientes.setText("Esperando inicio");
        btnIniciarClientes.setDisable(false);
        btnDetenerClientes.setDisable(true);
        btnDetenerClientes.setText("⏸ Pausar Clientes");
        btnReiniciarClientes.setDisable(false);
        actualizarEstadoResumen();
    }

    @FXML
    private void iniciarProcesoProductos() {
        if (hiloProductos != null && hiloProductos.isAlive()) {
            return;
        }

        if (progresoProductos >= 50) {
            progresoProductos = 0;
            barraProgresoProductos.setProgress(0);
            lblPorcentajeProductos.setText("0%");
            lblDetalleProductos.setText("0 / 50 procesados");
        }

        ejecutarHiloProductos();
    }

    private void ejecutarHiloProductos() {
        lblEstadoProductos.setText(progresoProductos > 0 ? "Reanudando productos..." : "Procesando productos...");
        btnIniciarProductos.setDisable(true);
        btnDetenerProductos.setDisable(false);
        btnDetenerProductos.setText("⏸ Pausar Productos");
        btnReiniciarProductos.setDisable(true);
        actualizarEstadoResumen();

        final int inicio = progresoProductos + 1;

        Runnable tareaProductos = () -> {
            try {
                for (int i = inicio; i <= 50; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    Thread.sleep(150);

                    final int avance = i;
                    progresoProductos = avance;
                    final int porcentaje = (avance * 100) / 50;

                    Platform.runLater(() -> {
                        barraProgresoProductos.setProgress(avance / 50.0);
                        lblPorcentajeProductos.setText(porcentaje + "%");
                        lblDetalleProductos.setText(avance + " / 50 procesados");
                    });
                }

                Platform.runLater(() -> {
                    lblEstadoProductos.setText("Productos completados");
                    progresoProductos = 50;
                    btnIniciarProductos.setDisable(false);
                    btnDetenerProductos.setDisable(true);
                    btnDetenerProductos.setText("⏸ Pausar Productos");
                    btnReiniciarProductos.setDisable(false);
                    actualizarEstadoResumen();
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Platform.runLater(() -> {
                    lblEstadoProductos.setText("Pausado en " + progresoProductos + "/50");
                    btnIniciarProductos.setDisable(true);
                    btnDetenerProductos.setDisable(false);
                    btnDetenerProductos.setText("▶ Reanudar Productos");
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
            lblEstadoProductos.setText("Pausando productos...");
            btnDetenerProductos.setDisable(true);
            hiloProductos.interrupt();
        } else if (progresoProductos > 0 && progresoProductos < 50) {
            ejecutarHiloProductos();
        }
    }

    @FXML
    private void reiniciarProcesoProductos() {
        if (hiloProductos != null && hiloProductos.isAlive()) {
            hiloProductos.interrupt();
        }

        progresoProductos = 0;
        barraProgresoProductos.setProgress(0);
        lblPorcentajeProductos.setText("0%");
        lblDetalleProductos.setText("0 / 50 procesados");
        lblEstadoProductos.setText("Esperando inicio");
        btnIniciarProductos.setDisable(false);
        btnDetenerProductos.setDisable(true);
        btnDetenerProductos.setText("⏸ Pausar Productos");
        btnReiniciarProductos.setDisable(false);
        actualizarEstadoResumen();
    }

    @FXML
    private void iniciarAmbosProcesos() {
        if (hiloClientes == null || !hiloClientes.isAlive()) {
            if (progresoClientes > 0 && progresoClientes < 100) {
                ejecutarHiloClientes();
            } else {
                iniciarProcesoClientes();
            }
        }
        if (hiloProductos == null || !hiloProductos.isAlive()) {
            if (progresoProductos > 0 && progresoProductos < 50) {
                ejecutarHiloProductos();
            } else {
                iniciarProcesoProductos();
            }
        }
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
        boolean clientesPausado = !clientesActivo && (progresoClientes > 0 && progresoClientes < 100);
        boolean productosPausado = !productosActivo && (progresoProductos > 0 && progresoProductos < 50);

        if (clientesActivo && productosActivo) {
            lblResumenGeneral.setText("▶ Ejecutando concurrentemente: Clientes y Productos");
        } else if (clientesActivo) {
            lblResumenGeneral.setText("▶ Ejecutando Clientes" + (productosPausado ? " | ⏸ Productos pausado" : ""));
        } else if (productosActivo) {
            lblResumenGeneral.setText("▶ Ejecutando Productos" + (clientesPausado ? " | ⏸ Clientes pausado" : ""));
        } else if (clientesPausado || productosPausado) {
            lblResumenGeneral.setText("⏸ Procesos en pausa (Listos para reanudar)");
        } else {
            lblResumenGeneral.setText("Listo para iniciar tareas concurrentes");
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
