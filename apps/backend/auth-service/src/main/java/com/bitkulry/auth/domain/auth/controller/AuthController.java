package com.bitkulry.auth.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitkulry.auth.domain.auth.dto.LoginRequest;
import com.bitkulry.auth.domain.auth.dto.TokenResponse;
import com.bitkulry.auth.domain.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        // 서비스에서 ID/PW 검증 후 Access + Refresh 토큰 반환
        TokenResponse tokens = authService.login(request);
        return ResponseEntity.ok(tokens);
    }
}
