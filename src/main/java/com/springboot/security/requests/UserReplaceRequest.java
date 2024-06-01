package com.springboot.security.requests;

import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReplaceRequest {
    @NotEmpty(message = "User firstname cannot be empty")
    private String firstname;
    @NotEmpty(message = "User lastname cannot be empty")
    private String lastname;
}
