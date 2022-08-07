package com.morse_coders.aucdaisbackend.LiveAuctions;


import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Users.Users; 

import javax.persistence.*; 

@Entity
@Table(name = "live_auctions")

public class LiveAuctions{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGenerator")
    @SequenceGenerator(name = "SequenceGenerator", sequenceName = "live_auctions_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "auction_product_id")
    private AuctionProducts auctionProduct;

    public  LiveAuctions(){}

    public LiveAuctions(AuctionProducts auctionProduct ) { 
        this.auctionProduct = auctionProduct; 
    }

    public LiveAuctions(Long id, Users user, AuctionProducts auctionProduct ) {
        this.id = id;
        this.user = user;
        this.auctionProduct = auctionProduct; 
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


}