package com.morse_coders.aucdaisbackend.Auction_Products;

import com.morse_coders.aucdaisbackend.Users.Users;
import com.morse_coders.aucdaisbackend.Users.UsersService;
import org.springframework.web.bind.annotation.*;

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
    @ResponseBody
    public void createAuctionProduct(@RequestParam(required = false) String owner_id, @RequestParam(required = false) String max_bidder_id, @RequestParam(required = false) String product_name,
                                     @RequestParam(required = false) String product_description, @RequestParam(required = false) String tags, @RequestParam(required = false) Double minimum_price,
                                     @RequestParam(required = false) Double max_bid, @RequestParam(required = false) String photos, @RequestParam(required = false) Date auction_start_date,
                                     @RequestParam(required = false) Date auction_end_date, @RequestParam(required = false) String address, @RequestParam(required = false) Boolean isApproved) {
        System.out.println("owner_id: " + owner_id);
//        Users owner = usersService.getUserById(Long.parseLong(owner_id));
//        Users max_bidder = usersService.getUserById(Long.parseLong(max_bidder_id));
//        auctionProductService.createAuctionProduct(new AuctionProducts(owner, max_bidder, product_name, product_description, tags, minimum_price, max_bid, photos, auction_start_date, auction_end_date, address, isApproved));


    }
}
