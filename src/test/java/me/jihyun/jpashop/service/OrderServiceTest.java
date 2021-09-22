package me.jihyun.jpashop.service;

import me.jihyun.jpashop.domain.Address;
import me.jihyun.jpashop.domain.Item.Book;
import me.jihyun.jpashop.domain.Item.Item;
import me.jihyun.jpashop.domain.Member;
import me.jihyun.jpashop.domain.Order;
import me.jihyun.jpashop.domain.OrderStatus;
import me.jihyun.jpashop.exception.NotEnoughStockException;
import me.jihyun.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EntityManager em;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void TestOrder() {
        //given
        Member member = createMember("회원1");

        Item book = createBook("name", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        Order gotOrder = orderRepository.findOne(orderId);

        //then
        assertEquals(OrderStatus.ORDER, gotOrder.getStatus());
        assertEquals(1, gotOrder.getOrderItems().size());
        assertEquals(10000 * orderCount, gotOrder.getTotalPrice());
        assertEquals(8, book.getStockQuantity());
    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String username) {
        Member member = Member.joinMember(username, new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    @Test
    void TestOrderIfOverStock() {
        //given
        Member member = createMember("회원1");
        Item book = createBook("name", 10000, 10);
        int overQauntity = 11;

        //when
        NotEnoughStockException thrwonException = assertThrows(NotEnoughStockException.class, ()-> {
            orderService.order(member.getId(), book.getId(), overQauntity);
        });

        assertEquals(thrwonException.getMessage(), "need more stock");




    }

    @Test
    void TestCancleOrder() {
        //given
        Member member = createMember("회원1");
        Item book = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancleOrder(orderId);

        //then
        Order order = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCLE, order.getStatus());
        assertEquals(10, book.getStockQuantity());
    }
}