package com.springboot.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.security.exception.BadRequestException;
import com.springboot.security.repository.UserRepository;
import com.springboot.security.requests.AuthenticateRequest;
import com.springboot.security.configuration.JwtService;
import com.springboot.security.domain.Role;
import com.springboot.security.domain.Token;
import com.springboot.security.domain.TokenType;
import com.springboot.security.domain.User;
import com.springboot.security.mapper.UserMapper;
import com.springboot.security.repository.TokenRepository;
import com.springboot.security.requests.AuthenticationResponse;
import com.springboot.security.requests.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if(!userRepository.findByEmail(registerRequest.getEmail()).isEmpty())
            throw new BadRequestException( "User already exists");

        var user = UserMapper.INSTANCE.toUser(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        var userSaved = userRepository.save(user);
        var jwtToken = jwtService.generateAccessToken(user);
        var refreshToken  = jwtService.generateRefreshToken(userSaved);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticateRequest authenticateRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticateRequest.getEmail(),
                        authenticateRequest.getPassword()
                )
        );
        var user = userRepository.findByEmail(authenticateRequest.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateAccessToken(user);
        var refreshToken  = jwtService.generateRefreshToken(user);
        revokeAllValidUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer "))
            return;

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if(userEmail != null){
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if(jwtService.isTokenValid(refreshToken, user)){
                var accessToken = jwtService.generateAccessToken(user);
                revokeAllValidUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }

    }

    private void revokeAllValidUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidUserToken(user.getId());
        if(!validUserTokens.isEmpty()) {
            validUserTokens.forEach(t -> {
                t.setExpired(true);
                t.setRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .tokenType(TokenType.BEARER)
                .build();
        tokenRepository.save(token);
    }
}
