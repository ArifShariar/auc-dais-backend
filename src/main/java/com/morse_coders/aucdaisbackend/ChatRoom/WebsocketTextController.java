package com.morse_coders.aucdaisbackend.ChatRoom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketTextController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate; //used to send private messages

    @MessageMapping("/message")     //If user sends message: app/message/
    @SendTo("/chatroom/public")     // User listens to:
    public TextMessageDTO receiveMessage(@Payload TextMessageDTO message) {
        return message;
    }

    @MessageMapping("/private-message")
    public TextMessageDTO recMessage(@Payload TextMessageDTO message){
        // If user wants to listen to private messages: user/username/private
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        System.out.println(message.toString());
        return message;
    }
}

