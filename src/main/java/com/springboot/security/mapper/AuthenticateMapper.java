package com.springboot.security.mapper;

import com.springboot.security.requests.AuthenticateRequest;
import com.springboot.security.requests.UserReplaceEmailRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class AuthenticateMapper {
    public static final AuthenticateMapper INSTANCE = Mappers.getMapper(AuthenticateMapper.class);
    public abstract AuthenticateRequest toAuthenticateRequest(UserReplaceEmailRequest UserReplaceEmailRequest);
}
