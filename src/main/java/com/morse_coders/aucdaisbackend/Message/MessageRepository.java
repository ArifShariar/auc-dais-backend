package com.morse_coders.aucdaisbackend.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllBySenderId(Long senderId);

    List<Message> findAllByReceiverId(Long receiverId);

    @Query(value = "SELECT * FROM message WHERE sender_id = ?1 AND receiver_id = ?2", nativeQuery = true)
    List<Message> findAllMessageBySenderIdAndReceiverId(Long senderId, Long receiverId);

    // SORT BY DATE
    @Query(value = "SELECT * FROM message WHERE (sender_id = ?1 AND receiver_id = ?2) OR (sender_id = ?2 AND receiver_id = ?1) ORDER BY date", nativeQuery = true)
    List<Message> findAllMessageBySenderIdOrReceiverIdSorted(Long senderId, Long receiverId);

    // set all received or sent messages by a user sorted by date
    @Query(value = "SELECT * FROM message WHERE sender_id = ?1 OR receiver_id = ?1 ORDER BY date desc", nativeQuery = true)
    List<Message> findAllMessageSentOrReceivedSorted(Long senderId);

    // set message as read
    @Modifying
    @Query(value = "UPDATE message SET is_read = true WHERE (sender_id = ?1 AND receiver_id = ?2) OR (sender_id = ?2 AND receiver_id = ?1)", nativeQuery = true)
    void markAllRead(Long senderId, Long receiverId);
}
