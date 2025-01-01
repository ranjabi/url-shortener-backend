package com.ranjabi.urlshortener.dto.response;

import com.ranjabi.urlshortener.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthResponse {
    private User user;
    private String token;

    public AuthResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }
}
