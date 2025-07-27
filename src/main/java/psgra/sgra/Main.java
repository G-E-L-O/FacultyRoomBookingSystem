package psgra.sgra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/psgra/sgra/loginview.fxml")); // ✅ Asegúrate de que este path sea correcto
        Scene scene = new Scene(loader.load());

        // Cargar estilos
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/psgra/sgra/styles.css")).toExternalForm());

        // Título y configuración de ventana
        stage.setTitle("Sistema de Reserva de Aulas");
        stage.setScene(scene);
        stage.setMaximized(true); // ✅ Esto hace que inicie maximizado
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
