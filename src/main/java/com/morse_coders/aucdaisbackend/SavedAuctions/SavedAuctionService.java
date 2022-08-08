package com.morse_coders.aucdaisbackend.SavedAuctions;


import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProductRepository;
import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Session.SessionToken;
import com.morse_coders.aucdaisbackend.Session.SessionTokenRepository;
import com.morse_coders.aucdaisbackend.Users.Users;
import com.morse_coders.aucdaisbackend.Users.UsersRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SavedAuctionService {
    private final SavedAuctionRepository savedAuctionRepository;
    private final AuctionProductRepository auctionProductRepository;

    private final UsersRepository usersRepository;

    private final SessionTokenRepository sessionTokenRepository;

    @Autowired
    public SavedAuctionService(SavedAuctionRepository savedAuctionRepository, AuctionProductRepository auctionProductRepository, UsersRepository usersRepository, SessionTokenRepository sessionTokenRepository) {
        this.savedAuctionRepository = savedAuctionRepository;
        this.auctionProductRepository = auctionProductRepository;
        this.usersRepository = usersRepository;
        this.sessionTokenRepository = sessionTokenRepository;
    }

    public List<SavedAuctions> getAllSavedAuctions() {
        return savedAuctionRepository.findAll();
    }


    public void createSavedAuction(SavedAuctions savedAuctions, Long userId, Long auctionId, String token) {

        Optional<Users> user = usersRepository.findById(userId);
        Optional<AuctionProducts> auctionProduct = auctionProductRepository.findById(auctionId);

        if (user.isPresent() && auctionProduct.isPresent()) {
            Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(user.get(), LocalDateTime.now());
            if(getUserToken.isPresent()){
                if(getUserToken.get().getToken().equals(token)){
                    // check if same user saved the same product, if yes, return error
                    if (savedAuctionRepository.findByUserIdAndAuctionId(userId, auctionId) != null) {
                        throw new IllegalArgumentException("User already saved this product");
                    }
                    savedAuctions.setUser(user.get());
                    savedAuctions.setAuctionProduct(auctionProduct.get());
                    savedAuctionRepository.save(savedAuctions);
                }
                else {
                    throw new RuntimeException("Token is expired / not valid");
                }

            }
            else {
                throw new RuntimeException("Token is expired / not valid");
            }

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


    public List<SavedAuctions> getAllSavedAuctionsOfUser(Long userId, String token) {
        Optional<Users> user = usersRepository.findById(userId);
        if(user.isPresent()){
            Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(user.get(), LocalDateTime.now());
            if(getUserToken.isPresent()){
                if(getUserToken.get().getToken().equals(token)){
                    return savedAuctionRepository.findAllByUserId(userId);
                }
                else {
                    throw new RuntimeException("Token is expired / not valid");
                }

            }
            else {
                throw new RuntimeException("Token is expired / not valid");
            }

        }
        else{
            System.out.println("User not found");
        }
        return null;
    }

    // all saved auction by user id and auction id
    public SavedAuctions getSavedAuctionByUserIdAndAuctionId(Long userId, Long auctionId) {
        return savedAuctionRepository.findSavedAuctionByUserIdAndAuctionProductId(userId, auctionId);
    }

    // all saved auction by user id and auction id before a specific date
    public List<SavedAuctions> getAllSavedAuctionsByUserIdAndAuctionIdBeforeDate(Long userId, String date, String token) {
        Optional<Users> user = usersRepository.findById(userId);
        if(user.isPresent()){
            Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(user.get(), LocalDateTime.now());
            if(getUserToken.isPresent()){
                if(getUserToken.get().getToken().equals(token)){
                    return savedAuctionRepository.findAllByUserIdAndAuctionProductIdBeforeDate(userId, date);
                }
                else {
                    throw new RuntimeException("Token is expired / not valid");
                }
            }
            else {
                throw new RuntimeException("Token is expired / not valid");
            }
        }
        else{
            System.out.println("User not found");
        }
        return null;
    }

    // all saved auction by user id and auction id after a specific date
    public List<SavedAuctions> getAllSavedAuctionsByUserIdAndAuctionIdAfterDate(Long userId, String date, String token) {
        Optional<Users> user = usersRepository.findById(userId);
        if (user.isPresent()){
            Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(user.get(), LocalDateTime.now());
            if(getUserToken.isPresent()){
                if(getUserToken.get().getToken().equals(token)){
                    return savedAuctionRepository.findAllByUserIdAndAuctionProductIdAfterDate(userId, date);
                }
                else {
                    throw new RuntimeException("Token is expired / not valid");
                }
            }
            else {
                throw new RuntimeException("Token is expired / not valid");
            }
        }
        else{
            System.out.println("User not found");
        }
        return null;
    }

    public void deleteSavedAuctionByUserIdAndAuctionId(Long userId, Long auctionId, String token) {
        Optional<Users> users = usersRepository.findById(userId);
        Optional<AuctionProducts> auctionProduct = auctionProductRepository.findById(auctionId);
        if (users.isPresent() && auctionProduct.isPresent()) {
            Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(users.get(), LocalDateTime.now());
            if(getUserToken.isPresent()){
                if(getUserToken.get().getToken().equals(token)){
                    savedAuctionRepository.deleteSavedAuctionByUserIdAndAuctionProductId(userId, auctionId);
                }
                else {
                    throw new RuntimeException("Token is expired / not valid");
                }

            }
            else {
                throw new RuntimeException("Token is expired / not valid");
            }

        }
        else{
            System.out.println("User or AuctionProduct not found");
        }
    }
}
