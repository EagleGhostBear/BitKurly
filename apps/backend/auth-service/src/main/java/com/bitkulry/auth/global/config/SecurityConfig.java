package com.bitkulry.auth.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. CSRF 비활성화 (Rest API 서버는 필수)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. Form 로그인 및 HttpBasic 비활성화 (우린 JWT 쓸거니까)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)

            // 3. 세션 사용 안 함 (STATELESS)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 4. URL 권한 설정 (로그인, 회원가입은 누구나 접근 가능해야 함)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login", "/auth/signup", "/actuator/**").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }

    // 비밀번호 암호화용 Bean 등록 (필수)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
