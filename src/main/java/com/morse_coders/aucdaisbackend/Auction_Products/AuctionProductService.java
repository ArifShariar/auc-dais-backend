package com.morse_coders.aucdaisbackend.Auction_Products;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AuctionProductService {
    private final AuctionProductRepository auctionProductRepository;

    public AuctionProductService(AuctionProductRepository auctionProductRepository) {
        this.auctionProductRepository = auctionProductRepository;
    }

    // Get all AuctionProducts
    public List<AuctionProducts> getAllAuctionProducts() {
        return auctionProductRepository.findAll();
    }

    // create an auction product
    public void createAuctionProduct(AuctionProducts auctionProduct){
        auctionProductRepository.save(auctionProduct);
    }


    // get all auction products of a specific user
    public List<AuctionProducts> getAllAuctionProductsOfUser(Long userId) {
        return auctionProductRepository.findAllByOwnerId(userId);
    }

    // delete an auction product by auction id
    public void deleteAuctionProduct(Long auctionId) {
        auctionProductRepository.deleteById(auctionId);
    }
}
