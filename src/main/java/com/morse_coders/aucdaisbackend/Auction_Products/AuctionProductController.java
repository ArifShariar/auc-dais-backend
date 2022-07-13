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

    /*
    * Create an auction product
    * @return void
     */
    @PostMapping("/create")

    public void createAuctionProduct(@RequestParam String owner_id, @RequestParam(required = false) String max_bidder_id, @RequestParam String product_name, @RequestParam Boolean isOnline,
                                     @RequestParam String product_description, @RequestParam(required = false) String tags, @RequestParam Double minimum_price,
                                     @RequestParam(required = false) Double max_bid, @RequestParam(required = false) String photos, @RequestParam String auction_start_date,
                                     @RequestParam String auction_end_date, @RequestParam(required = false) String address) throws ParseException {
        Boolean isApproved = false;

        Date auction_start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(auction_start_date);
        Date auction_end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(auction_end_date);

        Users owner = usersService.getUserById(Long.parseLong(owner_id));

        AuctionProducts auctionProduct = new AuctionProducts();
        auctionProduct.setOwner(owner);
        auctionProduct.setProduct_name(product_name);
        auctionProduct.setProduct_description(product_description);

        if(max_bidder_id!=null){
            Users max_bidder = usersService.getUserById(Long.parseLong(max_bidder_id));
            auctionProduct.setMax_bidder(max_bidder);
        }

        if(tags!=null){
            auctionProduct.setTags(tags);
        }
        auctionProduct.setMinimum_price(minimum_price);
        if(max_bid!=null){
            auctionProduct.setMax_bid(max_bid);
        }
        if(photos!=null){
            auctionProduct.setPhotos(photos);
        }
        auctionProduct.setAuction_start_date(auction_start);
        auctionProduct.setAuction_end_date(auction_end);
        if(address!=null){
            auctionProduct.setAddress(address);
        }

        auctionProduct.setOnline(isOnline);

        auctionProduct.setApproved(isApproved);
        auctionProductService.createAuctionProduct(auctionProduct);



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
