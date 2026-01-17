package com.bitkulry.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bitkulry.member.domain.Member;
import com.bitkulry.member.dto.request.SignUpRequest;
import com.bitkulry.member.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service // 1. 스프링 빈으로 등록
@RequiredArgsConstructor // 2. final 필드에 대한 생성자를 자동으로 만들어줌 (의존성 주입의 핵심)
@Transactional // 3. 기본적으로 읽기 전용으로 설정 (성능 최적화)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(SignUpRequest request) {

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        // 1. DTO -> Entity 변환 (생성 시점에 값 주입)
        Member member = Member.builder()
            .id(request.getId())
            .password(encodedPassword)
            .name(request.getName())
            .email(request.getEmail())
            .phoneNumber(request.getPhoneNumber())
            .zipcode(request.getZipcode())
            .address(request.getAddress())
            .addressDetail(request.getAddressDetail())
            .role("USER")
            .build();

        // 2. DB 저장
        return memberRepository.save(member).getMemberSeq();
    }
}