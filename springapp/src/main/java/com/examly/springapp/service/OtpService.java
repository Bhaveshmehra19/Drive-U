package com.examly.springapp.service;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    @Autowired
    private EmailService emailService;

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
        emailService.sendPlainText(email, "DriveU - Your OTP", "Your OTP is: " + otp + "\nThis OTP is valid for 5 minutes.");
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