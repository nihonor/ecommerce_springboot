package com.project.ecommerce.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 255,message = "Name cannot exceed 255")
    private String name;

    @NotBlank(message = "Password is required")
    @Size(min = 6,max = 25,message = "Password must be between 6 and 25")
    private String password;

    @NotBlank(message = "Email is required")
    @Email
    private String email;
}
