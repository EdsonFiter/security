package com.springboot.security.mapper;

import com.springboot.security.requests.UserResponse;
import com.springboot.security.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class UserResponseMapper {
    public static final UserResponseMapper INSTANCE = Mappers.getMapper(UserResponseMapper.class);
    public abstract UserResponse toUserResponse(User user);
}
