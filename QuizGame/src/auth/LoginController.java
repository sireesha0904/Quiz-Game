package auth;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import services.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.User; // Make sure you have this model
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter username and password.");
            return;
        }

        User user = authService.loginUser(username, password);

        if (user != null) {
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Login successful! Welcome " + user.getFullName());

            try {
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Parent root;

                if ("admin".equalsIgnoreCase(user.getRole())) {
                    root = FXMLLoader.load(getClass().getResource("/fxml/quiz_management.fxml"));
                    stage.setTitle("Quiz Management - Admin");
                } else {
                    root = FXMLLoader.load(getClass().getResource("/fxml/dashboard.fxml"));
                    stage.setTitle("Dashboard - User");
                }

                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                messageLabel.setText("Failed to load next screen.");
            }

        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/register.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
