package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Question;
import models.Quiz;
import services.QuestionService;
import services.QuizService;

public class QuizManagementController {

    @FXML
    private TextField quizTitleField;

    @FXML
    private TextArea quizDescriptionArea;

    @FXML
    private ListView<Quiz> quizListView;

    @FXML
    private TextArea questionTextArea;

    @FXML
    private TextField optionAField, optionBField, optionCField, optionDField;

    @FXML
    private ChoiceBox<String> correctOptionChoiceBox;

    @FXML
    private Button addQuizBtn, updateQuizBtn, deleteQuizBtn;

    @FXML
    private Button addQuestionBtn;

    private QuizService quizService = new QuizService();
    private QuestionService questionService = new QuestionService();

    private ObservableList<Quiz> quizzes;

    @FXML
    public void initialize() {
        // Load quizzes from DB
        quizzes = FXCollections.observableArrayList(quizService.getAllQuizzes());
        quizListView.setItems(quizzes);

        correctOptionChoiceBox.setItems(FXCollections.observableArrayList("A", "B", "C", "D"));

        quizListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadQuizDetails(newVal);
            }
        });
    }

    private void loadQuizDetails(Quiz quiz) {
        quizTitleField.setText(quiz.getTitle());
        quizDescriptionArea.setText(quiz.getDescription());
        clearQuestionFields();
    }

    private void clearQuizFields() {
        quizTitleField.clear();
        quizDescriptionArea.clear();
        quizListView.getSelectionModel().clearSelection();
    }

    private void clearQuestionFields() {
        questionTextArea.clear();
        optionAField.clear();
        optionBField.clear();
        optionCField.clear();
        optionDField.clear();
        correctOptionChoiceBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void addQuiz() {
        String title = quizTitleField.getText();
        String description = quizDescriptionArea.getText();

        if (title.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Quiz title is required!");
            return;
        }

        Quiz newQuiz = new Quiz();
        newQuiz.setTitle(title);
        newQuiz.setDescription(description);

        boolean success = quizService.createQuiz(newQuiz);
        if (success) {
            quizzes.add(newQuiz);
            showAlert(Alert.AlertType.INFORMATION, "Quiz added successfully!");
            clearQuizFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to add quiz.");
        }
    }

    @FXML
    private void updateQuiz() {
        Quiz selectedQuiz = quizListView.getSelectionModel().getSelectedItem();
        if (selectedQuiz == null) {
            showAlert(Alert.AlertType.WARNING, "Select a quiz to update.");
            return;
        }

        selectedQuiz.setTitle(quizTitleField.getText());
        selectedQuiz.setDescription(quizDescriptionArea.getText());

        boolean success = quizService.updateQuiz(selectedQuiz);
        if (success) {
            quizListView.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Quiz updated successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to update quiz.");
        }
    }

    @FXML
    private void deleteQuiz() {
        Quiz selectedQuiz = quizListView.getSelectionModel().getSelectedItem();
        if (selectedQuiz == null) {
            showAlert(Alert.AlertType.WARNING, "Select a quiz to delete.");
            return;
        }

        boolean success = quizService.deleteQuiz(selectedQuiz.getId());
        if (success) {
            quizzes.remove(selectedQuiz);
            showAlert(Alert.AlertType.INFORMATION, "Quiz deleted successfully!");
            clearQuizFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to delete quiz.");
        }
    }

    @FXML
    private void addQuestion() {
        Quiz selectedQuiz = quizListView.getSelectionModel().getSelectedItem();
        if (selectedQuiz == null) {
            showAlert(Alert.AlertType.WARNING, "Select a quiz first.");
            return;
        }

        String questionText = questionTextArea.getText();
        String optA = optionAField.getText();
        String optB = optionBField.getText();
        String optC = optionCField.getText();
        String optD = optionDField.getText();
        String correctOpt = correctOptionChoiceBox.getValue();

        if (questionText.isEmpty() || optA.isEmpty() || optB.isEmpty() || optC.isEmpty() || optD.isEmpty() || correctOpt == null) {
            showAlert(Alert.AlertType.WARNING, "All fields must be filled.");
            return;
        }

        Question question = new Question();
        question.setQuizId(selectedQuiz.getId());
        question.setText(questionText);
        question.setOptionA(optA);
        question.setOptionB(optB);
        question.setOptionC(optC);
        question.setOptionD(optD);
        question.setCorrectOption(correctOpt);

        boolean success = questionService.addQuestion(question);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Question added successfully!");
            clearQuestionFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to add question.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType.toString());
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
