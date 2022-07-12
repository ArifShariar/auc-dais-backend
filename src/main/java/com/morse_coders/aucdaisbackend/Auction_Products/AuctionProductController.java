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

    public void createAuctionProduct(@RequestParam String owner_id, @RequestParam(required = false) String max_bidder_id, @RequestParam String product_name,
                                     @RequestParam String product_description, @RequestParam(required = false) String tags, @RequestParam Double minimum_price,
                                     @RequestParam(required = false) Double max_bid, @RequestParam(required = false) String photos, @RequestParam String auction_start_date,
                                     @RequestParam String auction_end_date, @RequestParam(required = false) String address) throws ParseException {
        System.out.println("owner_id: " + owner_id);
        System.out.println("max_bidder_id: " + max_bidder_id);
        System.out.println("product_name: " + product_name);
        System.out.println("product_description: " + product_description);
        System.out.println("tags: " + tags);
        System.out.println("minimum_price: " + minimum_price);
        System.out.println("max_bid: " + max_bid);
        System.out.println("photos: " + photos);
        System.out.println("auction_start_date: " + auction_start_date);
        System.out.println("auction_end_date: " + auction_end_date);
        System.out.println("address: " + address);
        Boolean isApproved = false;

        Date auction_start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(auction_start_date);
        Date auction_end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(auction_end_date);

        Users owner = usersService.getUserById(Long.parseLong(owner_id));
        if (max_bidder_id != null) {
            Users max_bidder = usersService.getUserById(Long.parseLong(max_bidder_id));
            auctionProductService.createAuctionProduct(new AuctionProducts(owner, max_bidder, product_name, product_description, tags, minimum_price, max_bid, photos, auction_start, auction_end, address, isApproved));
        } else {
            auctionProductService.createAuctionProduct(new AuctionProducts(owner, product_name, product_description, tags, minimum_price, max_bid, photos, auction_start, auction_end, address, isApproved));
        }


    }

}
