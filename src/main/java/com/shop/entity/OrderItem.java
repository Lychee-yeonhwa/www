package com.shop.entity;

import com.shop.dto.ReviewFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter @Setter
@ToString
public class OrderItem extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)      // 지연로딩
    @JoinColumn(name ="item_id")
    private Item item;                       // 다대일 단방향 매핑

    @ManyToOne(fetch = FetchType.LAZY)       // 지연로딩
    @JoinColumn(name = "order_id")
    private Order order;                     // 다대일 단방향 매핑

    private int orderPrice;                  // 주문 가격

    private int count;                       // 수량

    @Column(nullable = false)
    private String reviewYn = "N";

    private String comment;

    // OrderItem 객체 생성
    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);                      // 주문할 상품과 주문 수량 세팅
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());     // 현재 시간 기준으로 상품 가격 세팅

        item.removeStock(count);             // 주문한 수량만큼 재고 down
        return orderItem;
    }


    public int getTotalPrice(){                     // 주문 가격 * 주문 수량 = 총 가격
        return orderPrice*count;
    }

    // 주문 취소 시 주문 수량만큼 상품의 재고를 더해줌
    public void cancel(){
        this.getItem().addStock(count);
    }

    public void createReview(ReviewFormDto reviewFormDto){
        this.comment = reviewFormDto.getComment();
    }

    public void updateReview(ReviewFormDto reviewFormDto){
        this.comment = reviewFormDto.getComment();
    }

    public void deleteReview(ReviewFormDto reviewFormDto){
        this.comment = null;
    }
}
