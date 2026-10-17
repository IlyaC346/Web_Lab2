package com.lab2.quiz.application.port;

import com.lab2.quiz.domain.Question;

import java.util.List;
import java.util.Optional;

// Хранилище вопросов. Реализация находится в слое инфраструктуры.
public interface QuestionRepository {

    List<Question> findAll();

    Optional<Question> findById(long id);

    // Сохраняет новый вопрос (назначая ему id) или обновляет существующий.
    Question save(Question question);

    boolean deleteById(long id);

    int count();

    // Максимальное количество записей в хранилище.
    int capacity();
}
