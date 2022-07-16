package com.morse_coders.aucdaisbackend.RatingReview;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/ratingReview")
public class RatingReviewController {
    private final RatingReviewService ratingReviewService;

    @Autowired
    public RatingReviewController(RatingReviewService ratingReviewService) {
        this.ratingReviewService = ratingReviewService;
    }

    @GetMapping("/all")
    public List<RatingReview> getAllRatingReviews() {
        return ratingReviewService.getAllRatingReviews();
    }

    @GetMapping("reviewBy/{reviewerId}")
    public List<RatingReview> getRatingReviewsByOwnerId(@PathVariable("reviewerId") Long reviewerId) {
        return ratingReviewService.findAllByUserId(reviewerId);
    }

    @GetMapping("product/{productId}")
    public List<RatingReview> getRatingReviewsByProductId(@PathVariable("productId") Long productId) {
        return ratingReviewService.findAllByAuctionProductId(productId);
    }

    @GetMapping("/{ownerId}/{productId}")
    public RatingReview getRatingReviewByOwnerIdAndProductId(@PathVariable("ownerId") Long ownerId, @PathVariable("productId") Long productId) {
        return ratingReviewService.findRatingReviewByReviewerIdAndProductId(ownerId, productId);
    }

    @PostMapping("create/{reviewerId}/{productId}")
    public void createRatingReview(@RequestBody RatingReview ratingReview, @PathVariable String productId, @PathVariable String reviewerId) {
        ratingReviewService.createRatingReview(ratingReview, Long.parseLong(productId), Long.parseLong(reviewerId));
    }

    @DeleteMapping("delete/{id}")
    public void deleteRatingReview(@PathVariable("id") Long id) {
        ratingReviewService.deleteById(id);
    }



}
