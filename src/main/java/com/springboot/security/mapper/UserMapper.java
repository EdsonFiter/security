package com.springboot.security.mapper;

import com.springboot.security.requests.AuthenticateRequest;
import com.springboot.security.requests.UserReplaceEmailRequest;
import com.springboot.security.requests.UserReplaceRequest;
import com.springboot.security.domain.User;
import com.springboot.security.requests.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    public  User toUser(RegisterRequest registerRequest){
        return User.builder()
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .build();
    }
    public User toUser(UserReplaceRequest userReplaceRequest, User userSaved){
        return User.builder()
                .firstname(userReplaceRequest.getFirstname())
                .lastname(userReplaceRequest.getLastname())
                .email(userSaved.getEmail())
                .role(userSaved.getRole())
                .password(userSaved.getPassword())
                .id(userSaved.getId())
                .build();
    }

    public User toUser(UserReplaceEmailRequest userReplaceEmailRequest, User userSaved){
        return User.builder()
                .email(userReplaceEmailRequest.getNewEmail())
                .password(userSaved.getPassword())
                .role(userSaved.getRole())
                .firstname(userSaved.getFirstname())
                .lastname(userSaved.getLastname())
                .id(userSaved.getId())
                .build();
    }
}
