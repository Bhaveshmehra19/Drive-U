package com.examly.springapp.service;

import com.examly.springapp.config.JwtUtils;
import com.examly.springapp.model.LoginDTO;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public User createUser(User user) {
        User existingUser = userRepo.findByEmail(user.getEmail());

        if (existingUser != null) {
            throw new RuntimeException(
                    "A user with this email already exists"
            );
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepo.save(user);
    }

    @Override
    public LoginDTO loginUser(User user) {
        User existingUser = userRepo.findByEmail(user.getEmail());

        if (existingUser == null || existingUser.isDeleted()) {
            return null;
        }

        boolean passwordMatches =
                passwordEncoder.matches(
                        user.getPassword(),
                        existingUser.getPassword()
                );

        if (!passwordMatches) {
            return null;
        }

        String token = jwtUtils.generateToken(
                existingUser.getEmail(),
                existingUser.getUserRole()
        );

        LoginDTO loginDTO = new LoginDTO();

        /*
         * Token is used only internally by AuthController
         * to create the HttpOnly cookie.
         *
         * Because LoginDTO.token has @JsonIgnore,
         * token will not appear in API response JSON.
         */
        loginDTO.setToken(token);
        loginDTO.setUserId(existingUser.getUserId());
        loginDTO.setUsername(existingUser.getUsername());
        loginDTO.setUserRole(existingUser.getUserRole());

        return loginDTO;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
