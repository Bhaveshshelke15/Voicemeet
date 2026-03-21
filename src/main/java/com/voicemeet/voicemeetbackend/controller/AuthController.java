package com.voicemeet.voicemeetbackend.controller;

import com.voicemeet.voicemeetbackend.dto.LoginRequest;
import com.voicemeet.voicemeetbackend.entity.Admin;
import com.voicemeet.voicemeetbackend.entity.User;
import com.voicemeet.voicemeetbackend.repository.AdminRepository;
import com.voicemeet.voicemeetbackend.repository.UserRepository;
import com.voicemeet.voicemeetbackend.security.JwtUtil;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "https://voicemeet-frontend.onrender.com")
public class AuthController {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(AdminRepository adminRepository,
                          UserRepository userRepository,
                          JwtUtil jwtUtil) {

        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // ================= ADMIN LOGIN =================

    @PostMapping("/admin/login")
    public Map<String, Object> adminLogin(@RequestBody LoginRequest request) {

        Admin admin = adminRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if(!admin.getPassword().equals(request.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(admin.getUsername());

        Map<String,Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role","ADMIN");

        return response;
    }

    // ================= USER LOGIN =================

    @PostMapping("/user/login")
    public Map<String, Object> userLogin(@RequestBody LoginRequest request) {

        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.getPassword().equals(request.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getUserId());

        Map<String,Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role","USER");

        return response;
    }

}