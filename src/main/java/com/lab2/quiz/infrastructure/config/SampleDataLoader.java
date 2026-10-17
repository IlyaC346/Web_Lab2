package com.lab2.quiz.infrastructure.config;

import com.lab2.quiz.application.QuestionService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// Заполняет хранилище начальными вопросами при запуске.
@Component
public class SampleDataLoader implements ApplicationRunner {

    private final QuestionService questionService;

    public SampleDataLoader(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    public void run(ApplicationArguments args) {
        questionService.create("Сколько будет 2 + 2 * 2?", "6", "Математика");
        questionService.create("Столица Франции?", "Париж", "География");
        questionService.create("Какой тег HTML создаёт гиперссылку?", "a", "Веб");
        questionService.create("Как называется самая длинная река в мире?", "Амазонка", "География");
        questionService.create("Какой протокол используется для передачи веб-страниц?", "HTTP", "Веб");
        questionService.create("Сколько байт в одном килобайте (в двоичной системе)?", "1024", "Информатика");
        questionService.create("Кто написал роман «Война и мир»?", "Толстой", "Литература");
        questionService.create("Химический символ золота?", "Au", "Химия");
    }
}
