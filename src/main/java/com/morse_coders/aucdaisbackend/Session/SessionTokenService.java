package com.morse_coders.aucdaisbackend.Session;

import org.springframework.stereotype.Service;
import com.morse_coders.aucdaisbackend.Users.Users;

import java.util.Optional;

@Service
public class SessionTokenService {
    private final SessionTokenRepository sessionTokenRepository;

    public SessionTokenService(SessionTokenRepository sessionTokenRepository) {
        this.sessionTokenRepository = sessionTokenRepository;
    }

    public void saveSessionToken(SessionToken sessionToken) {
        this.sessionTokenRepository.save(sessionToken);
    }

    public Optional<SessionToken> getToken(Users user) {
        return sessionTokenRepository.findByUser(user);
    }

    public Optional<SessionToken> getToken(String token){
        return sessionTokenRepository.findByToken(token);
    }
}
