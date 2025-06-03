package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Quiz;
import services.QuizService;

import java.io.IOException;
import java.util.List;

public class DashboardController {

    @FXML
    private ListView<Quiz> quizListView;

    @FXML
    private Label messageLabel;

    private final QuizService quizService = new QuizService();

    @FXML
    public void initialize() {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        quizListView.getItems().addAll(quizzes);
    }

    @FXML
    private void handleStartQuiz() {
        Quiz selectedQuiz = quizListView.getSelectionModel().getSelectedItem();
        if (selectedQuiz == null) {
            messageLabel.setText("Please select a quiz.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/quiz_view.fxml"));
            Parent root = loader.load();

            // Pass quiz data to QuizController
            QuizController quizController = loader.getController();
            quizController.setQuiz(selectedQuiz);

            Stage stage = (Stage) quizListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Quiz: " + selectedQuiz.getTitle());

        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Failed to load quiz.");
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
