package com.bitkulry.member.dto.request;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter // DTO는 @Data를 막 써도 됩니다. 단순 데이터 전달용이니까요.
public class SignUpRequest {
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String role;
    private String grade;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}