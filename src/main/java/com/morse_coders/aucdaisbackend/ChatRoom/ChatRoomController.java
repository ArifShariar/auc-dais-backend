package com.morse_coders.aucdaisbackend.ChatRoom;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/chatroom")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @GetMapping("get/all/{auction_id}")
    public List<ChatRoom> getAllChatsOfAnAuction(@PathVariable("auction_id") String auction_id) {
        return chatRoomService.getAllChatsOfAnAuction(Long.parseLong(auction_id));
    }

    @PostMapping("/send/{auction_id}/{user_id}/{token}")
    public void sendMessageInChat(@PathVariable("auction_id") String auction_id, @PathVariable("user_id") String user_id, @PathVariable("token") String token, @RequestBody ChatRoom chatRoom) {
        chatRoomService.sendMessageInChat(Long.parseLong(auction_id), Long.parseLong(user_id), token, chatRoom);
    }

}
