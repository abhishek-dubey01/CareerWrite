package com.careerwrite.service;

import com.careerwrite.dto.ApiException;
import com.careerwrite.dto.AuthResponse;
import com.careerwrite.dto.LoginRequest;
import com.careerwrite.dto.RegisterRequest;
import com.careerwrite.entity.User;
import com.careerwrite.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

// Holds the business logic for registration and login. Controllers stay
// thin (they just receive the HTTP request and call this).
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException("This email is already registered", HttpStatus.BAD_REQUEST);
        }

        String role = request.getRole().trim().toUpperCase();
        if (!role.equals("JOB_SEEKER") && !role.equals("RECRUITER")) {
            throw new ApiException("Role must be JOB_SEEKER or RECRUITER", HttpStatus.BAD_REQUEST);
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getName(), request.getEmail(), hashedPassword, role);
        User saved = userRepository.save(user);

        return new AuthResponse(saved.getId(), saved.getName(), saved.getEmail(), saved.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("Invalid email or password", HttpStatus.UNAUTHORIZED));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!passwordMatches) {
            throw new ApiException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    public User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
    }
}
