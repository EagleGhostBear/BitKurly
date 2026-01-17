package com.bitkulry.gateway.filter;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    
    private final SecretKey key;

    public AuthorizationHeaderFilter(@Value("${jwt.secret}") String secret) {
        super(Config.class);
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public static class Config {
        // 설정값이 필요하면 여기에 추가
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 1. 헤더에 Authorization이 있는지 확인
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            // 2. Bearer 토큰 추출
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String token = authorizationHeader.replace("Bearer ", "");

            // 3. 토큰 검증 및 User ID 추출
            String userId = validateToken(token);
            if (userId == null) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            // 4. [핵심] User ID를 헤더에 추가해서 뒷단 서비스(Member 등)로 전달
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Id", userId) // 뒷단에서는 이 헤더를 꺼내 쓰면 됨
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }

    private String validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            return claims.getSubject(); // 토큰 만들 때 넣었던 loginId 반환
        } catch (Exception e) {
            log.error("JWT Validation Failed: {}", e.getMessage());
            return null;
        }
    }

    // 에러 발생 시 401 반환 처리
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(err);
        return response.setComplete();
    }
}
