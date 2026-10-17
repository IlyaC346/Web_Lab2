package com.lab2.quiz.application.port;

import com.lab2.quiz.domain.Feedback;

import java.util.List;

// Хранилище сообщений обратной связи.
public interface FeedbackRepository {

    void save(Feedback feedback);

    List<Feedback> findAll();
}
