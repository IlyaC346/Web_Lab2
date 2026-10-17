package com.lab2.quiz.api.dto;

public record OkResponse(boolean ok) {

    public static OkResponse success() {
        return new OkResponse(true);
    }
}
