package com.morse_coders.aucdaisbackend.SavedAuctions;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/savedAuctions")
public class SavedAuctionController {
    private final SavedAuctionService savedAuctionService;

    public SavedAuctionController(SavedAuctionService savedAuctionService) {
        this.savedAuctionService = savedAuctionService;
    }

    // development purposes only
    @GetMapping("get/all")
    public List<SavedAuctions> getAllSavedAuctions() {
        return savedAuctionService.getAllSavedAuctions();
    }

    @GetMapping("get/user/{userId}/{token}")
    public List<SavedAuctions> getSavedAuctionsByUserId(@PathVariable("userId") Long userId, @PathVariable("token") String token) {
        return savedAuctionService.getAllSavedAuctionsOfUser(userId, token);
    }

    // get all instance of savedAuctions of a specific auction
    @GetMapping({"get/auction/{auctionId}"})
    public SavedAuctions getSavedAuctionByAuctionId(@PathVariable("auctionId") Long auctionId) {
        return savedAuctionService.getSavedAuctionById(auctionId);
    }

    @GetMapping("get/user/{userId}/auction/{auctionId}/")
    public SavedAuctions getSavedAuctionByUserIdAndAuctionId(@PathVariable("userId") Long userId, @PathVariable("auctionId") Long auctionId) {
        return savedAuctionService.getSavedAuctionByUserIdAndAuctionId(userId, auctionId);
    }

    @PostMapping("/create/user/{userId}/auction/{auctionId}/{token}")
    public void createSavedAuction(@RequestBody SavedAuctions savedAuction, @PathVariable("userId") String userId, @PathVariable("auctionId") String auctionId, @PathVariable("token") String token) {
        savedAuctionService.createSavedAuction(savedAuction, Long.parseLong(userId), Long.parseLong(auctionId), token);
    }

    // for testing purpose
    @DeleteMapping("/delete/auction/{id}")
    public void deleteSavedAuction(@PathVariable("id") Long id) {
        savedAuctionService.deleteSavedAuction(id);
    }

    @Transactional
    @Modifying
    @DeleteMapping("/delete/user/{userId}/auction/{auctionId}/{token}")
    public void deleteSavedAuctionByUserIdAndAuctionId(@PathVariable("userId") Long userId, @PathVariable("auctionId") Long auctionId, @PathVariable("token") String token) {
        savedAuctionService.deleteSavedAuctionByUserIdAndAuctionId(userId, auctionId, token);
    }

    // get all saved auctions before a date
    @GetMapping("get/user/{userId}/before/{date}/{token}")
    public List<SavedAuctions> getSavedAuctionsBeforeDate(@PathVariable("userId") Long userId, @PathVariable("date") String date, @PathVariable("token") String token) {
        return savedAuctionService.getAllSavedAuctionsByUserIdAndAuctionIdBeforeDate(userId, date, token);
    }

    // get all saved auctions after a date
    @GetMapping("get/user/{userId}/after/{date}/{token}")
    public List<SavedAuctions> getSavedAuctionsAfterDate(@PathVariable("userId") Long userId, @PathVariable("date") String date, @PathVariable("token") String token) {
        return savedAuctionService.getAllSavedAuctionsByUserIdAndAuctionIdAfterDate(userId, date, token);
    }
}
