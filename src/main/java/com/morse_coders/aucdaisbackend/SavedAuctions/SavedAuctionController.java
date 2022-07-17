package com.morse_coders.aucdaisbackend.SavedAuctions;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/savedAuctions")
public class SavedAuctionController {
    private final SavedAuctionService savedAuctionService;

    public SavedAuctionController(SavedAuctionService savedAuctionService) {
        this.savedAuctionService = savedAuctionService;
    }

    @GetMapping("get/all")
    public List<SavedAuctions> getAllSavedAuctions() {
        return savedAuctionService.getAllSavedAuctions();
    }

    @GetMapping("get/user/{userId}")
    public List<SavedAuctions> getSavedAuctionsByUserId(@PathVariable("userId") Long userId) {
        return savedAuctionService.getAllSavedAuctionsOfUser(userId);
    }

    @GetMapping({"get/auction/{auctionId}"})
    public SavedAuctions getSavedAuctionByAuctionId(@PathVariable("auctionId") Long auctionId) {
        return savedAuctionService.getSavedAuctionById(auctionId);
    }

    @GetMapping("get/user/{userId}/auction/{auctionId}")
    public SavedAuctions getSavedAuctionByUserIdAndAuctionId(@PathVariable("userId") Long userId, @PathVariable("auctionId") Long auctionId) {
        return savedAuctionService.getSavedAuctionByUserIdAndAuctionId(userId, auctionId);
    }

    @PostMapping("/create/user/{userId}/auction/{auctionId}")
    public void createSavedAuction(@RequestBody SavedAuctions savedAuction, @PathVariable String userId, @PathVariable String auctionId) {
        savedAuctionService.createSavedAuction(savedAuction, Long.parseLong(userId), Long.parseLong(auctionId));
    }

    @DeleteMapping("/delete/auction/{id}")
    public void deleteSavedAuction(@PathVariable("id") Long id) {
        savedAuctionService.deleteSavedAuction(id);
    }

    @DeleteMapping("/delete/user/{userId}/auction/{auctionId}")
    public void deleteSavedAuctionByUserIdAndAuctionId(@PathVariable("userId") Long userId, @PathVariable("auctionId") Long auctionId) {
        savedAuctionService.deleteSavedAuctionByUserIdAndAuctionId(userId, auctionId);
    }

    // get all saved auctions before a date
    @GetMapping("get/user/{userId}/before/{date}")
    public List<SavedAuctions> getSavedAuctionsBeforeDate(@PathVariable("userId") Long userId, @PathVariable("date") String date) {
        return savedAuctionService.getAllSavedAuctionsByUserIdAndAuctionIdBeforeDate(userId, date);
    }

    // get all saved auctions after a date
    @GetMapping("get/user/{userId}/after/{date}")
    public List<SavedAuctions> getSavedAuctionsAfterDate(@PathVariable("userId") Long userId, @PathVariable("date") String date) {
        return savedAuctionService.getAllSavedAuctionsByUserIdAndAuctionIdAfterDate(userId, date);
    }
}
