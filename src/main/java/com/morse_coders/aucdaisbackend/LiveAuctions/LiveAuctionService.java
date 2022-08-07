package com.morse_coders.aucdaisbackend.LiveAuctions;

import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProductRepository;
import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LiveAuctionService {
    private final LiveAuctionRepository liveAuctionRepository;
    private final AuctionProductRepository auctionProductRepository;
    
    public LiveAuctionService(AuctionProductRepository auctionProductRepository, LiveAuctionRepository liveAuctionRepository) {
        this.liveAuctionRepository = liveAuctionRepository;
        this.auctionProductRepository = auctionProductRepository;
    }
    public List<LiveAuctions> getAllLiveAuctions() {
        return liveAuctionRepository.findAll();
    }


    public void createLiveAuctions(LocalDateTime today) {
        liveAuctionRepository.deleteAll();
        List<AuctionProducts> auctionProducts = auctionProductRepository.findAll();
        for(AuctionProducts prod: auctionProducts){
            System.out.println("date compare");
            System.out.println(prod.getAuction_start_date());
            System.out.println(prod.getAuction_end_date());
            System.out.println(prod.getAuction_start_date().compareTo(today));
            System.out.println(prod.getAuction_end_date().compareTo(today));
            System.out.println();

            if(prod.getAuction_start_date().compareTo(today)<=0 && prod.getAuction_end_date().compareTo(today)>=0){
                prod.setOnline(true);
                System.out.println("selected");
                LiveAuctions liveAuction = new LiveAuctions(prod);
                liveAuctionRepository.save(liveAuction);
            }
        }
    } 
}
