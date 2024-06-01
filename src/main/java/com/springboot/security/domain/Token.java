package com.springboot.security.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    @Column(nullable = false)
    private Boolean expired;
    @Column(nullable = false)
    private Boolean revoked;
    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;
}
