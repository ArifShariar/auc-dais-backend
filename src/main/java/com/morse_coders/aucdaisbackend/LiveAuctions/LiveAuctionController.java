package com.morse_coders.aucdaisbackend.LiveAuctions;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Date;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/liveAuctions")
public class LiveAuctionController {

    private final LiveAuctionService liveAuctionService;
    public LiveAuctionController(LiveAuctionService liveAuctionServce){
        this.liveAuctionService = liveAuctionServce;
    }
    @GetMapping("get/all")
    public List<LiveAuctions> getAllLiveAuctions() {
        Date today = new Date();
        System.out.println("today is "+today);
        liveAuctionService.createLiveAuctions(today);
        List<LiveAuctions> ret = liveAuctionService.getAllLiveAuctions();
        System.out.println("total live auctions "+ret.size());
        return ret;
    }
    
}
