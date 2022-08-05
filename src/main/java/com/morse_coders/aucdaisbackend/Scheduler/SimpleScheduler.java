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
    public void endAuctions() {
        List<AuctionProducts> auctionProducts = auctionProductRepository.getAllOngoingButNotSoldAuctions();

        for (AuctionProducts products: auctionProducts){
            if(products.getAuction_end_date().compareTo(LocalDateTime.now())<0){
                products.setOngoing(false);

                if (products.getMax_bid() > products.getMinimum_price() && products.getMax_bidder()!=null){
                    Users max_bidder = products.getMax_bidder();
                    products.setOngoing(false);
                    products.setSold(true);
                    auctionProductRepository.save(products);
                    EmailDetails emailDetails = new EmailDetails();
                    emailDetails.setReceiver(max_bidder.getEmail());
                    emailDetails.setFrom("morse@coders.com");
                    emailDetails.setSubject("Auction "+products.getId() + "::" + products.getProduct_name() +" has ended");
                    emailDetails.setBody("Dear "+max_bidder.getFirstName()+",\n\n" +
                            "Congratulations! You have won the auction "+products.getId() + "::" + products.getProduct_name() +"\n\n" +
                            "The winning bid was "+products.getMax_bid()+"\n\n" +
                            "You can contact the seller at "+products.getOwner().getEmail()+"\n\n" +
                            "Thank you for using AucDais");
                    emailSender.send(emailDetails);
                }
                else{
                    // send the owner an email telling that the auction was not successful
                    products.setOngoing(false);
                    products.setSold(false);
                    auctionProductRepository.save(products);
                    EmailDetails emailDetails = new EmailDetails();
                    emailDetails.setReceiver(products.getOwner().getEmail());
                    emailDetails.setFrom("morse@coders.com");
                    emailDetails.setSubject("Auction "+products.getId() + "::" + products.getProduct_name() +" has ended");
                    emailDetails.setBody("Dear "+products.getOwner().getFirstName()+",\n\n" +
                            "The auction "+products.getId() + "::" + products.getProduct_name() +" has ended without success because there were no bidders.\n\n" +
                            "You can change the starting date and host the auction again.\n\n" +
                            "Thank you for using AucDais");
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
