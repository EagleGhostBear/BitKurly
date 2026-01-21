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
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "login_id", nullable = false, unique = true, length = 50)
    private String loginId;

    @Column(nullable = false) // 길이는 암호화 방식에 따라 다르므로 생략 가능 (기본 255)
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    // 주소 정보 (값 타입으로 빼도 되지만, 일단 필드로 둡니다)
    @Column(length = 10)
    private String zipcode;

    private String address;

    @Column(name = "address_detail")
    private String addressDetail;

    // Enum 매핑 (DB에는 문자열 "USER", "BRONZE"로 저장됨)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Grade grade;

    // Auditing (자동 날짜 주입)
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // @Builder를 통해 안전하게 객체 생성
    @Builder
    public Member(String loginId, String password, String name, String email, 
                  String phoneNumber, String zipcode, String address, String addressDetail, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.zipcode = zipcode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.role = (role != null) ? role : Role.USER;
        this.grade = Grade.BRONZE; // default : BRONZE
    }

    public void updateAddress(String zipcode, String address, String addressDetail) {
        // 데이터 무결성 검사 (엔티티가 스스로 방어)
        if (zipcode == null || address == null) {
            throw new IllegalArgumentException("주소 정보가 누락되었습니다.");
        }
        this.zipcode = zipcode;
        this.address = address;
        this.addressDetail = addressDetail;
    }

    public void changePassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }
}