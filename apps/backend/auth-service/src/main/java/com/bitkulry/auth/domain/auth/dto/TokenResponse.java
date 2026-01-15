package com.bitkulry.auth.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String grantType; // "Bearer" 등을 명시하기 위함
    private Long accessTokenExpiresIn;
}