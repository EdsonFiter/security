package com.springboot.security.service;

import com.springboot.security.configuration.JwtService;
import com.springboot.security.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer "))
            return;
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        if(userEmail != null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> (!t.getExpired() && !t.getRevoked()))
                    .orElse(false);
            if(jwtService.isTokenValid(jwt, userDetails) && isTokenValid){
                var storedToken = tokenRepository.findByToken(jwt)
                        .orElse(null);
                if(storedToken != null){
                    storedToken.setRevoked(true);
                    storedToken.setExpired(true);
                    tokenRepository.save(storedToken);
                }
            }

        }
    }
}
