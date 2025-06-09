package com.project.ecommerce.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Email
    @NotBlank(message = "Email must not be blank")
    private String email;
    @NotBlank(message = "password must not be blank")
    private String password;
}
