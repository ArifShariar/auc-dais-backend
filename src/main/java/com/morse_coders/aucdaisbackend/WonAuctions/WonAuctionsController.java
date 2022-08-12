package com.morse_coders.aucdaisbackend.WonAuctions;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/won_auctions")
public class WonAuctionsController {

    private final WonAuctionsService wonAuctionsService;

    public WonAuctionsController(WonAuctionsService wonAuctionsService) {
        this.wonAuctionsService = wonAuctionsService;
    }

    @GetMapping(value = "/all/{user_id}")
    public List<WonAuctions> getAllWonAuctionByUserId(@PathVariable String user_id) {
        return wonAuctionsService.getAllWonAuctionByUserId(Long.parseLong(user_id));
    }

    @GetMapping(value = "/all/not_paid/{user_id}")
    public List<WonAuctions> getAllWonButNotPaidAuctions(@PathVariable String user_id) {
        return wonAuctionsService.getAllWonButNotPaidAuctions(Long.parseLong(user_id));
    }

    @GetMapping(value = "/all/paid/{user_id}")
    public List<WonAuctions> getAllWonAndPaidAuctions(@PathVariable String user_id) {
        return wonAuctionsService.getAllWonAndPaidAuctions(Long.parseLong(user_id));
    }

    @PutMapping(value = "/update/{won_auction_id}")
    public void updateWonAuction(@PathVariable String won_auction_id, @RequestBody WonAuctions wonAuctions) {
        wonAuctionsService.updateWonAuction(Long.parseLong(won_auction_id), wonAuctions);
    }
}
