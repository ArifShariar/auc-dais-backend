package com.morse_coders.aucdaisbackend.SavedAuctions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedAuctionRepository extends JpaRepository<SavedAuctions, Long> {

        List<SavedAuctions> findAllByUserId(Long userId);

        SavedAuctions findSavedAuctionByUserIdAndAuctionProductId(Long userId, Long auctionProductId);


        // find auction product of a user before a specific date
        @Query(value = "SELECT * FROM saved_auctions WHERE user_id = ?1 AND auction_product_id IN (SELECT id FROM auction_products WHERE auction_end_date < ?2)", nativeQuery = true)
        List<SavedAuctions> findAllByUserIdAndAuctionProductIdBeforeDate(Long userId, String date);

        // find auction product of a user after a specific date
        @Query(value = "SELECT * FROM saved_auctions WHERE user_id = ?1 AND auction_product_id IN (SELECT id FROM auction_products WHERE auction_end_date > ?2)", nativeQuery = true)
        List<SavedAuctions> findAllByUserIdAndAuctionProductIdAfterDate(Long userId, String date);

        // delete auction by user id and auction product id
        @Modifying
        @Query(value = "DELETE FROM saved_auctions WHERE user_id = ?1 AND auction_product_id = ?2", nativeQuery = true)
        void deleteSavedAuctionByUserIdAndAuctionProductId(Long userId, Long auctionId);

        @Query(value = "SELECT * FROM saved_auctions WHERE user_id = ?1 AND auction_product_id = ?2", nativeQuery = true)
        SavedAuctions findByUserIdAndAuctionId(Long userId, Long auctionId);

        // get list of users who have saved an auction product
        @Query(value = "SELECT user_id FROM saved_auctions WHERE auction_product_id = ?1", nativeQuery = true)
        List<Long> getAllUsersWhoSavedAuctionProduct(Long auctionProductId);
}
