package com.morse_coders.aucdaisbackend.WonAuctions;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WonAuctionsRepository extends JpaRepository<WonAuctions, Long> {

    @Query(value = "SELECT * FROM won_auctions WHERE user_id = ?1", nativeQuery = true)
    List<WonAuctions> getAllWonAuctionByUserId(Long user_id);

    @Query(value = "SELECT * FROM won_auctions WHERE user_id = ?1 AND is_paid = false", nativeQuery = true)
    List<WonAuctions> getAllWonButNotPaidAuctions(Long user_id);

    @Query(value = "SELECT * FROM won_auctions WHERE user_id = ?1 AND is_paid = true", nativeQuery = true)
    List<WonAuctions> getAllWonAndPaidAuctions(Long user_id);
}
