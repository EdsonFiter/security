package com.springboot.security.controller;

import com.springboot.security.requests.*;
import com.springboot.security.service.AdminService;
import com.springboot.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService userAdminService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> getAdmin(){
        return new ResponseEntity<>(userService.getUser(), HttpStatus.OK);
    }

    @PostMapping("/replace-first-and-last-name")
    public ResponseEntity<Void> replaceFirstAndLastName(@RequestBody @Valid UserReplaceRequest userReplaceRequest){
        userService.replaceFirstAndLastName(userReplaceRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest registerRequest){
        userAdminService.registerNewAdmin(registerRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/find-Users-Who-Are-LoggedIn-By-Role")
    public ResponseEntity<Set<UserResponse>> findUsersWhoAreLoggedInByRole(@RequestParam String roleName){
        return new ResponseEntity<>(userAdminService.findUsersWhoAreLoggedInByRole(roleName), HttpStatus.OK);
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

    @GetMapping("/listAllUser")
    public ResponseEntity<Set<UserResponse>> listAllUser(){
        return new ResponseEntity<>(userAdminService.findAllUser(), HttpStatus.OK);
    }


    @DeleteMapping("/remove")
    public ResponseEntity<Void> remove(@RequestBody @Valid UserRemoveRequest userRemoveRequest){
        userAdminService.remove(userRemoveRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/count-admin-users-by/{roleName}")
    public ResponseEntity<Long> countAdminUser(@PathVariable String roleName){
        return new ResponseEntity<>(userAdminService.countUserByRoleName(roleName), HttpStatus.OK);
    }

    @GetMapping("/users-by/{roleName}")
    public ResponseEntity<Set<UserResponse>> findUserByRoleName(@PathVariable String roleName){
        return new ResponseEntity<>(userAdminService.findUserByRole(roleName), HttpStatus.OK);
    }
}
