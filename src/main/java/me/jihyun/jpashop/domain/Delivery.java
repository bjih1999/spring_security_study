package me.jihyun.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    /*
    @Enumerated(EnumType.STRING)
    - 기본 값은 EnumType.ORDINAL
    - ORDINAL은 enum값이 1, 2, 3... 과 같은 방식으로 저장됨
        -> 중간에 다른 상태 값이 추가되면 망함 ** ORDINAL은 절대 사용하지 말것! **
     */
    private DeliveryStatus status;

    public static Delivery of(Address address) {
        Delivery delivery = new Delivery();
        delivery.setAddress(address);
        delivery.setStatus(DeliveryStatus.READY);
        return delivery;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}
