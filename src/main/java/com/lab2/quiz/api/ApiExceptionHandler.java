package com.lab2.quiz.api;

import com.lab2.quiz.api.dto.ErrorResponse;
import com.lab2.quiz.application.exception.NotFoundException;
import com.lab2.quiz.application.exception.StorageLimitException;
import com.lab2.quiz.domain.DomainValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

// Превращает исключения нижних слоёв в HTTP-ответы вида {"error": "..."}.
@RestControllerAdvice(basePackages = "com.lab2.quiz.api")
public class ApiExceptionHandler {

    @ExceptionHandler(DomainValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(DomainValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(StorageLimitException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleStorageLimit(StorageLimitException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadJson() {
        return new ErrorResponse("Некорректный запрос");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadParameter() {
        return new ErrorResponse("Некорректный параметр запроса");
    }
}
