package com.voicemeet.voicemeetbackend.config;

import com.voicemeet.voicemeetbackend.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())   // enable CORS
                .csrf(csrf -> csrf.disable())     // disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth

                        // login endpoints
                        .requestMatchers("/auth/**").permitAll()

                        // websocket
                        .requestMatchers("/ws/**").permitAll()

                        // signaling for WebRTC
                        .requestMatchers("/signal/**").permitAll()

                        // meeting APIs
                        .requestMatchers("/meeting/**").authenticated()

                        // admin APIs
                        .requestMatchers("/admin/**").authenticated()

                        // user APIs
                        .requestMatchers("/user/**").authenticated()

                        .anyRequest().permitAll()
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}