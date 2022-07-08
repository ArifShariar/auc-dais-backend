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
    @JoinColumn(name = "user_id")
    private Users user;
    
    @OneToOne
    @JoinColumn(name = "max_bidder_id")
    private Users max_bidder;

    private String product_name;

    private String product_description;

    private String tags;

    private Double minimum_price;

    private Double max_bid;

    private String photos;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date auction_start_date;

    private String auction_start_time;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date auction_end_date;

    private String auction_end_time;


    private String address;

    private Boolean isApproved;

    public AuctionProducts() {
    }

    public AuctionProducts(Users user, Users max_bidder, String product_name, String product_description, String tags, Double minimum_price, Double max_bid, String photos, Date auction_start_date, String auction_start_time, Date auction_end_date, String auction_end_time, String address, Boolean isApproved) {
        this.user = user;
        this.max_bidder = max_bidder;
        this.product_name = product_name;
        this.product_description = product_description;
        this.tags = tags;
        this.minimum_price = minimum_price;
        this.max_bid = max_bid;
        this.photos = photos;
        this.auction_start_date = auction_start_date;
        this.auction_start_time = auction_start_time;
        this.auction_end_date = auction_end_date;
        this.auction_end_time = auction_end_time;
        this.address = address;
        this.isApproved = isApproved;
    }

    public AuctionProducts(Long id, Users user, Users max_bidder, String product_name, String product_description, String tags, Double minimum_price, Double max_bid, String photos, Date auction_start_date, String auction_start_time, Date auction_end_date, String auction_end_time, String address, Boolean isApproved) {
        this.id = id;
        this.user = user;
        this.max_bidder = max_bidder;
        this.product_name = product_name;
        this.product_description = product_description;
        this.tags = tags;
        this.minimum_price = minimum_price;
        this.max_bid = max_bid;
        this.photos = photos;
        this.auction_start_date = auction_start_date;
        this.auction_start_time = auction_start_time;
        this.auction_end_date = auction_end_date;
        this.auction_end_time = auction_end_time;
        this.address = address;
        this.isApproved = isApproved;
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

    public String getAuction_start_time() {
        return auction_start_time;
    }

    public void setAuction_start_time(String auction_start_time) {
        this.auction_start_time = auction_start_time;
    }

    public Date getAuction_end_date() {
        return auction_end_date;
    }

    public void setAuction_end_date(Date auction_end_date) {
        this.auction_end_date = auction_end_date;
    }

    public String getAuction_end_time() {
        return auction_end_time;
    }

    public void setAuction_end_time(String auction_end_time) {
        this.auction_end_time = auction_end_time;
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
}
