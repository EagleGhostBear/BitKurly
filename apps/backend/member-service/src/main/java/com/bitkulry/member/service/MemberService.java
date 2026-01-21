package com.bitkulry.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitkulry.member.domain.Member;
import com.bitkulry.member.domain.Role;
import com.bitkulry.member.dto.request.AddressUpdateRequest;
import com.bitkulry.member.dto.request.PasswordChangeRequest;
import com.bitkulry.member.dto.request.SignUpRequest;
import com.bitkulry.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service // 1. 스프링 빈으로 등록
@RequiredArgsConstructor // 2. final 필드에 대한 생성자를 자동으로 만들어줌 (의존성 주입의 핵심)
@Transactional(readOnly = true) // 3. 기본적으로 읽기 전용으로 설정 (성능 최적화)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(SignUpRequest request) {

        // 1. 중복 검사 (서비스의 역할)
        if (memberRepository.existsByLoginId(request.getLoginId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 3. DTO -> Entity 변환 (생성 시점에 값 주입) // 엔티티 생성 (Builder 사용)
        Member member = Member.builder()
            .loginId(request.getLoginId())
            .password(encodedPassword)
            .name(request.getName())
            .email(request.getEmail())
            .phoneNumber(request.getPhoneNumber())
            .zipcode(request.getZipcode())
            .address(request.getAddress())
            .addressDetail(request.getAddressDetail())
            .role(Role.USER)
            .build();

        // 4. DB 저장
        return memberRepository.save(member).getMemberId();
    }

    @Transactional
    public void updateAddress(Long memberId, AddressUpdateRequest request) {
        // 1. 엔티티 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));

        // 2. 엔티티에게 "주소 바꿔" 명령 (서비스는 값을 꺼내서 전달만 함)
        member.updateAddress(
                request.getZipcode(), 
                request.getAddress(), 
                request.getAddressDetail()
        );
        
        // 3. 별도의 save() 호출 필요 없음 (Dirty Checking으로 자동 반영됨)
    }

    @Transactional
    public void changePassword(Long memberId, PasswordChangeRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));

        // 1. 현재 비밀번호가 맞는지 검사 (서비스가 PasswordEncoder를 이용해 확인)
        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 2. 새 비밀번호 암호화
        String newEncodedPassword = passwordEncoder.encode(request.getNewPassword());

        // 3. 엔티티에게 "비번 갈아끼워" 명령
        member.changePassword(newEncodedPassword);
    }
}