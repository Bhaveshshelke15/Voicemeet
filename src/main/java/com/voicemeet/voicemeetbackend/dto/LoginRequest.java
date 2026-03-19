package com.voicemeet.voicemeetbackend.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String userId;
    private String password;

}