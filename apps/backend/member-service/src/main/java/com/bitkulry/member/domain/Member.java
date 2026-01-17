package com.bitkulry.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "members") // 테이블명 명시 (DB 예약어 member와 충돌 방지)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 필수: 기본 생성자 (외부에서 new Member() 남발 방지)
@EntityListeners(AuditingEntityListener.class) // 생성일/수정일 자동 관리
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberSeq; // PK는 Long 타입 권장 (seq -> id로 변경)

    @Column(nullable = false, unique = true, length = 50)
    private String id; // 로그인용 ID (구분을 위해 이름 변경)

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, unique = true)
    private String email; // 합쳐서 저장 (user@example.com)

    @Column(length = 20)
    private String phoneNumber; // 합쳐서 저장 (010-1234-5678)

    // 주소 정보
    private String zipcode;
    private String address; // addr1 (주소)
    private String addressDetail; // addr2 (상세주소)

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // logtime -> createdAt (가입일)

    @LastModifiedDate
    private LocalDateTime updatedAt; // 수정일

    @Column(nullable = false)
    private String role; // 권한 (USER, ADMIN)

    // @Builder를 통해 안전하게 객체 생성
    @Builder
    public Member(String id, String password, String name, String email, String phoneNumber, String zipcode,
            String address, String addressDetail, String role) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.zipcode = zipcode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.role = role;
    }
}