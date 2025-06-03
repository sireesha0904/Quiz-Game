package services;

import models.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionService {

    private List<Question> questionList = new ArrayList<>();
    private int nextQuestionId = 1;

    // Add a new question
    public boolean addQuestion(Question question) {
        if (question == null) return false;

        question.setId(nextQuestionId++);
        questionList.add(question);
        return true;
    }

    // Get questions by quiz ID
    public List<Question> getQuestionsByQuizId(int quizId) {
        return questionList.stream()
                .filter(q -> q.getQuizId() == quizId)
                .collect(Collectors.toList());
    }

    // Delete question by ID
    public boolean deleteQuestion(int questionId) {
        return questionList.removeIf(q -> q.getId() == questionId);
    }

    // Optional: Get all questions (useful for debugging/testing)
    public List<Question> getAllQuestions() {
        return new ArrayList<>(questionList);
    }
}
