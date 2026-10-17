package com.lab2.quiz.api.dto;

import com.lab2.quiz.domain.Question;

// Вопрос для викторины: без правильного ответа.
public record QuizQuestionResponse(long id, String text, String category) {

    public static QuizQuestionResponse from(Question q) {
        return new QuizQuestionResponse(q.getId(), q.getText(), q.getCategory());
    }
}
