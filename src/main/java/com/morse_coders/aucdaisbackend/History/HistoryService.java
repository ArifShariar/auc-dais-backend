package com.morse_coders.aucdaisbackend.History;

import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProductRepository;
import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Session.SessionToken;
import com.morse_coders.aucdaisbackend.Session.SessionTokenRepository;
import com.morse_coders.aucdaisbackend.Token.ConfirmationToken;
import com.morse_coders.aucdaisbackend.Token.ConfirmationTokenRepository;
import com.morse_coders.aucdaisbackend.Users.Users;
import com.morse_coders.aucdaisbackend.Users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
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
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            Users user = usersRepository.findById(userId).isPresent() ? usersRepository.findById(userId).get() : null;
            if (user!=null){
                Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(user, LocalDateTime.now());
                if (getUserToken.isPresent()) {
                    if (getUserToken.get().getToken().equals(token)) {
                        return historyRepository.findAllByUserIdAndDateBefore(user.getId(), date1);
                    }
                }
                else{
                    throw new RuntimeException("Token is expired / not valid");
                }
            }
            return null;
        } catch (ParseException e) {
            throw new RuntimeException("Can not format date");
        }


    }

    public List<History> getAllHistoryByUserIdAndAuctionProductIdAfterDate(Long userId, String date, String token) {
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            Users user = usersRepository.findById(userId).isPresent() ? usersRepository.findById(userId).get() : null;
            if (user!=null){
                Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(user, LocalDateTime.now());
                if (getUserToken.isPresent()) {
                    if (getUserToken.get().getToken().equals(token)) {
                        return historyRepository.findAllByUserIdAndDateAfter(user.getId(), date1);
                    }
                }
                else{
                    throw new RuntimeException("Token is expired / not valid");
                }
            }
            return null;
        } catch (ParseException e) {
            throw new RuntimeException("Can not format date");
        }
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
        try {
            Date startDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            Date endDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            Users user = usersRepository.findById(userId).isPresent() ? usersRepository.findById(userId).get() : null;
            if (user!=null){
                Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(user, LocalDateTime.now());
                if (getUserToken.isPresent()) {
                    if (getUserToken.get().getToken().equals(token)) {
                        return historyRepository.findAllByUserIdAndDateBetween(user.getId(), startDate1, endDate1);
                    }
                }
            }
            return null;
        } catch (ParseException e) {
            throw new RuntimeException("Can not format date");
        }

    }
}
