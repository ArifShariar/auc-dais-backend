package com.morse_coders.aucdaisbackend.Auction_Products;

import com.morse_coders.aucdaisbackend.Users.Users;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class AuctionProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGenerator")
    @SequenceGenerator(name = "SequenceGenerator", sequenceName = "auction_products_seq", allocationSize = 1)

    private Long id;

    // create a one to many field with user
    // a user can have multiple auction products
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Users owner;
    
    @OneToOne
    @JoinColumn(name = "max_bidder_id")
    private Users max_bidder;

    private String product_name;

    private String product_description;

    private String tags;

    private Double minimum_price;

    private Double max_bid;

    private String photos;

    private Boolean isOnline;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date auction_start_date;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date auction_end_date;


    private String address;

    private Boolean isApproved;

    private Boolean isSold;

    private Boolean isOngoing;

    public AuctionProducts() {}




    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getOwner() {
        return owner;
    }

    public void setOwner(Users user) {
        this.owner = user;
    }

    public Users getMax_bidder() {
        return max_bidder;
    }

    public void setMax_bidder(Users max_bidder) {
        this.max_bidder = max_bidder;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Double getMinimum_price() {
        return minimum_price;
    }

    public void setMinimum_price(Double minimum_price) {
        this.minimum_price = minimum_price;
    }

    public Double getMax_bid() {
        return max_bid;
    }

    public void setMax_bid(Double max_bid) {
        this.max_bid = max_bid;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public Date getAuction_start_date() {
        return auction_start_date;
    }

    public void setAuction_start_date(Date auction_start_date) {
        this.auction_start_date = auction_start_date;
    }

    public Date getAuction_end_date() {
        return auction_end_date;
    }

    public void setAuction_end_date(Date auction_end_date) {
        this.auction_end_date = auction_end_date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public Boolean getSold() {
        return isSold;
    }

    public void setSold(Boolean sold) {
        isSold = sold;
    }

    public Boolean getOngoing() {
        return isOngoing;
    }

    public void setOngoing(Boolean ongoing) {
        isOngoing = ongoing;
    }
}
