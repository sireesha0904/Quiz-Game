package services;

import models.Quiz;
import models.Question;

import java.util.ArrayList;
import java.util.List;

public class QuizService {

    private List<Quiz> quizList = new ArrayList<>();
    private int nextQuizId = 1;

    private QuestionService questionService = new QuestionService();

    // Create a new quiz
    public boolean createQuiz(Quiz quiz) {
        if (quiz == null) return false;

        quiz.setId(nextQuizId++);
        quizList.add(quiz);
        return true;
    }

    // Get all quizzes
    public List<Quiz> getAllQuizzes() {
        // For each quiz, load its questions from questionService
        for (Quiz quiz : quizList) {
            List<Question> questions = questionService.getQuestionsByQuizId(quiz.getId());
            quiz.setQuestions(questions);
        }
        return new ArrayList<>(quizList);
    }

    // Update a quiz
    public boolean updateQuiz(Quiz updatedQuiz) {
        for (int i = 0; i < quizList.size(); i++) {
            if (quizList.get(i).getId() == updatedQuiz.getId()) {
                quizList.set(i, updatedQuiz);
                return true;
            }
        }
        return false;
    }

    // Delete a quiz by ID (also delete its questions)
    public boolean deleteQuiz(int quizId) {
        // Delete all questions of this quiz
        List<Question> questions = questionService.getQuestionsByQuizId(quizId);
        for (Question q : questions) {
            questionService.deleteQuestion(q.getId());
        }
        // Delete the quiz
        return quizList.removeIf(q -> q.getId() == quizId);
    }
}
