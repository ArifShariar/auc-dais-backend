package com.morse_coders.aucdaisbackend.Scheduler;


import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProductRepository;
import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Email.EmailDetails;
import com.morse_coders.aucdaisbackend.Email.EmailSender;
import com.morse_coders.aucdaisbackend.SavedAuctions.SavedAuctionRepository;
import com.morse_coders.aucdaisbackend.Users.Users;
import com.morse_coders.aucdaisbackend.Users.UsersRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SimpleScheduler {

    private final SavedAuctionRepository savedAuctionRepository;

    private final AuctionProductRepository auctionProductRepository;
    private final UsersRepository usersRepository;

    private final EmailSender emailSender;

    public SimpleScheduler(SavedAuctionRepository savedAuctionRepository, AuctionProductRepository auctionProductRepository, UsersRepository usersRepository, EmailSender emailSender) {
        this.savedAuctionRepository = savedAuctionRepository;
        this.auctionProductRepository = auctionProductRepository;
        this.usersRepository = usersRepository;
        this.emailSender = emailSender;
    }

    @Scheduled(fixedRate = 1000*60)
    public void sendEmailOnSavedAuctions() {
        System.out.println("Sending email: " + LocalDateTime.now());
    }

    @Scheduled(fixedRate = 1000*60)
    public void setAuctionToLive(){
        List<AuctionProducts> auctionProducts = auctionProductRepository.getAllNotOngoingAuctions();
        for(AuctionProducts auctionProduct : auctionProducts){
            if(auctionProduct.getAuction_start_date().compareTo(LocalDateTime.now()) < 0){
                auctionProduct.setOngoing(true);
                auctionProductRepository.save(auctionProduct);
                // send email to all users that saved this product
                List<Long> userList = savedAuctionRepository.getAllUsersWhoSavedAuctionProduct(auctionProduct.getId());


                for(Long userId : userList){
                    Users user = usersRepository.findById(userId).get();

                    EmailDetails emailDetails = new EmailDetails();

                    String auctionURL = "http://localhost:3000/auction/" + auctionProduct.getId();

                    emailDetails.setReceiver(user.getEmail());
                    emailDetails.setFrom("morse@coders.com");
                    emailDetails.setSubject("Auction "+ auctionProduct.getProduct_name() +" is live");
                    emailDetails.setBody("Auction "+ auctionProduct.getProduct_name() +" is live. You can now bid on this product." +
                            "You can visit our website to bid on this product. " +
                            "Or you can directly visit this link: " + auctionURL);
                    emailSender.send(emailDetails);

                }
            }
        }

    }
}
