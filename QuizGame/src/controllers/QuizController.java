package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Question;
import models.Quiz;

import java.util.List;

public class QuizController {

    private Quiz currentQuiz;
    private List<Question> questions;
    private int currentQuestionIndex = 0;

    @FXML
    private Label quizTitleLabel;

    @FXML
    private Label questionLabel;

    @FXML
    private RadioButton optionARadio, optionBRadio, optionCRadio, optionDRadio;

    @FXML
    private ToggleGroup optionsGroup;

    @FXML
    private Label feedbackLabel;

    public void setQuiz(Quiz quiz) {
        this.currentQuiz = quiz;
        this.questions = quiz.getQuestions();  // assuming Quiz has getQuestions() returning List<Question>
        this.currentQuestionIndex = 0;
        quizTitleLabel.setText(quiz.getTitle());
        feedbackLabel.setText("");
        showQuestion(currentQuestionIndex);
    }

    private void showQuestion(int index) {
        if (questions == null || questions.isEmpty() || index >= questions.size()) {
            questionLabel.setText("No questions available.");
            disableOptions(true);
            return;
        }

        Question q = questions.get(index);
        questionLabel.setText((index + 1) + ". " + q.getQuestionText());
        optionARadio.setText(q.getOptionA());
        optionBRadio.setText(q.getOptionB());
        optionCRadio.setText(q.getOptionC());
        optionDRadio.setText(q.getOptionD());

        optionsGroup.selectToggle(null);
        feedbackLabel.setText("");
        disableOptions(false);
    }

    private void disableOptions(boolean disable) {
        optionARadio.setDisable(disable);
        optionBRadio.setDisable(disable);
        optionCRadio.setDisable(disable);
        optionDRadio.setDisable(disable);
    }

    @FXML
    private void handleSubmitAnswer() {
        RadioButton selected = (RadioButton) optionsGroup.getSelectedToggle();
        if (selected == null) {
            feedbackLabel.setText("Please select an answer.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        Question currentQ = questions.get(currentQuestionIndex);
        String selectedAnswer = selected.getText();

        boolean isCorrect = selectedAnswer.equals(currentQ.getCorrectOptionText());

        if (isCorrect) {
            feedbackLabel.setText("Correct!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
        } else {
            feedbackLabel.setText("Incorrect! Correct answer: " + currentQ.getCorrectOptionText());
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }

        disableOptions(true);

        // Move to next question after a short delay (e.g., 1.5 seconds)
        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            javafx.application.Platform.runLater(() -> {
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.size()) {
                    showQuestion(currentQuestionIndex);
                } else {
                    questionLabel.setText("Quiz completed! Well done.");
                    quizTitleLabel.setText("");
                    optionARadio.setVisible(false);
                    optionBRadio.setVisible(false);
                    optionCRadio.setVisible(false);
                    optionDRadio.setVisible(false);
                    feedbackLabel.setText("");
                }
            });
        }).start();
    }
}
