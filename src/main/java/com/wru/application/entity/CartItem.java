//package com.huce.application.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Setter
//@Getter
//@Entity
//@Table(name = "cart_item")
//public class CartItem {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "order_detail_id")
//    private Long id;
//    private Long quantity;
//    private double totalPrice;
//
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "shopping_cart_id", referencedColumnName = "shopping_cart_id")
//    private ShoppingCart cart;
//
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "product_id", referencedColumnName = "id")
//    private Product product;
//
//
//}
