package com.morse_coders.aucdaisbackend.RatingReview;

import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Users.Users;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class RatingReview {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGenerator")
    @SequenceGenerator(name = "SequenceGenerator", sequenceName = "rating_review_seq", allocationSize = 1)
    private Long id;


    // create a relationship with auction product
    // a user can have multiple rating reviews
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "auction_product_id")
    private AuctionProducts auctionProduct;

    private String review;

    private Integer rating;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    public RatingReview() {
    }

    public RatingReview(Users user, AuctionProducts auctionProduct, String review, Integer rating, Date date) {
        this.user = user;
        this.auctionProduct = auctionProduct;
        this.review = review;
        this.rating = rating;
        this.date = date;
    }

    public RatingReview(String review, Integer rating, Date date) {
        this.review = review;
        this.rating = rating;
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

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RatingReview{" +
                "id=" + id +
                ", user=" + user +
                ", auctionProduct=" + auctionProduct +
                ", review='" + review + '\'' +
                ", rating=" + rating +
                ", date=" + date +
                '}';
    }
}
