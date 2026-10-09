
package com.archit.chatapp.service;

import com.archit.chatapp.dto.LoginRequest;
import com.archit.chatapp.dto.LoginResponse;
import com.archit.chatapp.dto.RegisterRequest;
import com.archit.chatapp.entity.User;
import com.archit.chatapp.repository.UserRepository;

import com.archit.chatapp.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request) {


        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException(
                    "Email is already registered");
        }


        String hashedPassword =
                passwordEncoder.encode(request.password());


        User user = new User(
                request.name(),
                request.email(),
                hashedPassword
        );


        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
