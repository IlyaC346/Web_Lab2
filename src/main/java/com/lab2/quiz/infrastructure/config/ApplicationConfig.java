package com.lab2.quiz.infrastructure.config;

import com.lab2.quiz.application.FeedbackService;
import com.lab2.quiz.application.QuestionService;
import com.lab2.quiz.application.port.FeedbackRepository;
import com.lab2.quiz.application.port.QuestionRepository;
import com.lab2.quiz.infrastructure.persistence.InMemoryFeedbackRepository;
import com.lab2.quiz.infrastructure.persistence.InMemoryQuestionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Связывает слои: подставляет реализации хранилищ в сервисы приложения.
// Классы domain и application ничего не знают о Spring.
@Configuration
public class ApplicationConfig {

    @Bean
    public QuestionRepository questionRepository(@Value("${quiz.storage.capacity:50}") int capacity) {
        return new InMemoryQuestionRepository(capacity);
    }

    @Bean
    public FeedbackRepository feedbackRepository(@Value("${quiz.storage.capacity:50}") int capacity) {
        return new InMemoryFeedbackRepository(capacity);
    }

    @Bean
    public QuestionService questionService(QuestionRepository questionRepository) {
        return new QuestionService(questionRepository);
    }

    @Bean
    public FeedbackService feedbackService(FeedbackRepository feedbackRepository) {
        return new FeedbackService(feedbackRepository);
    }
}
