package com.morse_coders.aucdaisbackend.Auction_Products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AuctionProductRepository extends JpaRepository<AuctionProducts, Long> {

    List<AuctionProducts> findAllByOwnerId(Long ownerId);
}
