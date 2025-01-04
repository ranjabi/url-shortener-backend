package com.ranjabi.urlshortener.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequestDto {

    @NotBlank
    @Size(min = 4, max = 128)
    private String username;
    
    @NotBlank
    @Size(max = 256)
    private String password;
}
