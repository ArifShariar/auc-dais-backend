package com.morse_coders.aucdaisbackend.Auction_Products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AuctionProductRepository extends JpaRepository<AuctionProducts, Long> {

    List<AuctionProducts> findAllByOwnerId(Long ownerId);

    // implement search functionality
    @Query(value = "SELECT * FROM auction_products WHERE product_name ILIKE %?1% OR product_description ILIKE %?1% OR tags ILIKE %?1%", nativeQuery = true)
    List<AuctionProducts> findAllByproduct_nameOrproduct_descriptionOrTags(String search);

    // get auctions that have auction_start_time before current time and is_ongoing set to false
    @Query(value = "SELECT * FROM auction_products WHERE is_ongoing = false", nativeQuery = true)
    List<AuctionProducts> getAllNotOngoingAuctions();

    // get auctions that are not finished and is_ongoing set to true
    @Query(value = "SELECT * FROM auction_products WHERE is_ongoing = true AND is_sold = false", nativeQuery = true)
    List<AuctionProducts> getAllOngoingButNotSoldAuctions();

    @Query(value = "SELECT * FROM auction_products WHERE is_ongoing = true AND is_sold = false AND owner_id != ?1", nativeQuery = true)
    List<AuctionProducts> getAllOnGoingAuctions(Long ownerId);
}
