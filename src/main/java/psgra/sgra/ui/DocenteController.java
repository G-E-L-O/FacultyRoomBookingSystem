package psgra.sgra.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DocenteController implements Initializable {

    @FXML
    private VBox fondoDocente;

    private void cargarVistaEnFondoDocente(String rutaFXML) {
        try {
            Node nuevaVista = FXMLLoader.load(getClass().getResource(rutaFXML));
            fondoDocente.getChildren().setAll(nuevaVista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Se ejecuta automáticamente al cargar el FXML
        cargarVistaEnFondoDocente("/psgra/sgra/inicioDocente.fxml");
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
