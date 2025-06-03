package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Question;
import models.Quiz;

import java.util.ArrayList;
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

    @FXML
    private Button submitAnswerBtn;

    // Call this method to set the quiz and start showing questions
    public void setQuiz(Quiz quiz) {
        this.currentQuiz = quiz;
        this.questions = quiz.getQuestions() != null ? quiz.getQuestions() : new ArrayList<>();
        this.currentQuestionIndex = 0;

        quizTitleLabel.setText(quiz.getTitle());
        feedbackLabel.setText("");
        submitAnswerBtn.setDisable(false);

        showQuestion(currentQuestionIndex);
    }

    private void showQuestion(int index) {
        if (questions.isEmpty() || index >= questions.size()) {
            questionLabel.setText("No questions available or quiz completed.");
            disableOptions(true);
            submitAnswerBtn.setDisable(true);
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
        submitAnswerBtn.setDisable(false);
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
        submitAnswerBtn.setDisable(true);

        // After 1.5 seconds, show next question or finish quiz
        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
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
