package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainItemDto {

    private Long id;

    private Long cateCode;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;


    // 상품 조회 시 DTO 객체로 결과 값을 받음
    @QueryProjection
    public MainItemDto(Long id, String itemNm, String itemDetail,
                       String imgUrl, Integer price){
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}
