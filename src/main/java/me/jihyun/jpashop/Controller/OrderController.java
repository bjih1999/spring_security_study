package me.jihyun.jpashop.Controller;

import lombok.RequiredArgsConstructor;
import me.jihyun.jpashop.domain.Item.Item;
import me.jihyun.jpashop.domain.Member;
import me.jihyun.jpashop.domain.Order;
import me.jihyun.jpashop.repository.OrderSearch;
import me.jihyun.jpashop.service.ItemService;
import me.jihyun.jpashop.service.MemberService;
import me.jihyun.jpashop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final MemberService memberService;

    private final ItemService itemService;

    @PostMapping("/order")
    public Long order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {
        return orderService.order(memberId, itemId, count);
    }

    @GetMapping("/orders")
    public List<Order> orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch) {
        return orderService.findOrders(orderSearch);
    }

    @PostMapping("/orders/{orderId}/cancle")
    public Long orderCancel(@PathVariable("orderId") Long orderId) {
        return orderService.cancleOrder(orderId);
    }

}
