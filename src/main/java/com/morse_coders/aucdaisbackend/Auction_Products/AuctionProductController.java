package com.morse_coders.aucdaisbackend.Auction_Products;

import com.morse_coders.aucdaisbackend.Users.Users;
import com.morse_coders.aucdaisbackend.Users.UsersService;

import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auction_products")
public class AuctionProductController {
    private final AuctionProductService auctionProductService;
    private final UsersService usersService;

    public AuctionProductController(AuctionProductService auctionProductService, UsersService usersService) {
        this.auctionProductService = auctionProductService;
        this.usersService = usersService;
    }

    /*
    * Get all AuctionProducts
    * @return List<AuctionProducts>
    * */
    @GetMapping("/all")
    public List<AuctionProducts> getAllAuctionProducts() {
        return auctionProductService.getAllAuctionProducts();
    }

    /*
    * Get all AuctionProducts of a specific user
    * @return List<AuctionProducts>
     */
    @GetMapping("/all/{userId}")
    public List<AuctionProducts> getAllAuctionProductsOfUser(@PathVariable String userId) {
        List<AuctionProducts> result = auctionProductService.getAllAuctionProductsOfUser(Long.parseLong(userId));
        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    @GetMapping("/auction/{auctionId}")
    public AuctionProducts getAuctionProductByAuctionId(@PathVariable String auctionId) {
        return auctionProductService.getAuctionProductById(Long.parseLong(auctionId));
    }

    @GetMapping("/search/{keyword}")
    public List<AuctionProducts> findAllByproduct_nameOrproduct_descriptionOrTags(@PathVariable String keyword) {
        return auctionProductService.findAllByproduct_nameOrproduct_descriptionOrTags(keyword);
    }

    /*
    * Create an auction product
    * @return void
     */
    @PostMapping("/create")
    public void createAuctionProduct(@RequestParam String ownerId, @RequestParam String product_name, @RequestParam String product_description,
                                     @RequestParam String tags, @RequestParam String auction_start_date, @RequestParam String auction_end_date,
                                     @RequestParam String minimum_price, @RequestParam String photos, @RequestParam String address) {

        // find user by ownerId
        Users user = usersService.getUserById(Long.parseLong(ownerId));
        if (user == null) {
            throw new IllegalStateException("User with id " + ownerId + " does not exist");
        }
        // create auction product
        AuctionProducts auctionProduct = new AuctionProducts();
        auctionProduct.setOwner(user);
        auctionProduct.setProduct_name(product_name);
        auctionProduct.setProduct_description(product_description);
        auctionProduct.setTags(tags);
        auctionProduct.setMinimum_price(Double.parseDouble(minimum_price));
        auctionProduct.setPhotos(photos);
        auctionProduct.setAddress(address);
        auctionProduct.setOnline(false);
        auctionProduct.setApproved(false);
        auctionProduct.setOngoing(false);
        auctionProduct.setSold(false);

        // convert auction_start_date and auction_end_date to Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate;
        Date endDate;
        try{
            startDate = sdf.parse(auction_start_date);
        }
        catch (ParseException e) {
            throw new RuntimeException("Invalid date format");
        }
        try{
            endDate = sdf.parse(auction_end_date);
        }
        catch (ParseException e) {
            throw new RuntimeException("Invalid date format");
        }
        auctionProduct.setAuction_start_date(startDate);
        auctionProduct.setAuction_end_date(endDate);

        auctionProductService.createAuctionProduct(auctionProduct);




    }

    @PutMapping("/update/max_bid/{auction_id}")
    public void updateMaxBid(@PathVariable String auction_id, @RequestParam String user_id, @RequestParam String max_bid){
        Long id_auction = Long.parseLong(auction_id);
        Long id_user = Long.parseLong(user_id);
        double bid_max = Double.parseDouble(max_bid);
        auctionProductService.update_max_bid(id_auction, id_user, bid_max);

    }
    @DeleteMapping("/delete/{id}")
    public void deleteAuctionProduct(@PathVariable String id) {
        auctionProductService.deleteAuctionProduct(Long.parseLong(id));
    }

    @PutMapping("/update/{id}")
    public void updateAuctionProduct(@PathVariable String id, @RequestParam(required = false) String product_name, @RequestParam(required = false) Boolean isOnline,
                                     @RequestParam (required = false) String product_description, @RequestParam(required = false) String tags, @RequestParam(required = false) Double minimum_price,
                                     @RequestParam(required = false) Double max_bid, @RequestParam(required = false) String photos, @RequestParam(required = false) String auction_start_date,
                                     @RequestParam(required = false) String auction_end_date, @RequestParam(required = false) String address) throws ParseException {

        AuctionProducts auctionProduct = auctionProductService.getAuctionProductById(Long.parseLong(id));

        if(auctionProduct!=null){


            if (product_name!=null){
                auctionProduct.setProduct_name(product_name);
            }
            if (isOnline!=null){
                auctionProduct.setOnline(isOnline);
            }
            if (product_description!=null){
                auctionProduct.setProduct_description(product_description);
            }
            if (tags!=null){
                auctionProduct.setTags(tags);
            }
            if (minimum_price!=null){
                auctionProduct.setMinimum_price(minimum_price);
            }
            if (max_bid!=null){
                auctionProduct.setMax_bid(max_bid);
            }
            if (photos!=null){
                auctionProduct.setPhotos(photos);
            }

            // check if new starting date is previous of today's date
            // if yes, throw error
            if(auction_start_date!=null){
                Date auction_start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(auction_start_date);

                if (auction_start.before(new Date())){
                    throw new IllegalArgumentException("Auction start date cannot be previous of today's date");
                } else {
                    auctionProduct.setAuction_start_date(auction_start);
                }

            }


            // check if new ending date is before of today's date
            // if yes, throw an error
            if (auction_end_date!=null){
                Date auction_end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(auction_end_date);
                if (auction_end.before(new Date())){
                    throw new IllegalArgumentException("Auction end date cannot be before today's date");
                } else {
                    auctionProduct.setAuction_end_date(auction_end);
                }
            }

            if (address!=null){
                auctionProduct.setAddress(address);
            }

            auctionProductService.updateAuctionProduct(auctionProduct);

        }
        else{
            throw new IllegalArgumentException("Auction product with id " + id + " does not exist");
        }

    }

}
