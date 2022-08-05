package com.morse_coders.aucdaisbackend.LiveAuctions;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Date;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/liveAuctions")
public class LiveAuctionController {

    private final LiveAuctionService liveAuctionService;
    public LiveAuctionController(LiveAuctionService liveAuctionService){
        this.liveAuctionService = liveAuctionService;
    }
    @GetMapping("get/all")
    public List<LiveAuctions> getAllLiveAuctions() {
        System.out.println("today is "+ LocalDateTime.now());
        liveAuctionService.createLiveAuctions(LocalDateTime.now());
        List<LiveAuctions> ret = liveAuctionService.getAllLiveAuctions();
        System.out.println("total live auctions "+ret.size());
        return ret;
    }
    
}
