package com.morse_coders.aucdaisbackend.SavedAuctions;


import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProductRepository;
import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Users.Users;
import com.morse_coders.aucdaisbackend.Users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SavedAuctionService {
    private final SavedAuctionRepository savedAuctionRepository;
    private final AuctionProductRepository auctionProductRepository;

    private final UsersRepository usersRepository;


    @Autowired
    public SavedAuctionService(SavedAuctionRepository savedAuctionRepository, AuctionProductRepository auctionProductRepository, UsersRepository usersRepository) {
        this.savedAuctionRepository = savedAuctionRepository;
        this.auctionProductRepository = auctionProductRepository;
        this.usersRepository = usersRepository;
    }

    public List<SavedAuctions> getAllSavedAuctions() {
        return savedAuctionRepository.findAll();
    }


    public void createSavedAuction(SavedAuctions savedAuctions, Long userId, Long auctionId) {

        Optional<Users> user = usersRepository.findById(userId);
        Optional<AuctionProducts> auctionProduct = auctionProductRepository.findById(auctionId);

        if (user.isPresent() && auctionProduct.isPresent()) {
            // check if same user saved the same product, if yes, return error
            if (savedAuctionRepository.findByUserIdAndAuctionId(userId, auctionId) != null) {
                throw new IllegalArgumentException("User already saved this product");
            }
            savedAuctions.setUser(user.get());
            savedAuctions.setAuctionProduct(auctionProduct.get());
            savedAuctionRepository.save(savedAuctions);
        }
        else{
            System.out.println("User or AuctionProduct not found");
        }
    }

    public void deleteSavedAuction(Long savedAuctionId) {
        savedAuctionRepository.deleteById(savedAuctionId);
    }

    public SavedAuctions getSavedAuctionById(long id) {
        if (savedAuctionRepository.findById(id).isPresent()){
            return savedAuctionRepository.findById(id).get();
        }
        return null;
    }


    public List<SavedAuctions> getAllSavedAuctionsOfUser(Long userId) {
        return savedAuctionRepository.findAllByUserId(userId);
    }

    // all saved auction by user id and auction id
    public SavedAuctions getSavedAuctionByUserIdAndAuctionId(Long userId, Long auctionId) {
        return savedAuctionRepository.findSavedAuctionByUserIdAndAuctionProductId(userId, auctionId);
    }

    // all saved auction by user id and auction id before a specific date
    public List<SavedAuctions> getAllSavedAuctionsByUserIdAndAuctionIdBeforeDate(Long userId, String date) {
        return savedAuctionRepository.findAllByUserIdAndAuctionProductIdBeforeDate(userId, date);
    }

    // all saved auction by user id and auction id after a specific date
    public List<SavedAuctions> getAllSavedAuctionsByUserIdAndAuctionIdAfterDate(Long userId, String date) {
        return savedAuctionRepository.findAllByUserIdAndAuctionProductIdAfterDate(userId, date);
    }

    public void deleteSavedAuctionByUserIdAndAuctionId(Long userId, Long auctionId) {
        Optional<Users> users = usersRepository.findById(userId);
        Optional<AuctionProducts> auctionProduct = auctionProductRepository.findById(auctionId);
        if (users.isPresent() && auctionProduct.isPresent()) {
            savedAuctionRepository.deleteSavedAuctionByUserIdAndAuctionProductId(userId, auctionId);
        }
        else{
            System.out.println("User or AuctionProduct not found");
        }
    }
}
