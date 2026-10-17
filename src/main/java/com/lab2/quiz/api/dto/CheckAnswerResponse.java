package com.lab2.quiz.api.dto;

import com.lab2.quiz.application.AnswerCheckResult;

public record CheckAnswerResponse(boolean correct, String correctAnswer) {

    public static CheckAnswerResponse from(AnswerCheckResult result) {
        return new CheckAnswerResponse(result.correct(), result.correctAnswer());
    }
}
