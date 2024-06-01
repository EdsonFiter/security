package com.springboot.security.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReplacePasswordRequest {
    @NotEmpty(message = "OldPassword cannot be empty")
    private String oldPassword;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    @NotEmpty(message = "Password confirm cannot be empty")
    private String passwordConfirm;
}
