package com.huce.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductDTO {
    private String id;
    private String name;
    private  String description;
    private long price;
    private Long quantity;
    private long salePrice;
    private long totalSold;
    private int status;
    private String dateOfManufacture; //ngày sản xuất
    private String expiry; //hạn sử dụng
    private ArrayList<String> images;
    private ArrayList<String> feedBackImages;
}
