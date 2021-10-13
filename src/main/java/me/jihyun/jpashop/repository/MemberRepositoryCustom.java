package me.jihyun.jpashop.repository;

import me.jihyun.jpashop.domain.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    public List<Member> findByUsername(String username);

    public Member findOne(Long id);
}
