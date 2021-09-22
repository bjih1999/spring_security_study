package me.jihyun.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.jihyun.jpashop.domain.Member;
import me.jihyun.jpashop.domain.Order;
import me.jihyun.jpashop.domain.OrderStatus;
import me.jihyun.jpashop.domain.QOrder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    private final static QOrder order = QOrder.order;

//    public void save(Order order) {
//        em.persist(order);
//    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    /*
    아래 코드의 문제점
        -> 파라미터가 null인 경우 모든 검색 결과를 출력하는 처리를 할 수 없음

        첫번째 해결방안
            값을 확인한 후 if로 null체크를 하여 jpql String을 조립한다.
                -> 좋지 않은 접근, 코드가 많고 복잡해지며 버그가 발생하기 쉬움

        두번째 해결 방안
            JPA Criteria를 사용
                -> 실무에서 지양하는 것을 추천, 표준으로 JPA가 제공하긴하지만
                치명적인 단점이 있음. 유지보수성이 좋지 않음.
     */
//    public List<Order> findAll(OrderSearch orderSearch) {
//        return em.createQuery("select o from Order o join o.member m" +
//                " where o.status =:status " +
//                " and m.username like :username", Order.class)
//                .setParameter("status", orderSearch.getOrderStatus())
//                .setParameter("username", orderSearch.getMemberName())
//                .setFirstResult(100)    //setFirstResult : 100번째 부터
//                .setMaxResults(1000)    //setMaxResult : 1000개  -> 이 2개를 사용해서 pagination 구현
//                .getResultList();
//    }

    public List<Order> findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.username like :username";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    /*
        세번째 해결 방안(추천)
        Querydsl로 처리
     */

    //일단은 이걸로 진행하자
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("username"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();
    }

    public List<Order> findAll(OrderSearch orderSearch) {

        return queryFactory.selectFrom(order)
                .where(memberNameLike(orderSearch.getMemberName()),
                        orderStatusEq(orderSearch.getOrderStatus()))
                .fetch();
    }

    private BooleanExpression memberNameLike(String memberName) {
        return memberName != null ? order.member.username.like(memberName) : null;
    }

    private BooleanExpression orderStatusEq(OrderStatus orderStatus) {
        return orderStatus != null ? order.status.eq(orderStatus) : null;
    }

}
