package com.morse_coders.aucdaisbackend.RatingReview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingReviewRepository extends JpaRepository<RatingReview, Long> {

        List<RatingReview> findAllByUserId(Long reviewerId);

        List<RatingReview> findAllByAuctionProductId(Long productId);

        RatingReview findRatingReviewByAuctionProductIdAndUserId(Long ownerId, Long productId);


}
