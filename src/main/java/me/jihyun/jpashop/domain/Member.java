package me.jihyun.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String username;

    /*
    Embeddable - Embedded
    - Entity에서 속성이 가질 타입을 정의하고 사용할 때 사용하는 애노테이션
    - 정의 하는 곳에 Embeddable, 사용하는 곳에 Embedded를 붙여줌
    - 둘 중 하나만 해주어도 되지만, 관용적으로 두 군데 다 붙여줌 -> 가독성이 좋아짐
     */
    @Embedded
    private Address address;


    /*
    필드에서 컬렉션을 초기화 하는 이유
    1. null 문제에서 안전
    2. JPA가 영속화하면 엔티티의 컬렉션을 하이버네이트가 제공하는 컬렉션으로 변경하여 관리한다.
       즉, 하이버네이트가 관리하기 위해서 하이버네이트가 제공하는 컬렉션 타입이 되어야하는데,
       별도의 로직 혹은 메소드에서 초기화를 해주는 경우 자바 내장 컬렉션으로 타입이 변경될 가능성이 있기 때문에
       하이버네이트가 동작하는데 오류가 발생할수 있다. 따라서, 필드에서 초기화해주고 안건드리는 것이 "Best Practice"이다!!
     */
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public static Member of(String username, Address address) {
        Member newMember = new Member();
        newMember.username = username;
        newMember.address = address;

        return newMember;
    }

    public void updateAddress(Address address) {
        this.address = address;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

}
