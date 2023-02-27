//package com.huce.application.entity;
//
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.util.List;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//@Entity
//@Table(name = "orders_n")
//public class OrderN {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "order_id")
//    private Long id;
//    @Column(name = "receiver_name")
//    private String receiverName;
//    @Column(name = "receiver_phone")
//    private String receiverPhone;
//    @Column(name = "receiver_address")
//    private String receiverAddress;
//    @Column(name = "total_price")
//    private double totalPrice;
//    @Column(name = "status")
//    private int status;
//    @Column(name = "notes")
//    private String notes;
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "buyer")
//    private User buyer;
//
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderN")
//    private List<OrderDetailN> orderDetailList;
//}