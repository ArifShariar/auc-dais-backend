package com.morse_coders.aucdaisbackend.WonAuctions;


import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProductRepository;
import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Users.Users;
import com.morse_coders.aucdaisbackend.Users.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WonAuctionsService {
    private final UsersRepository usersRepository;
    private final AuctionProductRepository auctionProductRepository;
    private final WonAuctionsRepository wonAuctionsRepository;

    public WonAuctionsService(UsersRepository usersRepository, AuctionProductRepository auctionProductRepository, WonAuctionsRepository wonAuctionsRepository) {
        this.usersRepository = usersRepository;
        this.auctionProductRepository = auctionProductRepository;
        this.wonAuctionsRepository = wonAuctionsRepository;
    }

    public List<WonAuctions> getAllWonAuctionByUserId(Long user_id) {
        return wonAuctionsRepository.getAllWonAuctionByUserId(user_id);
    }

    public List<WonAuctions> getAllWonButNotPaidAuctions(Long user_id) {
        return wonAuctionsRepository.getAllWonButNotPaidAuctions(user_id);
    }

    public List<WonAuctions> getAllWonAndPaidAuctions(Long user_id) {
        return wonAuctionsRepository.getAllWonAndPaidAuctions(user_id);
    }

    public void updateWonAuction(long parseLong, WonAuctions wonAuctions) {
        if (wonAuctionsRepository.findById(parseLong).isPresent()) {
            // find the auction product by id
            WonAuctions wonAuctions1 = wonAuctionsRepository.findById(parseLong).get();
            // update the auction product
            wonAuctions1.setPaid(true);
            wonAuctions1.setPayment_date(LocalDateTime.now());
            wonAuctions1.setPaymentMethod(wonAuctions.getPaymentMethod());
            wonAuctionsRepository.save(wonAuctions1);
        }
    }

    public void addPayment(long userId, long auctionId, WonAuctions wonAuction) {
        Optional<Users> user = usersRepository.findById(userId);
        Optional<AuctionProducts> auctionProduct = auctionProductRepository.findById(auctionId);
        WonAuctions wonAuctions = wonAuctionsRepository.getWonAuctionByUserIdAndAuctionId(userId, auctionId);
        if (wonAuctions != null) {
            wonAuctions.setPaid(true);
            wonAuctions.setPayment_date(LocalDateTime.now());
            wonAuctions.setPaymentMethod(wonAuction.getPaymentMethod());
            wonAuctionsRepository.save(wonAuction);
        }
        else{
            System.out.println("No auction found");
        }
    }

    public WonAuctions getByAuctionId(long wonId) {
        Optional<WonAuctions> wonAuctions =  wonAuctionsRepository.findById(wonId);
        return wonAuctions.orElse(null);
    }
}
