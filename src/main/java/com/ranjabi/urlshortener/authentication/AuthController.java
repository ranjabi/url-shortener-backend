package com.ranjabi.urlshortener.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ranjabi.urlshortener.dto.request.AuthRequest;
import com.ranjabi.urlshortener.dto.response.AuthResponse;
import com.ranjabi.urlshortener.dto.response.ErrorResponse;
import com.ranjabi.urlshortener.dto.response.Response;
import com.ranjabi.urlshortener.dto.response.SuccessResponse;
import com.ranjabi.urlshortener.user.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/auth")
@Tag(name = "auth", description = "Authentication")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @Operation(summary = "Register new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "409", description = "Username already exists", content = @Content) })
    // TODO 400 bad request
    @PostMapping("/register")
    public ResponseEntity<Response<Void>> register(@RequestBody AuthRequest request) {
        userService.saveUser(request.getUsername(), request.getPassword());

        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.ofMessage("Account has been created"));
    }

    @Operation(summary = "Login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "401", description = "Username/password is wrong", content = @Content) })
    // TODO 400 bad request
    @PostMapping("/login")
    public ResponseEntity<Response<AuthResponse>> login(@RequestBody AuthRequest request) {
        try {
            AuthResponse response = authService.authenticate(request.getUsername(), request.getPassword());

            return ResponseEntity.ok(SuccessResponse.of("", response));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.ofMessage("Username/password is wrong"));
        }
    }
}
