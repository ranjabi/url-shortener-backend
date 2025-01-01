package com.ranjabi.urlshortener.user;

import org.springframework.web.bind.annotation.RestController;

import com.ranjabi.urlshortener.dto.response.AuthResponse;
import com.ranjabi.urlshortener.dto.response.SuccessResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {
    private final UserService userService;

    record AuthRequest(String username, String password) {
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<Void>> register(@RequestBody AuthRequest request) {
        userService.saveUser(request.username, request.password);

        var res = new SuccessResponse<Void>("Account has been created");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = userService.authenticate(request.username(), request.password());
        return ResponseEntity.ok(response);
    }
}
