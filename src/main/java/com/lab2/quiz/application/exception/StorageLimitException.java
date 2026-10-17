package com.lab2.quiz.application.exception;

// В хранилище закончилось место.
public class StorageLimitException extends RuntimeException {

    public StorageLimitException(int capacity) {
        super("Достигнут лимит хранилища: " + capacity + " записей");
    }
}
