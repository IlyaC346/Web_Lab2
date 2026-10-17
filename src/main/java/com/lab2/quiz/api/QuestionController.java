package com.lab2.quiz.api;

import com.lab2.quiz.api.dto.CheckAnswerRequest;
import com.lab2.quiz.api.dto.CheckAnswerResponse;
import com.lab2.quiz.api.dto.OkResponse;
import com.lab2.quiz.api.dto.QuestionListResponse;
import com.lab2.quiz.api.dto.QuestionRequest;
import com.lab2.quiz.api.dto.QuestionResponse;
import com.lab2.quiz.api.dto.QuizQuestionResponse;
import com.lab2.quiz.api.dto.RandomQuestionsResponse;
import com.lab2.quiz.application.QuestionService;
import com.lab2.quiz.domain.Question;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public QuestionListResponse getAll() {
        List<Question> questions = questionService.getAll();
        List<QuestionResponse> items = questions.stream().map(QuestionResponse::from).toList();
        return new QuestionListResponse(items, items.size(), questionService.getCapacity());
    }

    @GetMapping("/random")
    public RandomQuestionsResponse getRandom(@RequestParam(required = false) Integer count,
                                             @RequestParam(required = false) String category) {
        List<QuizQuestionResponse> items = questionService.getRandom(count, category).stream()
                .map(QuizQuestionResponse::from)
                .toList();
        return new RandomQuestionsResponse(items);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionResponse create(@RequestBody QuestionRequest request) {
        Question question = questionService.create(request.text(), request.answer(), request.category());
        return QuestionResponse.from(question);
    }

    @PutMapping("/{id}")
    public QuestionResponse update(@PathVariable long id, @RequestBody QuestionRequest request) {
        Question question = questionService.update(id, request.text(), request.answer(), request.category());
        return QuestionResponse.from(question);
    }

    @DeleteMapping("/{id}")
    public OkResponse delete(@PathVariable long id) {
        questionService.delete(id);
        return OkResponse.success();
    }

    @PostMapping("/{id}/check")
    public CheckAnswerResponse check(@PathVariable long id, @RequestBody CheckAnswerRequest request) {
        return CheckAnswerResponse.from(questionService.checkAnswer(id, request.answer()));
    }
}
