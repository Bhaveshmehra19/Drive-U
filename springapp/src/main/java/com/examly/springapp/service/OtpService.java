package com.examly.springapp.service;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    private final ConcurrentHashMap<String, OtpEntry> otpStore =
            new ConcurrentHashMap<>();

    private final SecureRandom random = new SecureRandom();

    private static final long OTP_VALID_MS = 5 * 60 * 1000; // 5 minutes

    private static class OtpEntry {
        String otp;
        long expiresAt;

        OtpEntry(String otp, long expiresAt) {
            this.otp = otp;
            this.expiresAt = expiresAt;
        }
    }

    public void sendOtp(String email) {
        String otp = String.format("%06d", random.nextInt(1000000));

        otpStore.put(
                email.toLowerCase(),
                new OtpEntry(
                        otp,
                        System.currentTimeMillis() + OTP_VALID_MS
                )
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("DriveU Email Verification Code");
        message.setText(
                "Your DriveU verification code is: " + otp +
                "\n\nThis code is valid for 5 minutes." +
                "\n\nIf you did not request this, please ignore this email."
        );

        mailSender.send(message);
    }

    public boolean verifyOtp(String email, String otp) {
        String key = email.toLowerCase();
        OtpEntry entry = otpStore.get(key);

        if (entry == null) {
            return false;
        }

        if (System.currentTimeMillis() > entry.expiresAt) {
            otpStore.remove(key);
            return false;
        }

        boolean matches = entry.otp.equals(otp);

        if (matches) {
            otpStore.remove(key);
        }

        return matches;
    }
}