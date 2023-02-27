package com.huce.application.model.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

@Setter
@Getter
public class ShortProductInfoDTO {
    private String id;

    private String name;

    private long price;

//    List<Integer> availableSizes;
    private Long quantity;

    public ShortProductInfoDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public ShortProductInfoDTO(String id, String name, long price, Long quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
//        if (availableSizes != null) {
//            ObjectMapper mapper = new ObjectMapper();
//            try {
//                this.availableSizes = mapper.readValue((String) availableSizes, new TypeReference<List<Integer>>(){});
//            } catch (IOException e) {
//                this.availableSizes = null;
//            }
//        }
    }
}