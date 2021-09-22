package me.jihyun.jpashop.service;

import me.jihyun.jpashop.domain.Member;
import me.jihyun.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void joinTest() {
        //given
        Member member = new Member();
        member.setUsername("Jihyun");

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    void joinTestIfDuplicate() {
        //given
        Member member = new Member();
        member.setUsername("Jihyun");

        Member duplicatedMember = new Member();
        duplicatedMember.setUsername("Jihyun");

        //when
        memberService.join(member);

        //then
        IllegalStateException thrownException = assertThrows(IllegalStateException.class, () -> {
            memberService.join(duplicatedMember);
        });
        assertEquals(thrownException.getMessage(), "이미 존재하는 회원입니다.");

    }
}