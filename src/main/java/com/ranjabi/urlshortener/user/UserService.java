package com.ranjabi.urlshortener.user;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ranjabi.urlshortener.entities.User;
import com.ranjabi.urlshortener.jwt.JwtService;
import com.ranjabi.urlshortener.responses.AuthResponse;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
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

    public User saveUser(String username, String password) {
        var user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }
}
