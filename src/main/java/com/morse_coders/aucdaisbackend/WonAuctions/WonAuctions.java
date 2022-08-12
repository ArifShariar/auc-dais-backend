package com.morse_coders.aucdaisbackend.WonAuctions;


import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Users.Users;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "won_auctions")
public class WonAuctions {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGenerator")
    @SequenceGenerator(name = "SequenceGenerator", sequenceName = "won_auctions_seq", allocationSize = 1)
    private Long id;

    @OneToOne
    @JoinColumn(name = "auction_product_id")
    private AuctionProducts auctionProduct;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    private Double bid;

    private LocalDateTime date = LocalDateTime.now();

    private LocalDateTime payment_date;

    private Boolean isPaid = false;

    private PaymentMethod paymentMethod;


    public WonAuctions() {
    }


    public WonAuctions(Long id, AuctionProducts auctionProduct, Users user, Double bid, LocalDateTime date, LocalDateTime payment_date, Boolean isPaid, PaymentMethod paymentMethod) {
        this.id = id;
        this.auctionProduct = auctionProduct;
        this.user = user;
        this.bid = bid;
        this.date = date;
        this.payment_date = payment_date;
        this.isPaid = isPaid;
        this.paymentMethod = paymentMethod;
    }

    public WonAuctions(AuctionProducts auctionProduct, Users user, Double bid, LocalDateTime date, LocalDateTime payment_date, Boolean isPaid, PaymentMethod paymentMethod) {
        this.auctionProduct = auctionProduct;
        this.user = user;
        this.bid = bid;
        this.date = date;
        this.payment_date = date;
        this.isPaid = isPaid;
        this.paymentMethod = paymentMethod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuctionProducts getAuctionProduct() {
        return auctionProduct;
    }

    public void setAuctionProduct(AuctionProducts auctionProduct) {
        this.auctionProduct = auctionProduct;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    @Enumerated(EnumType.STRING)
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(LocalDateTime payment_date) {
        this.payment_date = payment_date;
    }
}
