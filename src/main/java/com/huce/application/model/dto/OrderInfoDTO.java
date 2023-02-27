package com.huce.application.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderInfoDTO {
    private long id;

    private long totalPrice;

    private Long quantity;

    private String productName;

    private String productImg;

    public OrderInfoDTO(long id, long totalPrice, Long quantity, String productName, String productImg) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.productName = productName;
        this.productImg = productImg;
    }
}