package com.ranjabi.urlshortener.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ranjabi.urlshortener.dto.request.AuthRequest;
import com.ranjabi.urlshortener.dto.response.AuthResponse;
import com.ranjabi.urlshortener.dto.response.SuccessResponse;
import com.ranjabi.urlshortener.user.UserService;

@RestController
@RequestMapping(path="/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<Void>> register(@RequestBody AuthRequest request) {
        userService.saveUser(request.getUsername(), request.getPassword());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.ofMessage("Account has been created"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.authenticate(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}
