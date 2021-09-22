package me.jihyun.jpashop.repository;

import lombok.RequiredArgsConstructor;
import me.jihyun.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/*
스프링 데이터 JPA로 간단하게 생성 가능하지만
JPA 자체를 다루는 것이 중요하다!
-> JPA로 개발한 후 추 후 스프링 데이터 JPA로 전환하자!
 */
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        /*
        기본적으로 persist를 한다고 디비에 insert문이 전달되진 않는다.
            Q: 그러면 언제 디비에 insert 되느냐?
            A: Transaction이 커밋 될 때. 
         */
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByUsername(String username) {
        return em.createQuery("select m from Member m where m.username=:username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

}
