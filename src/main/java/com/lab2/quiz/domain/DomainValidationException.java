package com.lab2.quiz.domain;

// Данные не прошли проверку правил предметной области.
public class DomainValidationException extends RuntimeException {

    public DomainValidationException(String message) {
        super(message);
    }
}
