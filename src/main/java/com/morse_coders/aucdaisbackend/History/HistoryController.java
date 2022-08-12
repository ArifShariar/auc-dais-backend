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

    @GetMapping("get/user/{userId}/{token}")
    public List<History> getHistoryByUserId(@PathVariable("userId") Long userId, @PathVariable("token") String token) {
        return historyService.getAllHistoryByUserId(userId, token);
    }

    @GetMapping("get/user/{userId}/auction/{auctionId}/{token}")
    public List<History> getHistoryByUserIdAndAuctionId(@PathVariable("userId") Long userId, @PathVariable("auctionId") Long auctionId, @PathVariable("token") String token) {
        return historyService.getAllHistoryByUserIdAndAuctionProductId(userId, auctionId, token);
    }

    @GetMapping("get/user/{userId}/before/{date}/{token}")
    public List<History> getHistoryByUserIdAndAuctionIdBeforeDate(@PathVariable("userId") Long userId, @PathVariable("date") String date, @PathVariable("token") String token) {
        return historyService.getAllHistoryByUserIdAndAuctionProductIdBeforeDate(userId, date, token);
    }

    @GetMapping("get/user/{userId}/after/{date}/{token}")
    public List<History> getHistoryByUserIdAndAuctionIdAfterDate(@PathVariable("userId") Long userId, @PathVariable("date") String date, @PathVariable("token") String token) {
        return historyService.getAllHistoryByUserIdAndAuctionProductIdAfterDate(userId, date, token);
    }

    @GetMapping("get/user/{userId}/between/{startDate}/{endDate}/{token}")
    public List<History> getHistoryByUserIdAndAuctionIdBetweenDates(@PathVariable("userId") Long userId, @PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, @PathVariable("token") String token) {
        return historyService.getHistoryByUserIdAndAuctionIdBetweenDates(userId, startDate, endDate, token);
    }

    @GetMapping("get/last/user/{userId}/auction/{auctionId}/{token}")
    public History getLastHistoryByUserIdAndAuctionId(@PathVariable("userId") Long userId, @PathVariable("auctionId") Long auctionId, @PathVariable("token") String token) {
        return historyService.getLastHistoryByUserIdAndAuctionProductId(userId, auctionId, token);
    }


    @PostMapping("/create/user/{userId}/auction/{auctionId}/bid/{bid}/{token}")
    public void createHistory(@PathVariable("userId") Long userId, @PathVariable("auctionId") Long auctionId, @PathVariable("bid") Double bid, @PathVariable("token") String token) {
        historyService.createHistory(bid, userId, auctionId, token);
    }

}
