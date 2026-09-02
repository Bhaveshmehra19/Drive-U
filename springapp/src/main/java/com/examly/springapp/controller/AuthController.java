package com.examly.springapp.controller;

import com.examly.springapp.model.LoginDTO;
import com.examly.springapp.model.User;
import com.examly.springapp.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private static final String JWT_COOKIE_NAME = "DriveU-JWT";
    private static final long COOKIE_MAX_AGE_SECONDS = 24 * 60 * 60;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);

            if (createdUser == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .build();
            }

            /*
             * Never return encoded password to frontend.
             */
            createdUser.setPassword(null);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(createdUser);

        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("A user with this email already exists");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody User user,
            HttpServletResponse response
    ) {
        LoginDTO loginResponse = userService.loginUser(user);

        if (loginResponse == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }

        ResponseCookie authenticationCookie =
                createAuthenticationCookie(loginResponse.getToken());

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                authenticationCookie.toString()
        );

        /*
         * LoginDTO.token has @JsonIgnore.
         * So JSON response will contain only:
         * userId, username, userRole
         */
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (
                authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())
        ) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication required");
        }

        User user = userService.getUserByEmail(authentication.getName());

        if (user == null || user.isDeleted()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("User not found");
        }

        LoginDTO currentUser = new LoginDTO();
        currentUser.setUserId(user.getUserId());
        currentUser.setUsername(user.getUsername());
        currentUser.setUserRole(user.getUserRole());

        return ResponseEntity.ok(currentUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie deletedCookie =
                ResponseCookie
                        .from(JWT_COOKIE_NAME, "")
                        .httpOnly(true)
                        .secure(true)
                        .sameSite("None")
                        .path("/")
                        .maxAge(0)
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                deletedCookie.toString()
        );

        return ResponseEntity.ok("Logged out successfully");
    }

    private ResponseCookie createAuthenticationCookie(String token) {
        return ResponseCookie
                .from(JWT_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(COOKIE_MAX_AGE_SECONDS)
                .build();
    }
}
