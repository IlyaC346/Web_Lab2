package com.lab2.quiz.api;

import com.lab2.quiz.api.dto.FeedbackRequest;
import com.lab2.quiz.api.dto.OkResponse;
import com.lab2.quiz.application.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OkResponse submit(@RequestBody FeedbackRequest request) {
        feedbackService.submit(request.name(), request.email(), request.message());
        return OkResponse.success();
    }
}
