package com.morse_coders.aucdaisbackend.ChatRoom;


import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProductRepository;
import com.morse_coders.aucdaisbackend.Auction_Products.AuctionProducts;
import com.morse_coders.aucdaisbackend.Session.SessionToken;
import com.morse_coders.aucdaisbackend.Session.SessionTokenRepository;
import com.morse_coders.aucdaisbackend.Users.Users;
import com.morse_coders.aucdaisbackend.Users.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    private final UsersRepository usersRepository;

    private final AuctionProductRepository auctionProductRepository;

    private final SessionTokenRepository sessionTokenRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, UsersRepository usersRepository, AuctionProductRepository auctionProductRepository, SessionTokenRepository sessionTokenRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.usersRepository = usersRepository;
        this.auctionProductRepository = auctionProductRepository;
        this.sessionTokenRepository = sessionTokenRepository;
    }

    public List<ChatRoom> getAllChatsOfAnAuction(long auctionId) {

        return chatRoomRepository.getAllByAuctionId(auctionId);
    }

    public void sendMessageInChat(long auctionId, long userId, String token, ChatRoom chatRoom) {
        Users user = usersRepository.findById(userId).isPresent() ? usersRepository.findById(userId).get() : null;
        AuctionProducts auctionProduct = auctionProductRepository.findById(auctionId).isPresent() ? auctionProductRepository.findById(auctionId).get() : null;
        if (user!=null && auctionProduct!=null){
            Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(user, LocalDateTime.now());
            if (getUserToken.isPresent()) {
                if (getUserToken.get().getToken().equals(token)) {
                    chatRoom.setSender(user);
                    chatRoom.setAuction(auctionProduct);
                    chatRoom.setMessage(chatRoom.getMessage());
                    chatRoomRepository.save(chatRoom);
                }
            }
            else{
                throw new RuntimeException("Token is expired / not valid");
            }
        }
        else{
            throw new RuntimeException("User or AuctionProduct not found");
        }
    }
}
