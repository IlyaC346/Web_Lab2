package com.lab2.quiz.api.dto;

import java.util.List;

public record QuestionListResponse(List<QuestionResponse> items, int count, int limit) {
}
