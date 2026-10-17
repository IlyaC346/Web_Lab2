package com.lab2.quiz.infrastructure.persistence;

import com.lab2.quiz.application.exception.StorageLimitException;
import com.lab2.quiz.application.port.QuestionRepository;
import com.lab2.quiz.domain.Question;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Хранилище вопросов в оперативной памяти с ограничением на количество записей.
// Данные теряются при перезапуске приложения.
public class InMemoryQuestionRepository implements QuestionRepository {

    private final Map<Long, Question> storage = new LinkedHashMap<>();
    private final int capacity;
    private long nextId = 1;

    public InMemoryQuestionRepository(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public synchronized List<Question> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public synchronized Optional<Question> findById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public synchronized Question save(Question question) {
        if (question.getId() == null) {
            if (storage.size() >= capacity) {
                throw new StorageLimitException(capacity);
            }
            question.assignId(nextId++);
        }
        storage.put(question.getId(), question);
        return question;
    }

    @Override
    public synchronized boolean deleteById(long id) {
        return storage.remove(id) != null;
    }

    @Override
    public synchronized int count() {
        return storage.size();
    }

    @Override
    public int capacity() {
        return capacity;
    }
}
