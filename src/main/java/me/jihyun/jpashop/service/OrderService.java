package me.jihyun.jpashop.service;

import lombok.RequiredArgsConstructor;
import me.jihyun.jpashop.domain.Delivery;
import me.jihyun.jpashop.domain.Item.Item;
import me.jihyun.jpashop.domain.Member;
import me.jihyun.jpashop.domain.Order;
import me.jihyun.jpashop.domain.OrderItem;
import me.jihyun.jpashop.repository.ItemRepository;
import me.jihyun.jpashop.repository.MemberRepository;
import me.jihyun.jpashop.repository.OrderRepository;
import me.jihyun.jpashop.repository.OrderSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final MemberRepository memberRepository;

    private final ItemRepository itemRepository;
    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = Delivery.of(member.getAddress());

        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.of(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public Long cancleOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancle();
        return order.getId();
    }

    //검색
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByCriteria(orderSearch);
    }
}
