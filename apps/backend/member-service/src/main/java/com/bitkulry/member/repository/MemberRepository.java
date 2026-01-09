package com.bitkulry.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitkulry.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
