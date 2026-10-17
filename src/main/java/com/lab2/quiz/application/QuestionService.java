package com.lab2.quiz.application;

import com.lab2.quiz.application.exception.NotFoundException;
import com.lab2.quiz.application.exception.StorageLimitException;
import com.lab2.quiz.application.port.QuestionRepository;
import com.lab2.quiz.domain.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Сценарии работы с вопросами: просмотр, добавление, изменение, удаление,
// выдача случайных вопросов и проверка ответа.
public class QuestionService {

    private static final int DEFAULT_RANDOM_COUNT = 5;

    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public List<Question> getAll() {
        return repository.findAll();
    }

    public int getCapacity() {
        return repository.capacity();
    }

    public List<Question> getRandom(Integer count, String category) {
        int n = (count == null || count < 1) ? DEFAULT_RANDOM_COUNT : count;

        List<Question> pool = new ArrayList<>();
        for (Question q : repository.findAll()) {
            if (category == null || category.isBlank() || q.getCategory().equals(category)) {
                pool.add(q);
            }
        }
        Collections.shuffle(pool);
        return pool.subList(0, Math.min(n, pool.size()));
    }

    public Question create(String text, String answer, String category) {
        if (repository.count() >= repository.capacity()) {
            throw new StorageLimitException(repository.capacity());
        }
        Question question = Question.create(text, answer, category);
        return repository.save(question);
    }

    public Question update(long id, String text, String answer, String category) {
        Question question = findOrThrow(id);
        question.update(text, answer, category);
        return repository.save(question);
    }

    public void delete(long id) {
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Вопрос не найден");
        }
    }

    public AnswerCheckResult checkAnswer(long id, String userAnswer) {
        Question question = findOrThrow(id);
        return new AnswerCheckResult(question.isCorrectAnswer(userAnswer), question.getAnswer());
    }

    private Question findOrThrow(long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Вопрос не найден"));
    }
}
