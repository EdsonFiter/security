package com.springboot.security.controller;

import com.springboot.security.requests.*;
import com.springboot.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> getUser(){
        return new ResponseEntity<>(userService.getUser(), HttpStatus.OK);
    }

    @PutMapping("/replace-first-and-last-name")
    public ResponseEntity<Void> replaceFirstAndLastName(@RequestBody @Valid UserReplaceRequest userReplaceRequest){
        userService.replaceFirstAndLastName(userReplaceRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/replace-password")
    public ResponseEntity<Void> replacePassword(@RequestBody @Valid UserReplacePasswordRequest userReplacePasswordRequest){
        userService.replacePassword(userReplacePasswordRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PatchMapping("/replace-email")
    public ResponseEntity<AuthenticationResponse> replaceEmail(@RequestBody @Valid UserReplaceEmailRequest userReplaceEmailRequest){
        return new ResponseEntity<>(userService.replaceEmail(userReplaceEmailRequest) ,HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> remove(@RequestBody @Valid UserRemoveRequest userRemoveRequest){
        userService.remove(userRemoveRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
