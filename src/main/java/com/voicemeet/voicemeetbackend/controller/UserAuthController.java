package com.voicemeet.voicemeetbackend.controller;

import com.voicemeet.voicemeetbackend.dto.UserLoginDTO;
import com.voicemeet.voicemeetbackend.entity.User;
import com.voicemeet.voicemeetbackend.repository.UserRepository;
import com.voicemeet.voicemeetbackend.security.JwtUtil;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserAuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserAuthController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginDTO dto){

        Optional<User> user = userRepository.findByUserId(dto.getUserId());

        if(user.isEmpty()){
            throw new RuntimeException("User not found");
        }

        if(!user.get().getPassword().equals(dto.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(dto.getUserId());
    }


}