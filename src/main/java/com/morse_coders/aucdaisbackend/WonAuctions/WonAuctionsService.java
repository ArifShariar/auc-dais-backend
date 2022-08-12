package com.morse_coders.aucdaisbackend.WonAuctions;


import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProductRepository;
import com.morse_coders.aucdaisbackend.Users.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
            wonAuctions1.setPaid(wonAuctions.getPaid());
            wonAuctions1.setPayment_date(wonAuctions.getPayment_date());
            wonAuctions1.setPaymentMethod(wonAuctions.getPaymentMethod());
            wonAuctionsRepository.save(wonAuctions1);
        }
    }
}
