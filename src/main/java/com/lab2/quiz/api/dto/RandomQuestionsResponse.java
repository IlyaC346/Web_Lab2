package com.lab2.quiz.api.dto;

import java.util.List;

public record RandomQuestionsResponse(List<QuizQuestionResponse> items) {
}
