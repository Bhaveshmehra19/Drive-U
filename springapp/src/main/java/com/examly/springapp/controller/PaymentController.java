package com.examly.springapp.controller;

import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private com.examly.springapp.service.EmailService emailService;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/send-confirmation")
    public ResponseEntity<?> sendConfirmation(@RequestBody PaymentEmailRequest req) {
        try {
            // Find the user's email by userId
            User user = userRepo.findById(req.getUserId()).orElse(null);

            if (user == null || user.getEmail() == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User email not found");
            }

            emailService.sendPlainText(
                    user.getEmail(),
                    "DriveU - Payment Confirmation",
                    "Dear " + user.getUsername() + ",\n\n" +
                    "Your payment has been received successfully.\n\n" +
                    "----------------------------------------\n" +
                    "Payment ID   : " + req.getPaymentId() + "\n" +
                    "Amount Paid  : Rs. " + req.getAmount() + "\n" +
                    "Driver       : " + req.getDriverName() + "\n" +
                    "Pickup       : " + req.getPickupLocation() + "\n" +
                    "Drop         : " + req.getDropLocation() + "\n" +
                    "----------------------------------------\n\n" +
                    "Thank you for choosing DriveU!\n" +
                    "Your Journey, Our Responsibility."
            );

            return ResponseEntity.ok("Confirmation email sent");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send confirmation email");
        }
    }

    public static class PaymentEmailRequest {
        private Long userId;
        private String paymentId;
        private double amount;
        private String driverName;
        private String pickupLocation;
        private String dropLocation;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getPaymentId() { return paymentId; }
        public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }

        public String getDriverName() { return driverName; }
        public void setDriverName(String driverName) { this.driverName = driverName; }

        public String getPickupLocation() { return pickupLocation; }
        public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

        public String getDropLocation() { return dropLocation; }
        public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }
    }
}