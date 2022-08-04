package com.morse_coders.aucdaisbackend.Session;

import com.morse_coders.aucdaisbackend.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface SessionTokenRepository extends JpaRepository<SessionToken, Long>  {
    Optional<SessionToken> findByToken(String token);

    @Transactional
    @Query("FROM SessionToken WHERE user = ?1")
    Optional<SessionToken> findByUser(Users user);

    @Query("FROM SessionToken WHERE user = ?1 AND expiresAt > ?2")
    Optional<SessionToken> findByUserAndExpiresAt(Users user, LocalDateTime now);
}
