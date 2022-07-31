package com.morse_coders.aucdaisbackend.Message;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("get/all")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("get/sender/{senderId}")
    public List<Message> getMessagesBySenderId(@PathVariable("senderId") Long senderId) {
        return messageService.getMessages(senderId);
    }

    @GetMapping("get/receiver/{receiverId}")
    public List<Message> getMessagesByReceiverId(@PathVariable("receiverId") Long receiverId) {
        return messageService.getMessagesReceived(receiverId);
    }

    @GetMapping("get/sender/{senderId}/receiver/{receiverId}")
    public List<Message> getMessage(@PathVariable("senderId") Long senderId, @PathVariable("receiverId") Long receiverId) {
        return messageService.getMessage(senderId, receiverId);
    }


    @PostMapping("send/sender/{senderId}/receiver/{receiverId}")
    public Message sendMessage(@RequestBody Message message, @PathVariable("senderId") Long senderId, @PathVariable("receiverId") Long receiverId) {
        return messageService.sendMessage(message, senderId, receiverId);
    }

    @DeleteMapping("delete/{id}")
    public void deleteMessage(@PathVariable("id") Long id) {
        messageService.deleteMessage(id);
    }
}
