package com.morse_coders.aucdaisbackend.Auction_Products;

import com.morse_coders.aucdaisbackend.History.History;
import com.morse_coders.aucdaisbackend.History.HistoryRepository;
import com.morse_coders.aucdaisbackend.Users.Users;
import com.morse_coders.aucdaisbackend.Users.UsersRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
public class AuctionProductService {
    private final AuctionProductRepository auctionProductRepository;
    private final UsersRepository usersRepository;

    private final HistoryRepository historyRepository;
    @Autowired
    public AuctionProductService(AuctionProductRepository auctionProductRepository, UsersRepository usersRepository, HistoryRepository historyRepository) {
        this.auctionProductRepository = auctionProductRepository;
        this.usersRepository = usersRepository;
        this.historyRepository = historyRepository;
    }

    // Get all AuctionProducts
    public List<AuctionProducts> getAllAuctionProducts() {
        return auctionProductRepository.findAll();
    }

    // create an auction product
    public void createAuctionProduct(AuctionProducts auctionProduct){
        auctionProductRepository.save(auctionProduct);
    }


    // get all auction products of a specific user
    public List<AuctionProducts> getAllAuctionProductsOfUser(Long userId) {
        return auctionProductRepository.findAllByOwnerId(userId);
    }

    // delete an auction product by auction id
    public void deleteAuctionProduct(Long auctionId) {
        auctionProductRepository.deleteById(auctionId);
    }

    public AuctionProducts getAuctionProductById(long id) {
        if (auctionProductRepository.findById(id).isPresent()){
            return auctionProductRepository.findById(id).get();
        }
        return null;
    }

    public void updateAuctionProduct(AuctionProducts auctionProduct) {
        auctionProductRepository.save(auctionProduct);
    }

    public List<AuctionProducts> findAllByproduct_nameOrproduct_descriptionOrTags(String keyword) {
        return auctionProductRepository.findAllByproduct_nameOrproduct_descriptionOrTags(keyword);
    }

    public void update_max_bid(Long auction_id, Long max_bidder_id, double max_bid){
        AuctionProducts auctionProducts = auctionProductRepository.getById(auction_id);


        double current_bid  = auctionProducts.getMax_bid();
        if (max_bid > current_bid){
            Users user = usersRepository.getById(max_bidder_id);
            auctionProducts.setMax_bidder(user);
            auctionProducts.setMax_bid(max_bid);
            auctionProductRepository.save(auctionProducts);

            History history = new History(user, auctionProducts, LocalDateTime.now(), max_bid);
            historyRepository.save(history);


        }
        else{
            throw new IllegalStateException("Bid is too low");
        }



    }

    public List<AuctionProducts> getAllOnGoingAuctions(long user_id) {
        return auctionProductRepository.getAllOnGoingAuctions(user_id);
    }

    public List<AuctionProducts> getRandomAuctionProduct(Long user_id) {
        return auctionProductRepository.getRandomAuctionProduct(user_id);
    }

    public List<AuctionProducts> getRandomAuctionProduct() {
        return auctionProductRepository.getRandomAuctionProductNoUser();
    }

    public List<AuctionProducts> findAllByproduct_nameOrproduct_descriptionOrTagsGivenUser(String keyword, long user_id) {
        return auctionProductRepository.findAllByproduct_nameOrproduct_descriptionOrTagsGivenUser(keyword, user_id);
    }

    public void updateAddress(Long auctionId, Double latitude, Double longitude) {
        Optional<AuctionProducts> product = auctionProductRepository.findById(auctionId);
        if (product.isPresent()){
            AuctionProducts auctionProducts = product.get();
            auctionProducts.setLatitude(latitude);
            auctionProducts.setLongitude(longitude);
            auctionProductRepository.save(auctionProducts);

            String map_token = "pk.eyJ1IjoicHAwMDYzeCIsImEiOiJjazhiNmZiMnkwNWw0M2RzMjJub2xhMXYwIn0.OssYldnMWVzFiQr0o24_iw";
            String _url = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + longitude + "," + latitude + ".json?access_token=" + map_token;
            try {
                URL url = new URL(_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }
                StringBuilder output = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    output.append(scanner.nextLine());
                }
                scanner.close();

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(output.toString());
                JSONArray jsonArray = (JSONArray) jsonObject.get("features");

                JSONObject newOb = (JSONObject) parser.parse(jsonArray.get(0).toString());
                String address = newOb.get("place_name").toString();
                if (address!=null){
                    auctionProducts.setAddress(address);
                    auctionProductRepository.save(auctionProducts);
                }



            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid URL");
            } catch (IOException e) {
                throw new RuntimeException("Error connecting to URL");

            } catch (ParseException e) {
                throw new RuntimeException("Error parsing JSON");
            }
        }
        else{
            throw new IllegalStateException("Auction product not found");
        }
    }
}
