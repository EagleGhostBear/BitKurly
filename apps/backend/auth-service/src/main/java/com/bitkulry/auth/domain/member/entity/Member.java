package com.bitkulry.auth.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "members") // 테이블명 명시 (DB 예약어 member와 충돌 방지)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 필수: 기본 생성자 (외부에서 new Member() 남발 방지)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "login_id", nullable = false, unique = true, length = 50)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role;
}