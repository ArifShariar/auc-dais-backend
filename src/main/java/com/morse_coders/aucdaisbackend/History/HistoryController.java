package com.morse_coders.aucdaisbackend.History;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/history")
public class HistoryController {
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("get/all")
    public List<History> getAllHistory() {
        return historyService.getAllHistory();
    }

    @GetMapping("get/user/{userId}")
    public List<History> getHistoryByUserId(@PathVariable("userId") Long userId, @RequestBody String token) {
        return historyService.getAllHistoryByUserId(userId, token);
    }

    @GetMapping("get/user/{userId}/auction/{auctionId}")
    public List<History> getHistoryByUserIdAndAuctionId(@PathVariable("userId") Long userId, @PathVariable("auctionId") Long auctionId, @RequestBody String token) {
        return historyService.getAllHistoryByUserIdAndAuctionProductId(userId, auctionId, token);
    }

    @GetMapping("get/user/{userId}/before/{date}")
    public List<History> getHistoryByUserIdAndAuctionIdBeforeDate(@PathVariable("userId") Long userId, @PathVariable("date") String date, @RequestBody String token) {
        return historyService.getAllHistoryByUserIdAndAuctionProductIdBeforeDate(userId, date, token);
    }

    @GetMapping("get/user/{userId}/after/{date}")
    public List<History> getHistoryByUserIdAndAuctionIdAfterDate(@PathVariable("userId") Long userId, @PathVariable("date") String date, @RequestBody String token) {
        return historyService.getAllHistoryByUserIdAndAuctionProductIdAfterDate(userId, date, token);
    }

    @GetMapping("get/user/{userId}/between/{startDate}/{endDate}")
    public List<History> getHistoryByUserIdAndAuctionIdBetweenDates(@PathVariable("userId") Long userId, @PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, @RequestBody String token) {
        return historyService.getHistoryByUserIdAndAuctionIdBetweenDates(userId, startDate, endDate, token);
    }


    @PostMapping("/create/user/{userId}/auction/{auctionId}/bid/{bid}")
    public void createHistory(@PathVariable("userId") Long userId, @PathVariable("auctionId") Long auctionId, @PathVariable("bid") Double bid, @RequestBody String token) {
        historyService.createHistory(bid, userId, auctionId, token);
    }

}
