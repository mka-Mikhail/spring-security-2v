package com.mka.springsecurity2v.rest;

import lombok.Data;

//для передачи дтошек
@Data
public class AuthenticationRequestDto {
    private String email;
    private String password;
}
