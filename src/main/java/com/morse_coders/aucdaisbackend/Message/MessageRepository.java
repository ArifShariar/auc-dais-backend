package com.morse_coders.aucdaisbackend.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllBySenderId(Long senderId);

    List<Message> findAllByReceiverId(Long receiverId);

    @Query(value = "SELECT * FROM message WHERE sender_id = ?1 AND receiver_id = ?2", nativeQuery = true)
    List<Message> findAllMessageBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
