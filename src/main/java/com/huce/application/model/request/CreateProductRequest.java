package com.huce.application.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateProductRequest {
    private String id;

    @NotBlank(message = "Tên sản phẩm trống!")
    @Size(max = 300, message = "Tên sản phẩm có độ dài tối đa 300 ký tự!")
    private String name;

    @Min(1)
    @Max(1000000000)
    @NotNull(message = "Số lượng sản phẩm trống!")
    private Long quantity;

    @NotBlank(message = "Mô tả sản phẩm trống!")
    private String description;

    @NotBlank(message = "Ngày sản xuất không được trống!")
    private String dateOfManufacture; //ngày sản xuất

    @NotBlank(message = "Hạn sử dụng không được trống!")
    private String expiry; //hạn sử dụng

    @NotNull(message = "Nhãn hiệu trống!")
    @JsonProperty("brand_id")
    private Long brandId;

    @NotNull(message = "Danh mục trống!")
    @JsonProperty("category_ids")
    private ArrayList<Integer> categoryIds;

    @NotNull(message = "Chứng nhận trống!")
    @JsonProperty("certification_ids")
    private ArrayList<Long> certification_ids;

    @Min(1000)
    @Max(1000000000)
    @NotNull(message = "Giá sản phẩm trống!")
    private Long price;

    @Min(1000)
    @Max(1000000000)
    @NotNull(message = "Giá bán sản phẩm trống!")
    private Long salePrice;

    @NotNull(message = "Danh sách ảnh trống!")
    @JsonProperty("product_images")
    private ArrayList<String> images;

    @JsonProperty("feed_back_images")
    private ArrayList<String> feedBackImages;
    private int status;
}
