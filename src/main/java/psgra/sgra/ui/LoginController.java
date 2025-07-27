package psgra.sgra.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Verificación simple de login (debes reemplazar con lógica real)
        if (email.equals("") && password.equals("")) {
            cargarVista("psgra/sgra/DocenteView.fxml");
        } else if (email.equals("coordinador@correo.com") && password.equals("admin")) {
            cargarVista("psgra/sgra/coordinadorView.fxml");
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Credenciales inválidas");
            alert.setContentText("Verifica tu correo y contraseña.");
            alert.showAndWait();
        }
    }

    private void cargarVista(String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + rutaFXML));
            Parent root = loader.load();

            // Obtener la ventana actual
            Stage stage = (Stage) emailField.getScene().getWindow();

            // Aplicar la nueva escena
            Scene nuevaEscena = new Scene(root);
            stage.setScene(nuevaEscena);

            // Maximizar la ventana correctamente
            stage.setMaximized(true); // Esto funciona si se llama después del setScene

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}