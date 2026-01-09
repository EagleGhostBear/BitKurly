package com.bitkulry.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bitkulry.member.dto.request.SignUpRequest;
import com.bitkulry.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/members") // 2. 이 컨트롤러의 공통 URL 프리픽스
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/hello")
    @ResponseBody
    public String test() {
        return "hello?";
    }

    /*
     * 회원가입 API
     * [POST] http://localhost:8080/api/v1/members/signup
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request) {
        // @RequestBody: 프론트에서 보낸 JSON 데이터를 SignUpRequest 객체로 변환해줘!

        memberService.signUp(request); // 서비스에게 일 시키기

        // 201 Created 상태코드와 함께 응답
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
