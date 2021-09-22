package me.jihyun.jpashop.repository;

import lombok.Getter;
import lombok.Setter;
import me.jihyun.jpashop.domain.OrderStatus;

@Getter @Setter
public class OrderSearch {

    private String memberName;

    private OrderStatus orderStatus;
}
