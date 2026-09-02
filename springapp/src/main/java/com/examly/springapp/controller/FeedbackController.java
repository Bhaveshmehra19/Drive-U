package com.examly.springapp.controller;

import com.examly.springapp.model.Feedback;
import com.examly.springapp.service.FeedbackService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/feedback")
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {

        try {
            Feedback createdFeedback = feedbackService.createFeedback(feedback);
            return ResponseEntity.status(201).body(createdFeedback);
        } catch (Exception e) {
            return ResponseEntity.status(409).build();
        }
    }

    @GetMapping("/feedback/{feedbackId}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long feedbackId) {
    
        try {
            Feedback feedback = feedbackService.getFeedbackById(feedbackId);
    
            if (feedback != null) {
                return ResponseEntity.status(200).body(feedback);
            }
            
    
            return ResponseEntity.status(204).build();
    
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }




    @GetMapping("/feedback")
    public ResponseEntity<List<Feedback>> getAllFeedBack() {

        try {

            List<Feedback> feedbacks =
                    feedbackService.getAllFeedbacks();

            if (feedbacks.isEmpty()) {
                return ResponseEntity.status(204).build();
            }

            return ResponseEntity.status(200).body(feedbacks);

        } catch (Exception e) {

            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/feedback/user/{userId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByUserId(
            @PathVariable Long userId) {

        List<Feedback> feedbacks =
                feedbackService.getFeedbacksByUserId(userId);

        if (feedbacks.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(feedbacks);
    }

    @DeleteMapping("/feedback/{feedbackId}")
    public ResponseEntity<Feedback> deleteFeedback(
            @PathVariable Long feedbackId) {

        Feedback feedback =
                feedbackService.deleteFeedback(feedbackId);

        if (feedback != null) {
            return ResponseEntity.status(200).body(feedback);
        }

        return ResponseEntity.status(404).build();
    }
}
