package ni.edu.uam.practicahilos;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.SnapshotParameters;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GeneradorCapturas extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("procesador-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Práctica - Hilos en JavaFX");
        stage.setScene(scene);
        stage.show();

        TabPane tabPane = (TabPane) scene.getRoot().lookup(".tab-pane");
        Label lblEstado = (Label) scene.lookup("#lblEstado");
        ProgressBar barraProgreso = (ProgressBar) scene.lookup("#barraProgreso");
        Label lblPorcentaje = (Label) scene.lookup("#lblPorcentaje");
        Button btnIniciar = (Button) scene.lookup("#btnIniciar");
        Button btnDetener = (Button) scene.lookup("#btnDetener");
        Button btnReiniciar = (Button) scene.lookup("#btnReiniciar");

        File outDir = new File("docs/img");
        outDir.mkdirs();

        // Captura 1: Estado Inicial Práctica Guiada
        saveNode(scene.getRoot(), new File(outDir, "01_practica_guiada_inicio.png"));

        // Captura 2: Procesamiento en progreso
        Platform.runLater(() -> {
            lblEstado.setText("Procesando registros...");
            barraProgreso.setProgress(0.46);
            lblPorcentaje.setText("46%");
            btnIniciar.setDisable(true);
            btnDetener.setDisable(false);
            btnDetener.setText("⏸ Pausar");
            btnReiniciar.setDisable(true);

            Platform.runLater(() -> {
                saveNode(scene.getRoot(), new File(outDir, "02_practica_guiada_progreso.png"));

                // Captura 3: Proceso Pausado / Reanudable (el botón se convierte en "▶ Reanudar")
                lblEstado.setText("Proceso pausado (46% completado)");
                barraProgreso.setProgress(0.46);
                lblPorcentaje.setText("46%");
                btnIniciar.setDisable(true);
                btnDetener.setDisable(false);
                btnDetener.setText("▶ Reanudar");
                btnReiniciar.setDisable(false);

                Platform.runLater(() -> {
                    saveNode(scene.getRoot(), new File(outDir, "03_practica_guiada_pausado_reanudable.png"));
                    // Mantener compatibilidad con nombre previo
                    saveNode(scene.getRoot(), new File(outDir, "03_practica_guiada_detenido.png"));

                    // Captura 4: Proceso completado al 100%
                    lblEstado.setText("Proceso finalizado");
                    barraProgreso.setProgress(1.0);
                    lblPorcentaje.setText("100%");
                    btnIniciar.setDisable(false);
                    btnDetener.setDisable(true);
                    btnDetener.setText("⏸ Pausar");
                    btnReiniciar.setDisable(false);

                    Platform.runLater(() -> {
                        saveNode(scene.getRoot(), new File(outDir, "04_practica_guiada_finalizado.png"));

                        // Cambiar a Tab 2: Reto Final
                        tabPane.getSelectionModel().select(1);

                        Platform.runLater(() -> {
                            // Captura 5: Reto Final en progreso concurrente
                            Label lblEstadoClientes = (Label) scene.lookup("#lblEstadoClientes");
                            ProgressBar barraProgresoClientes = (ProgressBar) scene.lookup("#barraProgresoClientes");
                            Label lblPorcentajeClientes = (Label) scene.lookup("#lblPorcentajeClientes");
                            Label lblDetalleClientes = (Label) scene.lookup("#lblDetalleClientes");
                            Button btnIniciarClientes = (Button) scene.lookup("#btnIniciarClientes");
                            Button btnDetenerClientes = (Button) scene.lookup("#btnDetenerClientes");

                            Label lblEstadoProductos = (Label) scene.lookup("#lblEstadoProductos");
                            ProgressBar barraProgresoProductos = (ProgressBar) scene.lookup("#barraProgresoProductos");
                            Label lblPorcentajeProductos = (Label) scene.lookup("#lblPorcentajeProductos");
                            Label lblDetalleProductos = (Label) scene.lookup("#lblDetalleProductos");
                            Button btnIniciarProductos = (Button) scene.lookup("#btnIniciarProductos");
                            Button btnDetenerProductos = (Button) scene.lookup("#btnDetenerProductos");

                            Label lblResumen = (Label) scene.lookup("#lblResumenGeneral");

                            lblEstadoClientes.setText("Procesando clientes...");
                            barraProgresoClientes.setProgress(0.68);
                            lblPorcentajeClientes.setText("68%");
                            lblDetalleClientes.setText("68 / 100 procesados");
                            btnIniciarClientes.setDisable(true);
                            btnDetenerClientes.setDisable(false);
                            btnDetenerClientes.setText("⏸ Pausar Clientes");

                            lblEstadoProductos.setText("Pausado en 22/50");
                            barraProgresoProductos.setProgress(0.44);
                            lblPorcentajeProductos.setText("44%");
                            lblDetalleProductos.setText("22 / 50 procesados");
                            btnIniciarProductos.setDisable(true);
                            btnDetenerProductos.setDisable(false);
                            btnDetenerProductos.setText("▶ Reanudar Productos");

                            lblResumen.setText("▶ Ejecutando Clientes | ⏸ Productos pausado");

                            Platform.runLater(() -> {
                                saveNode(scene.getRoot(), new File(outDir, "05_reto_final_concurrente.png"));

                                // Captura 6: Reto Final completado
                                lblEstadoClientes.setText("Clientes completados");
                                barraProgresoClientes.setProgress(1.0);
                                lblPorcentajeClientes.setText("100%");
                                lblDetalleClientes.setText("100 / 100 procesados");
                                btnIniciarClientes.setDisable(false);
                                btnDetenerClientes.setDisable(true);
                                btnDetenerClientes.setText("⏸ Pausar Clientes");

                                lblEstadoProductos.setText("Productos completados");
                                barraProgresoProductos.setProgress(1.0);
                                lblPorcentajeProductos.setText("100%");
                                lblDetalleProductos.setText("50 / 50 procesados");
                                btnIniciarProductos.setDisable(false);
                                btnDetenerProductos.setDisable(true);
                                btnDetenerProductos.setText("⏸ Pausar Productos");

                                lblResumen.setText("Todos los procesos han finalizado con éxito");

                                Platform.runLater(() -> {
                                    saveNode(scene.getRoot(), new File(outDir, "06_reto_final_completado.png"));
                                    stage.close();
                                    Platform.exit();
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    private void saveNode(javafx.scene.Node node, File file) {
        SnapshotParameters params = new SnapshotParameters();
        WritableImage image = node.snapshot(params, null);
        int w = (int) image.getWidth();
        int h = (int) image.getHeight();
        BufferedImage bImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        PixelReader pr = image.getPixelReader();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                bImage.setRGB(x, y, pr.getArgb(x, y));
            }
        }
        try {
            ImageIO.write(bImage, "png", file);
            System.out.println("Guardada captura: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
