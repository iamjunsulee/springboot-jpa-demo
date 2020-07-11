package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;

    /*
    EnumType.ORDINAL : enum 순서값을 DB에 저장
    EnumType.STRING : enum 이름을 DB에 저장
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태[ORDER, CANCEL]

    /*
    연관관계 편의 메서드
    양방향일때, 양쪽에 다 넣어줘야한다.
    */
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void setOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    /* 생성 메서드 */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.setOrderItem(orderItem);
        }

        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    /* 비지니스 로직 */
    /**
     * 주문취소
     */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMPLETE){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : this.orderItems){
            orderItem.cancel();
        }
    }

    /**
     * 전체 주문 가격 조회
     * @return
     */
    public int getTotalPrice(){
        return this.orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
}
