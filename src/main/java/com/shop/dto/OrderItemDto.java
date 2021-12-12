package com.shop.dto;

import com.shop.constant.GiftStatus;
import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.orderShippingFee = orderItem.getItem().getShippingFee();
        this.imgUrl = imgUrl;
        this.comment = orderItem.getComment();
        this.orderItemId = orderItem.getId();
        this.reviewYn = orderItem.getReviewYn();
    }

    private String itemNm;    // 상품명

    private int count;      // 주문 수량

    private int orderPrice; // 주문 금액

    private int orderShippingFee;

    private String imgUrl;  // 상품 이미지 경로

    private String comment;

    private Long orderItemId;

    private String reviewYn;


}
