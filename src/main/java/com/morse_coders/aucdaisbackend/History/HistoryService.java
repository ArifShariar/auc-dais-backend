package com.morse_coders.aucdaisbackend.History;

import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProductRepository;
import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Session.SessionToken;
import com.morse_coders.aucdaisbackend.Session.SessionTokenRepository;
import com.morse_coders.aucdaisbackend.Users.Users;
import com.morse_coders.aucdaisbackend.Users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    private final UsersRepository usersRepository;

    private final AuctionProductRepository auctionProductRepository;

    private final SessionTokenRepository sessionTokenRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository, UsersRepository usersRepository, AuctionProductRepository auctionProductRepository, SessionTokenRepository sessionTokenRepository) {
        this.historyRepository = historyRepository;
        this.usersRepository = usersRepository;
        this.auctionProductRepository = auctionProductRepository;
        this.sessionTokenRepository = sessionTokenRepository;
    }

    public List<History> getAllHistory() {
        return historyRepository.findAll();
    }

    public List<History> getAllHistoryByUserId(Long userId, String token) {
        Users user = usersRepository.findById(userId).isPresent() ? usersRepository.findById(userId).get() : null;
        if (user!=null){
            Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(user, LocalDateTime.now());
            if (getUserToken.isPresent()) {
                if (getUserToken.get().getToken().equals(token)) {
                    return historyRepository.findAllByUserId(user.getId());
                }
            }
            else{
                throw new RuntimeException("Token is expired / not valid");
            }
        }
        return null;
    }

    public List<History> getAllHistoryByUserIdAndAuctionProductId(Long userId, Long auctionProductId, String token) {
        Users user = usersRepository.findById(userId).isPresent() ? usersRepository.findById(userId).get() : null;
        if (user!=null){
            Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(user, LocalDateTime.now());
            if (getUserToken.isPresent()) {
                if (getUserToken.get().getToken().equals(token)) {
                    return historyRepository.findHistoryByUserIdAndAuctionProductId(user.getId(), auctionProductId);
                }
            }

            else{
                throw new RuntimeException("Token is expired / not valid");
            }
        }
        return null;
    }

    public List<History> getAllHistoryByUserIdAndAuctionProductIdBeforeDate(Long userId, String date, String token) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        Users user = usersRepository.findById(userId).isPresent() ? usersRepository.findById(userId).get() : null;
        if (user!=null){
            Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(user, LocalDateTime.now());
            if (getUserToken.isPresent()) {
                if (getUserToken.get().getToken().equals(token)) {
                    return historyRepository.findAllByUserIdAndDateBefore(user.getId(), dateTime);
                }
            }
            else{
                throw new RuntimeException("Token is expired / not valid");
            }
        }
        return null;
    }

    public List<History> getAllHistoryByUserIdAndAuctionProductIdAfterDate(Long userId, String date, String token) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        Users user = usersRepository.findById(userId).isPresent() ? usersRepository.findById(userId).get() : null;
        if (user!=null){
            Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(user, LocalDateTime.now());
            if (getUserToken.isPresent()) {
                if (getUserToken.get().getToken().equals(token)) {
                    return historyRepository.findAllByUserIdAndDateAfter(user.getId(), dateTime);
                }
            }
            else{
                throw new RuntimeException("Token is expired / not valid");
            }
        }
        return null;
    }

    public void createHistory(Double bid, Long userId, Long auctionProductId, String token) {
        Optional<Users> user = usersRepository.findById(userId);
        Optional<AuctionProducts> auctionProduct = auctionProductRepository.findById(auctionProductId);

        if (user.isPresent() && auctionProduct.isPresent()) {
            Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(user.get(), LocalDateTime.now());

            if (getUserToken.isPresent()) {
                if (getUserToken.get().getToken().equals(token)) {
                    History history = new History();
                    history.setBid_amount(bid);
                    history.setUser(user.get());
                    history.setAuctionProduct(auctionProduct.get());
                    history.setDate(LocalDateTime.now());
                    historyRepository.save(history);
                }
            }
            else {
                throw new RuntimeException("Token is expired");
            }


        }
        else{
            System.out.println("User or AuctionProduct not found");
        }
    }


    public List<History> getHistoryByUserIdAndAuctionIdBetweenDates(Long userId, String startDate, String endDate, String token) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localStartDate = LocalDateTime.parse(startDate, formatter);
        LocalDateTime localEndDate = LocalDateTime.parse(endDate, formatter);

        Users user = usersRepository.findById(userId).isPresent() ? usersRepository.findById(userId).get() : null;
        if (user!=null){
            Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(user, LocalDateTime.now());
            if (getUserToken.isPresent()) {
                if (getUserToken.get().getToken().equals(token)) {
                    return historyRepository.findAllByUserIdAndDateBetween(user.getId(), localStartDate, localEndDate);
                }
            }
        }
        return null;
    }

    public History getLastHistoryByUserIdAndAuctionProductId(Long userId, Long auctionId, String token) {
        Users user = usersRepository.findById(userId).isPresent() ? usersRepository.findById(userId).get() : null;
        if (user!=null){
            Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(user, LocalDateTime.now());
            if (getUserToken.isPresent()) {
                if (getUserToken.get().getToken().equals(token)) {
                    return historyRepository.findLastHistoryByAuctionIdAndUserId(user.getId(), auctionId);
                }
            }
            else{
                throw new RuntimeException("Token is expired / not valid");
            }
        }
        return null;
    }
}
