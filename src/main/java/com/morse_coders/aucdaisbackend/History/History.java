package com.morse_coders.aucdaisbackend.History;


import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Users.Users;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGenerator")
    @SequenceGenerator(name = "SequenceGenerator", sequenceName = "history_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "auction_product_id")
    private AuctionProducts auctionProduct;

    // date when the user put a bid on the auction
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private Double bid_amount;

    public History() {
    }

    public History(Users user, AuctionProducts auctionProduct, LocalDateTime date, Double bid_amount) {
        this.user = user;
        this.auctionProduct = auctionProduct;
        this.date = date;
        this.bid_amount = bid_amount;
    }

    public History(Long id, Users user, AuctionProducts auctionProduct, LocalDateTime date, Double bid_amount) {
        this.id = id;
        this.user = user;
        this.auctionProduct = auctionProduct;
        this.date = date;
        this.bid_amount = bid_amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public AuctionProducts getAuctionProduct() {
        return auctionProduct;
    }

    public void setAuctionProduct(AuctionProducts auctionProduct) {
        this.auctionProduct = auctionProduct;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getBid_amount() {
        return bid_amount;
    }

    public void setBid_amount(Double bid_amount) {
        this.bid_amount = bid_amount;
    }
}
