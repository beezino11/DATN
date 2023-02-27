package com.huce.application.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Setter
@Getter
public class ProductInfoDTO {
    private String id;
    private String name;
    private String slug;
    private long price;
    private int views;
    private String images;
    private int totalSold;

    private long promotionPrice;

//    private String dateOfManufacture; //ngày sản xuất
//
//    private String expiry; //hạn sử dụng

    public ProductInfoDTO(String id, String name, String slug, long price, int views, String images, int totalSold) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.price = price;
        this.views = views;
        this.images = images;
        this.totalSold = totalSold;
    }
}