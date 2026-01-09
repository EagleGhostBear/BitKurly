package com.bitkulry.member.dto.request;

import java.time.LocalDateTime;

import com.bitkulry.member.domain.Member;

import lombok.Data;

@Data // DTO는 막 써도 됩니다. 단순 데이터 전달용이니까요.
public class SignUpRequest {
    private String id;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private String zipcode;
    private String address;
    private String addressDetail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // DTO를 Entity로 변환하는 메서드
    public Member toEntity() {
        return Member.builder() // Setter 대신 Builder 사용!
                .id(this.id)
                .password(this.password) // 실제론 암호화 해야 함
                .name(this.name)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .zipcode(this.zipcode)
                .address(this.address)
                .addressDetail(this.addressDetail)
                .build();
    }
}