package com.springboot.security.requests;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String firstname;
    private String lastname;
    private String email;
}
