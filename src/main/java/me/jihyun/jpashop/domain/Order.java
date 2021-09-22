package me.jihyun.jpashop.domain;

import lombok.*;
import me.jihyun.jpashop.domain.Item.Item;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
/*
테이블 이름을 orders로 지정해주는 이유
- 지정해주지 않으면 자동으로 테이블 이름이 order가 되면서 order by 키워드와 충돌하는 경우가 생김
- 따라서, 관용적으로 order 테이블은 orders로 자주 사용함
 */
@Table(name ="orders")
/*
    NoArgsContructor(access = AccessLeve.PROTECTED)
    접근자가 protected인 디폴트 생성자를 생성해 주는 롬복
        - JPA 스펙에서 생성자는 protected까지 지원
        - 이렇게 해두면 다른 디렉터리에서 쓸 수 없음
        - 이 말은 즉슨, 이 생성자 쓰지 말고 생성 메소드를 사용을 해라. 라는 의미
        - 생성 메소드 외에 다른 생성 로직이 있을 경우 유지 보수가 어려워 지기 때문
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order {

    @Id @GeneratedValue
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    /*
    cascade = CascadeType.ALL
        - Order가 persist될 때, OrderItem도 종속적으로 persist를 함

        Q: cascade를 언제 사용하는가?
        A: 하위객체를 하나의 상위객체가 유일하게 관리하고 다른 객체가 참조하지 않을때 #private owner (ex. Order - Deilvery, Order = OrderItems)
     */
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    /*
    OneToOne 을 사용할 때의 고려 사항
    - 어디에 FK를 두어야할까?
        -> 접근이 많은 Entity에 둔다!

    - 현 시스템에는 Order를 통해 Delivery를 조회하는 상황을 가정하고 있기 때문에
      Order가 Delivery를 참조하는 것이 바람직 하다.
     */
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDateTime;    // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [ORDER, CANCLE]

    //==연관관계 메서드==//
    /*
    양방향 관계에서 사용할 때 편리함
    연관 관계 세팅 로직을 원자적으로 묶어 놓은 메소드
    둘 중 관계의 주도권을 갖는 엔티티에 추가한다.
     */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메소드==//
    public static Order of(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.orderDateTime = LocalDateTime.now();

        return order;
    }

    //==비즈니스 로직==//
    public void cancle() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCLE);
        for (OrderItem orderItem : this.orderItems) {
            orderItem.cancle();
        }
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    //==조회 로직==//
    public int getTotalPrice() {
        int totalPrice = this.orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        return totalPrice;
    }

}
