package com.bitkulry.member.dto.request;

import lombok.Getter;

@Getter
public class PasswordChangeRequest {
    private String currentPassword; // 현재 비번 (확인용)
    private String newPassword;     // 바꿀 비번
}
