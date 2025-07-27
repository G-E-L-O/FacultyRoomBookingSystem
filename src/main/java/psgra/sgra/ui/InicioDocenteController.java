package psgra.sgra.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InicioDocenteController implements Initializable {

    @FXML
    private VBox fondoDocente;

    @FXML
    private FlowPane contenedorTarjetas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contenedorTarjetas.widthProperty().addListener((obs, oldVal, newVal) -> ajustarTarjetas());
        contenedorTarjetas.heightProperty().addListener((obs, oldVal, newVal) -> ajustarTarjetas());
        ajustarTarjetas();
    }

    private void ajustarTarjetas() {
        double ancho = contenedorTarjetas.getWidth();
        double altoDisponible = fondoDocente.getHeight() - 100; // restamos encabezado

        if (ancho <= 0 || altoDisponible <= 0) return;

        int columnas = Math.max(1, (int) (ancho / 450));
        int filas = (int) Math.ceil((double) contenedorTarjetas.getChildren().size() / columnas);

        double anchoCard = (ancho - (contenedorTarjetas.getHgap() * (columnas + 1))) / columnas;
        anchoCard = Math.max(300, Math.min(anchoCard, 500));

        double alturaCard = (altoDisponible - (contenedorTarjetas.getVgap() * (filas + 1))) / filas;
        alturaCard = Math.max(150, Math.min(alturaCard, 300)); // límites

        for (Node nodo : contenedorTarjetas.getChildren()) {
            if (nodo instanceof VBox card) {
                card.setPrefWidth(anchoCard);
                card.setPrefHeight(alturaCard);

                for (Node elemento : card.getChildren()) {
                    if (elemento instanceof Label label) {
                        if (label.getStyleClass().toString().contains("card-title")) {
                            label.setStyle("-fx-font-size: " + (anchoCard / 16) + "px;");
                        } else if (label.getStyleClass().toString().contains("card-item")) {
                            label.setStyle("-fx-font-size: " + (anchoCard / 22) + "px;");
                        }
                    } else if (elemento instanceof Button boton) {
                        if (boton.getStyleClass().toString().contains("link-button")) {
                            boton.setStyle("-fx-font-size: " + (anchoCard / 22) + "px;");
                        }
                    }
                }
            }
        }
    }


    private void cargarVistaEnFondoDocente(String rutaFXML) {
        try {
            VBox nuevaVista = FXMLLoader.load(getClass().getResource(rutaFXML));
            fondoDocente.getChildren().setAll(nuevaVista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irAInicio(ActionEvent event) {
        cargarVistaEnFondoDocente("/psgra/sgra/inicioDocente.fxml");
    }

    @FXML
    private void irReservas(ActionEvent event) {
        cargarVistaEnFondoDocente("/psgra/sgra/reservasDocente.fxml");
    }

    @FXML
    private void irAEventos(ActionEvent event) {
        cargarVistaEnFondoDocente("/psgra/sgra/eventosDocente.fxml");
    }

    @FXML
    private void irAHorarios(ActionEvent event) {
        cargarVistaEnFondoDocente("/psgra/sgra/horariosDocente.fxml");
    }

    @FXML
    private void irAReportes(ActionEvent event) {
        cargarVistaEnFondoDocente("/psgra/sgra/reportesDocente.fxml");
    }

    @FXML
    private void abrirReservas(ActionEvent event) {
        irReservas(event);
    }
}
