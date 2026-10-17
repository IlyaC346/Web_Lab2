package com.lab2.quiz.application;

import com.lab2.quiz.application.exception.NotFoundException;
import com.lab2.quiz.application.exception.StorageLimitException;
import com.lab2.quiz.domain.DomainValidationException;
import com.lab2.quiz.domain.Question;
import com.lab2.quiz.infrastructure.persistence.InMemoryQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionServiceTest {

    private QuestionService service;

    @BeforeEach
    void setUp() {
        service = new QuestionService(new InMemoryQuestionRepository(3));
    }

    @Test
    void createsQuestionWithDefaultCategory() {
        Question q = service.create("Столица Италии?", "Рим", "");
        assertEquals(1L, q.getId());
        assertEquals(Question.DEFAULT_CATEGORY, q.getCategory());
    }

    @Test
    void rejectsInvalidQuestion() {
        assertThrows(DomainValidationException.class, () -> service.create("ab", "x", null));
        assertThrows(DomainValidationException.class, () -> service.create("Вопрос?", " ", null));
    }

    @Test
    void doesNotExceedCapacity() {
        service.create("Вопрос 1", "1", null);
        service.create("Вопрос 2", "2", null);
        service.create("Вопрос 3", "3", null);
        assertThrows(StorageLimitException.class, () -> service.create("Вопрос 4", "4", null));
    }

    @Test
    void checksAnswerIgnoringCaseSpacesAndYo() {
        Question q = service.create("Кто написал «Ёжика в тумане»?", "Сергей Козлов", null);
        assertTrue(service.checkAnswer(q.getId(), "  сергей   козлов ").correct());
        assertFalse(service.checkAnswer(q.getId(), "Пушкин").correct());

        Question q2 = service.create("Колючее животное?", "Ёж", null);
        assertTrue(service.checkAnswer(q2.getId(), "еж").correct());
    }

    @Test
    void updatesAndDeletes() {
        Question q = service.create("Вопрос", "1", null);
        service.update(q.getId(), "Новый вопрос", "2", "Тест");
        assertEquals("Новый вопрос", service.getAll().get(0).getText());

        service.delete(q.getId());
        assertTrue(service.getAll().isEmpty());
        assertThrows(NotFoundException.class, () -> service.delete(q.getId()));
    }

    @Test
    void randomReturnsAtMostAvailable() {
        service.create("Вопрос 1", "1", "А");
        service.create("Вопрос 2", "2", "Б");
        assertEquals(2, service.getRandom(10, null).size());
        assertEquals(1, service.getRandom(10, "А").size());
    }
}
