package com.lab2.quiz.application;

import com.lab2.quiz.application.port.FeedbackRepository;
import com.lab2.quiz.domain.Feedback;

// Сценарий отправки сообщения со страницы контактов.
public class FeedbackService {

    private final FeedbackRepository repository;

    public FeedbackService(FeedbackRepository repository) {
        this.repository = repository;
    }

    public void submit(String name, String email, String message) {
        repository.save(Feedback.create(name, email, message));
    }
}
