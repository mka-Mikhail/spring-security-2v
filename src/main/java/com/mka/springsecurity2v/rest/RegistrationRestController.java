package com.mka.springsecurity2v.rest;

import com.mka.springsecurity2v.model.UserDto;
import com.mka.springsecurity2v.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/registration")
@AllArgsConstructor
public class RegistrationRestController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createNewUser(@RequestBody UserDto userDto) {
        try {
            userService.findByEmail(userDto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User does`t exist"));
        } catch (UsernameNotFoundException e) {
            userService.createNewUser(userDto);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        return new ResponseEntity<>("User with such an email already exists", HttpStatus.CONFLICT);
    }
}
