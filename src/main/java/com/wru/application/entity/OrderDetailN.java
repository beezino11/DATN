//package com.huce.application.entity;
//
//import lombok.*;
//
//import javax.persistence.*;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//@Entity
//@Table(name = "order_detail_n")
//public class OrderDetailN {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "order_detail_id")
//    private Long id;
//
//    private Long quantity;
//    private double totalPrice;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
//    private OrderN orderN;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id", referencedColumnName = "id")
//    private Product product;
//
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class UsedPromotionN {
//        private String couponCode;
//
//        private int discountType;
//
//        private long discountValue;
//
//        private long maximumDiscountValue;
//    }
//}