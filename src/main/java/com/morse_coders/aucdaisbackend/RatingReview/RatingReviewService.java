package com.morse_coders.aucdaisbackend.RatingReview;

import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProductRepository;
import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Users.Users;
import com.morse_coders.aucdaisbackend.Users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingReviewService {
    private final RatingReviewRepository ratingReviewRepository;

    private final UsersRepository usersRepository;
    private final AuctionProductRepository auctionProductRepository;

    @Autowired
    public RatingReviewService(RatingReviewRepository ratingReviewRepository, UsersRepository usersRepository, AuctionProductRepository auctionProductRepository) {
        this.ratingReviewRepository = ratingReviewRepository;
        this.usersRepository = usersRepository;
        this.auctionProductRepository = auctionProductRepository;
    }

    public List<RatingReview> findAllByUserId(Long userId) {
        return ratingReviewRepository.findAllByUserId(userId);
    }

    public List<RatingReview> findAllByAuctionProductId(Long productId) {
        return ratingReviewRepository.findAllByAuctionProductId(productId);
    }

    public RatingReview findRatingReviewByReviewerIdAndProductId(Long reviewerId, Long productId) {
        return ratingReviewRepository.findRatingReviewByAuctionProductIdAndUserId(reviewerId, productId);
    }

    public void createRatingReview(RatingReview ratingReview, Long productId, Long reviewerId) {
        RatingReview newRatingReview = new RatingReview();
        // find the user by the id
        Optional<Users> user = usersRepository.findById(reviewerId);
        // find the product by the id
        Optional<AuctionProducts> product = auctionProductRepository.findById(productId);

        if (user.isPresent() && product.isPresent()) {
            newRatingReview.setId(ratingReview.getId());
            newRatingReview.setUser(user.get());
            newRatingReview.setAuctionProduct(product.get());
            newRatingReview.setRating(ratingReview.getRating());
            newRatingReview.setReview(ratingReview.getReview());
            newRatingReview.setDate(ratingReview.getDate());
            ratingReviewRepository.save(newRatingReview);
        }
        else{
            System.out.println("User or product or seller not found");
        }
    }

    public void deleteById(Long id) {
        ratingReviewRepository.deleteById(id);
    }


    public List<RatingReview> getAllRatingReviews() {
        return ratingReviewRepository.findAll();
    }
}
