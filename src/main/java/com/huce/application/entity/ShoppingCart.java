//package com.huce.application.entity;
//import lombok.*;
//
//import javax.persistence.*;
//import java.util.Set;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "shopping_cart")
//public class ShoppingCart {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "shopping_cart_id")
//    private Long id;
//    private int totalItems;
//    private double totalPrices;
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "buyer")
//    private User buyer;
//
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
//    private Set<CartItem> cartItem;
//}