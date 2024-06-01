package com.springboot.security.service;

import com.springboot.security.domain.User;
import com.springboot.security.exception.BadRequestException;
import com.springboot.security.mapper.AuthenticateMapper;
import com.springboot.security.mapper.UserMapper;
import com.springboot.security.mapper.UserResponseMapper;
import com.springboot.security.repository.UserRepository;
import com.springboot.security.requests.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public User findByIdOrThrowBadRequestException(long id){
        return userRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("User not found"));
    }

    public User findUserByUsername(){
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    public UserResponse getUser(){
        return UserResponseMapper.INSTANCE.toUserResponse(findUserByUsername());
    }

    public void replaceFirstAndLastName(UserReplaceRequest userReplaceRequest){
        var userToSave = UserMapper.INSTANCE.toUser(userReplaceRequest, findUserByUsername());
        userRepository.save(userToSave);
    }

    public AuthenticationResponse replaceEmail(UserReplaceEmailRequest userReplaceEmailRequest){
        var userSaved = findUserByUsername();
        if(userSaved.getEmail().equals(userReplaceEmailRequest.getOldEmail())){
            if(passwordEncoder.matches(userReplaceEmailRequest.getPassword(), userSaved.getPassword())) {
                var userToReplace = UserMapper.INSTANCE.toUser(userReplaceEmailRequest, userSaved);
                userRepository.save(userToReplace);
                return authService.authenticate(AuthenticateMapper.INSTANCE.toAuthenticateRequest(userReplaceEmailRequest));
            }else{
                throw new BadRequestException("Passwords do not matches");
            }
        }else{
            throw new BadRequestException("Old email do not match new email");
        }
    }

    public void replacePassword(UserReplacePasswordRequest userReplacePasswordRequest){
        if(!userReplacePasswordRequest.getPassword().equals(userReplacePasswordRequest.getPasswordConfirm())){
            throw new BadRequestException("Passwords do not match");
        }
        var userToReplace = findUserByUsername();
        if(!passwordEncoder.matches(userReplacePasswordRequest.getOldPassword(), userToReplace.getPassword())){
            throw new BadRequestException("Old password do not match confirmation");
        }
        userToReplace.setPassword(passwordEncoder.encode(userReplacePasswordRequest.getPassword()));
        userRepository.save(userToReplace);
    }

    public void remove(UserRemoveRequest userRemoveRequest) {
        if (!userRemoveRequest.getPassword().equals(userRemoveRequest.getPasswordConfirm())) {
            throw new BadRequestException("Passwords do not match");
        }
        var userToRemove = findUserByUsername();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userToRemove.getEmail(),
                        userRemoveRequest.getPassword())
        );
        tokenService.removeAllUserToken(userToRemove.getId());
        userRepository.delete(userToRemove);
    }
}
