package me.jihyun.jpashop.service;

import lombok.RequiredArgsConstructor;
import me.jihyun.jpashop.domain.Member;
import me.jihyun.jpashop.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
/*
@Transactional은 자바에서 제공하는 것과 스프링에서 제공하는 것이 있다.
    - 스프링에서 제공하는 것이 더 많은 옵션을 제공하기 때문에, 스프링 것을 사용하는 것을 권장

readOnly = true
    - DB에 따라 읽기 전용으로 명령을 주어 리소스를 절약할 수도 있다.(드라이버에 따라 다름)

    ** 클래스에 Transactional(readOnly = true)를 달아 놓으면 모든 메소드가 읽기 전용 모드 트랜잭션을 사용하게 되며
        쓰기 트랜잭션이 필요한 메소드에는 Transactional을 다시 달아두면 readOnly 명령을 재지정할 수 있다.(기본 값이 readOnly = false) **
        -> 이 방법을 권장
*/
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /*
    회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /*
    굳이 username이 일치하는 모든 사람을 읽어올 필요가 없기 때문에
    Repository에 member가 있는지 유무를 확인하는 조건자를 생성해둠으로써 최적화가 가능할 것 같다.
     */

    /*
    아래의 방법 또한 문제의 여지가 있는데, 멀티 스레드의 환경에서 2개의 같은 username의 요청이 동시에 들어오면
     둘 다 요청이 통과하게 된다. 따라서 최후의 보험으로 username(로그인 아이디)에 unique 제약을 걸어두는 것이 바람직하다.
     */
    private void validateDuplicateMember(Member member) throws IllegalStateException {
        List<Member> foundMembers = memberRepository.findByUsername(member.getUsername());
        if (!foundMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 전체 회원 조회
    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
