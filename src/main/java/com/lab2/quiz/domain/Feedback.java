package com.lab2.quiz.domain;

import java.time.Instant;
import java.util.regex.Pattern;

// Сообщение, отправленное со страницы контактов.
public class Feedback {

    public static final int MESSAGE_MIN_LENGTH = 5;
    public static final int MESSAGE_MAX_LENGTH = 500;
    private static final Pattern EMAIL = Pattern.compile("^\\S+@\\S+\\.\\S+$");

    private final String name;
    private final String email;
    private final String message;
    private final Instant date;

    private Feedback(String name, String email, String message, Instant date) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.date = date;
    }

    public static Feedback create(String name, String email, String message) {
        String n = name == null ? "" : name.trim();
        String e = email == null ? "" : email.trim();
        String m = message == null ? "" : message.trim();

        if (n.isEmpty()) {
            throw new DomainValidationException("Введите имя");
        }
        if (!EMAIL.matcher(e).matches()) {
            throw new DomainValidationException("Введите правильный e-mail");
        }
        if (m.length() < MESSAGE_MIN_LENGTH) {
            throw new DomainValidationException("Сообщение должно быть не короче " + MESSAGE_MIN_LENGTH + " символов");
        }
        if (m.length() > MESSAGE_MAX_LENGTH) {
            throw new DomainValidationException("Сообщение не длиннее " + MESSAGE_MAX_LENGTH + " символов");
        }
        return new Feedback(n, e, m, Instant.now());
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public Instant getDate() {
        return date;
    }
}
