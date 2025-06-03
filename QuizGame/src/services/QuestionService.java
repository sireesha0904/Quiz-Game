package services;

import models.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuestionService {

    private List<Question> questions = new ArrayList<>();

    // Add a question and return true if successful
    public boolean addQuestion(Question question) {
        try {
            questions.add(question);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all questions
    public List<Question> getAllQuestions() {
        return new ArrayList<>(questions);
    }

    // Find question by ID (assuming Question has an ID field)
    public Optional<Question> getQuestionById(int id) {
        return questions.stream()
                .filter(q -> q.getId() == id)
                .findFirst();
    }

    // Update an existing question
    public boolean updateQuestion(Question updatedQuestion) {
        try {
            for (int i = 0; i < questions.size(); i++) {
                if (questions.get(i).getId() == updatedQuestion.getId()) {
                    questions.set(i, updatedQuestion);
                    return true;
                }
            }
            return false;  // Question not found
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a question by ID
    public boolean deleteQuestion(int id) {
        return questions.removeIf(q -> q.getId() == id);
    }
}
