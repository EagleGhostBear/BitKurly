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
    private Long memberSeq; // PK는 Long 타입 권장 (seq -> id로 변경)

    @Column(nullable = false, unique = true, length = 50)
    private String id; // 로그인용 ID (구분을 위해 이름 변경)

    @Column(nullable = false)
    private String password;

    private String role; // 권한 (USER, ADMIN)
}