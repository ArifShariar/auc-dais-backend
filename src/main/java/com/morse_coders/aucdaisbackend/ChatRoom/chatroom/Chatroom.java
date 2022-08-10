package com.morse_coders.aucdaisbackend.ChatRoom.chatroom;


import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Users.Users;

import javax.persistence.*;

@Entity
@Table(name = "chatroom")

public class Chatroom{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGenerator")
    @SequenceGenerator(name = "SequenceGenerator", sequenceName = "chatroom_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Users member;

    @ManyToOne
    @JoinColumn(name = "auction_product_id")
    private AuctionProducts auctionProduct;

    public Chatroom() {}

    public Chatroom (AuctionProducts auctionProduct ) {
        this.auctionProduct = auctionProduct;
    }

    public Chatroom (Users member, AuctionProducts auctionProduct) {
        this.member = member;
        this.auctionProduct = auctionProduct;
    }

    public Chatroom(Long id, Users member, AuctionProducts auctionProduct ) {
        this.id = id;
        this.member = member;
        this.auctionProduct = auctionProduct;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getMember() {
        return member;
    }

    public void setMember(Users member) {
        this.member = member;
    }

    public AuctionProducts getAuctionProduct() {
        return auctionProduct;
    }

    public void setAuctionProduct(AuctionProducts auctionProduct) {
        this.auctionProduct = auctionProduct;
    }
}