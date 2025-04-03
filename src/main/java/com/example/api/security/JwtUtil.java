package com.example.api.security;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "minhaChaveSecreta";
    private final long EXPITATION_TIME = 86400000; // 1 dia

}
