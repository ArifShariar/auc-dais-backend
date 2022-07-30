package com.morse_coders.aucdaisbackend.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findUsersByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Users u SET u.isConfirmed = TRUE WHERE u.email = ?1")
    int updateIsConfirmed(String email);
}
