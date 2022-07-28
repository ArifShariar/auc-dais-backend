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
    public List<History> getHistoryByUserId(@PathVariable("userId") Long userId) {
        return historyService.getAllHistoryByUserId(userId);
    }

    @GetMapping("get/user/{userId}/auction/{auctionId}")
    public List<History> getHistoryByUserIdAndAuctionId(@PathVariable("userId") Long userId, @PathVariable("auctionId") Long auctionId) {
        return historyService.getAllHistoryByUserIdAndAuctionProductId(userId, auctionId);
    }

    @GetMapping("get/user/{userId}/before/{date}")
    public List<History> getHistoryByUserIdAndAuctionIdBeforeDate(@PathVariable("userId") Long userId, @PathVariable("date") String date) {
        return historyService.getAllHistoryByUserIdAndAuctionProductIdBeforeDate(userId, date);
    }

    @GetMapping("get/user/{userId}/after/{date}")
    public List<History> getHistoryByUserIdAndAuctionIdAfterDate(@PathVariable("userId") Long userId, @PathVariable("date") String date) {
        return historyService.getAllHistoryByUserIdAndAuctionProductIdAfterDate(userId, date);
    }

    @GetMapping("get/user/{userId}/between/{startDate}/{endDate}")
    public List<History> getHistoryByUserIdAndAuctionIdBetweenDates(@PathVariable("userId") Long userId, @PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate) {
        return historyService.getHistoryByUserIdAndAuctionIdBetweenDates(userId, startDate, endDate);
    }


    @PostMapping("/create/user/{userId}/auction/{auctionId}/bid/{bid}")
    public void createHistory(@PathVariable("userId") Long userId, @PathVariable("auctionId") Long auctionId, @PathVariable("bid") Double bid) {
        historyService.createHistory(bid, userId, auctionId);
    }

}
