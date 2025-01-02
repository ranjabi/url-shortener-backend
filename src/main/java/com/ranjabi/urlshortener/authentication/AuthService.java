package com.ranjabi.urlshortener.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.ranjabi.urlshortener.dto.response.AuthResponse;
import com.ranjabi.urlshortener.entities.User;
import com.ranjabi.urlshortener.security.JwtService;
import com.ranjabi.urlshortener.user.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse authenticate(String username, String password) throws BadCredentialsException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password));
            User authenticatedUser = userRepository.findByUsername(username)
                    .orElseThrow();
    
            String jwtToken = jwtService.generateToken(new UserAdapter(authenticatedUser));
    
            return new AuthResponse(authenticatedUser, jwtToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Username/password is wrong", e);
        }
    }
}
