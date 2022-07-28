package com.morse_coders.aucdaisbackend.History;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAllByUserId(Long userId);

    List<History> findHistoryByUserIdAndAuctionProductId(Long userId, Long auctionProductId);


    List<History> findAllByUserIdAndDateBefore(Long user_id, Date date);

    List<History> findAllByUserIdAndDateAfter(Long user_id, Date date);

    List<History> findAllByUserIdAndDateBetween(Long user_id, Date date1, Date date2);

    @Query(value = "SELECT * FROM history WHERE user_id = ?1 AND auction_product_id = ?2", nativeQuery = true)
    History findByUserIdAndAuctionId(Long userId, Long auctionId);




}
