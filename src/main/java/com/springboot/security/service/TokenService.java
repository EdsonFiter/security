package com.springboot.security.service;

import com.springboot.security.domain.Token;
import com.springboot.security.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public List<Token> findAllUserTokens(long userId){
        return tokenRepository.findAllUserToken(userId);
    }

    public void removeAllUserToken(long userId){
        tokenRepository.deleteAll(findAllUserTokens(userId));
    }
}
