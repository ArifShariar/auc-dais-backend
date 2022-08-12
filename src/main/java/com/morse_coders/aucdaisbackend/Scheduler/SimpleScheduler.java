package com.morse_coders.aucdaisbackend.Scheduler;


import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProductRepository;
import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Email.EmailDetails;
import com.morse_coders.aucdaisbackend.Email.EmailSender;
import com.morse_coders.aucdaisbackend.SavedAuctions.SavedAuctionRepository;
import com.morse_coders.aucdaisbackend.Users.Users;
import com.morse_coders.aucdaisbackend.Users.UsersRepository;
import com.morse_coders.aucdaisbackend.WonAuctions.WonAuctions;
import com.morse_coders.aucdaisbackend.WonAuctions.WonAuctionsRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SimpleScheduler {

    private final SavedAuctionRepository savedAuctionRepository;

    private final AuctionProductRepository auctionProductRepository;
    private final UsersRepository usersRepository;

    private final WonAuctionsRepository wonAuctionsRepository;
    private final EmailSender emailSender;

    public SimpleScheduler(SavedAuctionRepository savedAuctionRepository, AuctionProductRepository auctionProductRepository, UsersRepository usersRepository, WonAuctionsRepository wonAuctionsRepository, EmailSender emailSender) {
        this.savedAuctionRepository = savedAuctionRepository;
        this.auctionProductRepository = auctionProductRepository;
        this.usersRepository = usersRepository;
        this.wonAuctionsRepository = wonAuctionsRepository;
        this.emailSender = emailSender;
    }

    @Scheduled(fixedRate = 1000*60)
    public void endAuctions() {
        List<AuctionProducts> auctionProducts = auctionProductRepository.getAllOngoingButNotSoldAuctions();
        for (AuctionProducts product : auctionProducts){
            // check if the auction end date is before the current date
            if(product.getAuction_end_date().isBefore(LocalDateTime.now())){
                if (product.getMax_bidder()!=null){
                    product.setSold(true);
                    auctionProductRepository.save(product);

                    // send email to the max bidder that the auction has ended
                    Users user = product.getMax_bidder();

                    // create a won auction object
                    WonAuctions wonAuctions = new WonAuctions();
                    wonAuctions.setUser(user);
                    wonAuctions.setAuctionProduct(product);
                    wonAuctions.setPaid(false);
                    wonAuctions.setBid(product.getMax_bid());
                    wonAuctions.setPayment_date(null);
                    wonAuctions.setPaymentMethod(null);
                    wonAuctionsRepository.save(wonAuctions);

                    EmailDetails emailDetails = new EmailDetails();
                    emailDetails.setFrom("morse@coders.com");
                    emailDetails.setReceiver(user.getEmail());
                    emailDetails.setSubject("Auction "+ product.getProduct_name() + " has ended");
                    emailDetails.setBody("The auction "+ product.getProduct_name() + " has ended. You have won the auction with the highest bid.");
                    emailSender.send(emailDetails);
                }
                else if (!product.getSentFailEmail()){
                    product.setSentFailEmail(true);
                    auctionProductRepository.save(product);
                    Users user = product.getOwner();
                    EmailDetails emailDetails = new EmailDetails();
                    emailDetails.setFrom("morse@coders.com");
                    emailDetails.setReceiver(user.getEmail());
                    emailDetails.setSubject("Auction "+ product.getProduct_name() + " has ended without success");
                    emailDetails.setBody("The auction "+ product.getProduct_name() + " has ended without success. No one has bid on the auction.");
                    emailSender.send(emailDetails);
                }

            }
        }
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
