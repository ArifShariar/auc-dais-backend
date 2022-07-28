package com.morse_coders.aucdaisbackend.History;

import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProductRepository;
import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Users.Users;
import com.morse_coders.aucdaisbackend.Users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    private final UsersRepository usersRepository;

    private final AuctionProductRepository auctionProductRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository, UsersRepository usersRepository, AuctionProductRepository auctionProductRepository) {
        this.historyRepository = historyRepository;
        this.usersRepository = usersRepository;
        this.auctionProductRepository = auctionProductRepository;
    }

    public List<History> getAllHistory() {
        return historyRepository.findAll();
    }

    public List<History> getAllHistoryByUserId(Long userId) {
        return historyRepository.findAllByUserId(userId);
    }

    public List<History> getAllHistoryByUserIdAndAuctionProductId(Long userId, Long auctionProductId) {
        return historyRepository.findHistoryByUserIdAndAuctionProductId(userId, auctionProductId);
    }

    public List<History> getAllHistoryByUserIdAndAuctionProductIdBeforeDate(Long userId, String date) {
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            return historyRepository.findAllByUserIdAndDateBefore(userId, date1);
        } catch (ParseException e) {
            throw new RuntimeException("Can not format date");
        }


    }

    public List<History> getAllHistoryByUserIdAndAuctionProductIdAfterDate(Long userId, String date) {
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            return historyRepository.findAllByUserIdAndDateAfter(userId, date1);
        } catch (ParseException e) {
            throw new RuntimeException("Can not format date");
        }
    }

    public void createHistory(Double bid, Long userId, Long auctionProductId) {
        Optional<Users> user = usersRepository.findById(userId);
        Optional<AuctionProducts> auctionProduct = auctionProductRepository.findById(auctionProductId);

        if (user.isPresent() && auctionProduct.isPresent()) {
            History history = new History();
            history.setBid_amount(bid);
            history.setUser(user.get());
            history.setAuctionProduct(auctionProduct.get());
            history.setDate(new Date());
            historyRepository.save(history);
        }
        else{
            System.out.println("User or AuctionProduct not found");
        }
    }


    public List<History> getHistoryByUserIdAndAuctionIdBetweenDates(Long userId, String startDate, String endDate) {
        try {
            Date startDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            Date endDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            return historyRepository.findAllByUserIdAndDateBetween(userId, startDate1, endDate1);
        } catch (ParseException e) {
            throw new RuntimeException("Can not format date");
        }

    }
}
