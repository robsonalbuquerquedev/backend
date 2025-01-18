package br.edu.ifpe.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifpe.manager.dto.FeedbackDTO;
import br.edu.ifpe.manager.request.FeedbackRequest;
import br.edu.ifpe.manager.service.FeedbackService;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackDTO> enviarFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        FeedbackDTO feedbackDTO = feedbackService.salvarFeedback(feedbackRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackDTO);
    }
}