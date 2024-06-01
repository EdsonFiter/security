package com.springboot.security.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReplaceEmailRequest {
    @NotEmpty(message = "User oldEmail cannot be empty")
    private String oldEmail;
    @NotEmpty(message = "User newEmail cannot be empty")
    private String newEmail;
    @NotEmpty(message = "User password cannot be empty")
    private String password;
}
