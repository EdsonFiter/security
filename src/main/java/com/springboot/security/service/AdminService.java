package com.springboot.security.service;

import com.springboot.security.exception.BadRequestException;
import com.springboot.security.repository.UserRepository;
import com.springboot.security.domain.Role;
import com.springboot.security.mapper.UserMapper;
import com.springboot.security.mapper.UserResponseMapper;
import com.springboot.security.requests.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


    public void registerNewAdmin(RegisterRequest registerRequest){
        if(!userRepository.findByEmail(registerRequest.getEmail()).isEmpty())
            return;
        var user = UserMapper.INSTANCE.toUser(registerRequest);
        user.setRole(Role.ADMIN);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);
    }

    public Set<UserResponse> findAllUser(){
        Set<UserResponse> userSet = new HashSet<>();
        userRepository.findAll()
                .forEach(user -> userSet.add(UserResponseMapper.INSTANCE.toUserResponse(user)));
        return userSet;
    }

    public long countUserByRoleName(String roleName){
        if(roleName.equals(Role.USER.name())){
            return userRepository.countUserByRole(Role.USER);
        }
        return userRepository.countUserByRole(Role.ADMIN);
    }

    public void remove(UserRemoveRequest userRemoveRequest){
        if(countUserByRoleName(Role.ADMIN.name()) < 2){
            throw new BadRequestException("Must have minimum one admin in data base");
        }
        userService.remove(userRemoveRequest);
    }

    public Set<UserResponse> findUserByRole(String roleName){
        var userResponseSet = new HashSet<UserResponse>();
        if(roleName.equals(Role.USER.name())){
              userRepository.findUsersByRole(Role.USER)
                    .forEach(user -> userResponseSet.add(UserResponseMapper.INSTANCE.toUserResponse(user)));
        }else{
            userRepository.findUsersByRole(Role.ADMIN)
                    .forEach(user -> userResponseSet.add(UserResponseMapper.INSTANCE.toUserResponse(user)));
        }
        return userResponseSet;
    }

    public Set<UserResponse> findUsersWhoAreLoggedInByRole(String roleName){
        var userRole = Role.USER;
        if(Role.ADMIN.name().equals(roleName)){
            userRole = Role.ADMIN;
        }
        var userResponseSet = new HashSet<UserResponse>();
        userRepository.findAllUserWhoLoggedInByRole(userRole).forEach(user ->
            userResponseSet.add(UserResponseMapper.INSTANCE.toUserResponse(user))
        );
        return userResponseSet;
    }
}
