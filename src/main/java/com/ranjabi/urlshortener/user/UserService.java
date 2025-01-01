package com.ranjabi.urlshortener.user;

import java.sql.SQLException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ranjabi.urlshortener.dto.response.AuthResponse;
import com.ranjabi.urlshortener.entities.User;
import com.ranjabi.urlshortener.exception.DuplicateUsernameException;
import com.ranjabi.urlshortener.security.JwtService;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse authenticate(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password));
        User authenticatedUser = userRepository.findByUsername(username)
                .orElseThrow();

        String jwtToken = jwtService.generateToken(new UserAdapter(authenticatedUser));

        return new AuthResponse(authenticatedUser, jwtToken);
    }

    public void saveUser(String username, String password) {
        try {
            User newUser = new User(username, passwordEncoder.encode(password));
            userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException") && ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23505")) {
                throw new DuplicateUsernameException("Username already exists");
            }

            throw new RuntimeException("DATABASE ERROR: " + e.getMessage(), e);
        }
    }
}
