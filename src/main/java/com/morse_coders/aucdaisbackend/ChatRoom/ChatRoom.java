package com.morse_coders.aucdaisbackend.ChatRoom;


import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Users.Users;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGenerator")
    @SequenceGenerator(name = "SequenceGenerator", sequenceName = "chat_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "auction_id")
    private AuctionProducts auction;


    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Users sender;


    private String message;


    LocalDateTime sent_at = LocalDateTime.now();

    public ChatRoom() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuctionProducts getAuction() {
        return auction;
    }

    public void setAuction(AuctionProducts auction) {
        this.auction = auction;
    }

    public Users getSender() {
        return sender;
    }

    public void setSender(Users sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSent_at() {
        return sent_at;
    }

    public void setSent_at(LocalDateTime sent_at) {
        this.sent_at = sent_at;
    }
}
