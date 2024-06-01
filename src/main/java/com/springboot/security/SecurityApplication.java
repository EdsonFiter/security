package com.springboot.security;

import com.springboot.security.configuration.JwtService;
import com.springboot.security.domain.Role;
import com.springboot.security.domain.Token;
import com.springboot.security.domain.TokenType;
import com.springboot.security.domain.User;
import com.springboot.security.exception.BadRequestException;
import com.springboot.security.repository.TokenRepository;
import com.springboot.security.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserRepository userRepository, TokenRepository tokenRepository, JwtService jwtService, PasswordEncoder passwordEncoder){
		return args -> {
			System.out.println("Admin Token: "+ register(userRepository, tokenRepository, jwtService, passwordEncoder));
		};
	}

	public String register(UserRepository userRepository, TokenRepository tokenRepository, JwtService jwtService, PasswordEncoder passwordEncoder){
		var user = User.builder()
				.firstname("admin")
				.lastname("admin")
				.password(passwordEncoder.encode("admin"))
				.email("admin@gmail.com")
				.role(Role.ADMIN)
				.build();
		if(!userRepository.findByEmail(user.getEmail()).isEmpty())
			return null;

		var userSaved = userRepository.save(user);
		var jwtToken = jwtService.generateAccessToken(userSaved);
		var userToken = Token.builder()
				.user(userSaved)
				.token(jwtToken)
				.tokenType(TokenType.BEARER)
				.revoked(false)
				.expired(false)
				.build();
		tokenRepository.save(userToken);
		return userToken.getToken();
	}
}
