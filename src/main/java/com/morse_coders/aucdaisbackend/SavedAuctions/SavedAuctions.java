package com.morse_coders.aucdaisbackend.SavedAuctions;


import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Users.Users;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "saved_auctions")
public class SavedAuctions {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGenerator")
    @SequenceGenerator(name = "SequenceGenerator", sequenceName = "saved_auctions_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "auction_product_id")
    private AuctionProducts auctionProduct;

    // date when the user saved the auction
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime date;

    public SavedAuctions() {
    }

    public SavedAuctions(Users user, AuctionProducts auctionProduct, LocalDateTime date) {
        this.user = user;
        this.auctionProduct = auctionProduct;
        this.date = date;
    }

    public SavedAuctions(Long id, Users user, AuctionProducts auctionProduct, LocalDateTime date) {
        this.id = id;
        this.user = user;
        this.auctionProduct = auctionProduct;
        this.date = date;
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
}
