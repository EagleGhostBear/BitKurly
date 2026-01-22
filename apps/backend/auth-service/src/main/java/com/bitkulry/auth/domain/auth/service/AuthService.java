package com.bitkulry.auth.domain.auth.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitkulry.auth.domain.auth.dto.LoginRequest;
import com.bitkulry.auth.domain.auth.dto.TokenResponse;
import com.bitkulry.auth.domain.member.entity.Member;
import com.bitkulry.auth.domain.member.repository.MemberRepository;
import com.bitkulry.auth.global.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate; // Redis 접속 도구

    @Transactional
    public TokenResponse login(LoginRequest request) {
        // 1. ID 검증: 없는 유저면 예외 발생
        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));

        // 2. 비밀번호 검증: 암호화된 비번과 입력받은 비번 비교
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 토큰 발급 (Access Token & Refresh Token)
        // role이 없다면 "USER"로 하드코딩하거나 Entity에 role 필드를 추가해야 함.
        String role = (member.getRole() != null) ? member.getRole().name() : "USER";
        
        String accessToken = jwtTokenProvider.createAccessToken(member.getLoginId(), role);
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getLoginId()); // Provider에 이 메서드 추가 필요

        // 4. Redis에 Refresh Token 저장 (키: 유저ID, 값: 토큰)
        // 설정: 14일(2주) 뒤에 자동으로 사라짐
        redisTemplate.opsForValue().set(
                member.getLoginId(),
                refreshToken,
                14, 
                TimeUnit.DAYS
        );

        // 5. 응답 반환
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType("Bearer")
                .build();
    }
}
