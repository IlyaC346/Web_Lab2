package com.lab2.quiz.api.dto;

import com.lab2.quiz.domain.Question;

import java.time.Instant;

public record QuestionResponse(long id, String text, String answer, String category, Instant createdAt) {

    public static QuestionResponse from(Question q) {
        return new QuestionResponse(q.getId(), q.getText(), q.getAnswer(), q.getCategory(), q.getCreatedAt());
    }
}
