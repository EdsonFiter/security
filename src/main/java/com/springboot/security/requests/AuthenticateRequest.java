package com.springboot.security.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticateRequest {
    @NotEmpty(message = "User email cannot be empty")
    private String email;
    @NotEmpty(message = "User password cannot be empty")
    private String password;
}
