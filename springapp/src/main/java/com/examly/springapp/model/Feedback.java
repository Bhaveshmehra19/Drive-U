package com.examly.springapp.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long feedbackId;

    private String category;

    private int rating;

    private String feedbackText;

    private LocalDate date;
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"feedbacks", "password"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    @JsonIgnoreProperties({"feedbacks"})
    private Driver driver;

    public Feedback() {
    }

    public Feedback(Long feedbackId, String category, int rating, String feedbackText, LocalDate date, User user, Driver driver) {
        this.feedbackId = feedbackId;
        this.category = category;
        this.rating = rating;
        this.feedbackText = feedbackText;
        this.date = date;
        this.user = user;
        this.driver = driver;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}