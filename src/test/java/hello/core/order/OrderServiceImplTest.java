package hello.core.order;

import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemoryMemeberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    @Test
    void createOrder() {
        MemoryMemeberRepository memoryMemeberRepository = new MemoryMemeberRepository();
        memoryMemeberRepository.save(new Member(1L, "name", Grade.VIP));

        OrderServiceImpl orderService = new OrderServiceImpl(memoryMemeberRepository, new FixDiscountPolicy());
        Order order = orderService.createOrder(1L, "itemA", 100000);

        assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }

}