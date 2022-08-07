package com.morse_coders.aucdaisbackend.LiveAuctions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LiveAuctionRepository extends JpaRepository<LiveAuctions, Long>{ 

    List<LiveAuctions> findAllByUserId(Long userId);
    List<LiveAuctions> findAll();
}
