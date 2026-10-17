package com.lab2.quiz.domain;

import java.time.Instant;
import java.util.Locale;

// Вопрос викторины. Сам проверяет корректность своих данных и правильность ответа.
public class Question {

    public static final int TEXT_MIN_LENGTH = 3;
    public static final int TEXT_MAX_LENGTH = 300;
    public static final int ANSWER_MAX_LENGTH = 100;
    public static final int CATEGORY_MAX_LENGTH = 40;
    public static final String DEFAULT_CATEGORY = "Общее";

    private Long id;
    private String text;
    private String answer;
    private String category;
    private final Instant createdAt;

    private Question(String text, String answer, String category, Instant createdAt) {
        this.createdAt = createdAt;
        setData(text, answer, category);
    }

    public static Question create(String text, String answer, String category) {
        return new Question(text, answer, category, Instant.now());
    }

    public void update(String text, String answer, String category) {
        setData(text, answer, category);
    }

    // Ответ считается верным без учёта регистра, лишних пробелов и различия «е»/«ё».
    public boolean isCorrectAnswer(String userAnswer) {
        return normalize(userAnswer).equals(normalize(answer));
    }

    public void assignId(long id) {
        if (this.id != null) {
            throw new IllegalStateException("Идентификатор уже назначен");
        }
        this.id = id;
    }

    private void setData(String text, String answer, String category) {
        String t = trim(text);
        String a = trim(answer);
        String c = trim(category);

        if (t.length() < TEXT_MIN_LENGTH) {
            throw new DomainValidationException("Текст вопроса должен содержать минимум " + TEXT_MIN_LENGTH + " символа");
        }
        if (t.length() > TEXT_MAX_LENGTH) {
            throw new DomainValidationException("Текст вопроса не длиннее " + TEXT_MAX_LENGTH + " символов");
        }
        if (a.isEmpty()) {
            throw new DomainValidationException("Укажите правильный ответ");
        }
        if (a.length() > ANSWER_MAX_LENGTH) {
            throw new DomainValidationException("Ответ не длиннее " + ANSWER_MAX_LENGTH + " символов");
        }
        if (c.length() > CATEGORY_MAX_LENGTH) {
            throw new DomainValidationException("Категория не длиннее " + CATEGORY_MAX_LENGTH + " символов");
        }

        this.text = t;
        this.answer = a;
        this.category = c.isEmpty() ? DEFAULT_CATEGORY : c;
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private static String normalize(String s) {
        return trim(s).toLowerCase(Locale.ROOT).replace('ё', 'е').replaceAll("\\s+", " ");
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getAnswer() {
        return answer;
    }

    public String getCategory() {
        return category;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
