package com.morse_coders.aucdaisbackend.Message;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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


    @GetMapping("get/sender/{senderId}/receiver/{receiverId}/sorted")
    public List<Message> getMessageSorted(@PathVariable("senderId") Long senderId, @PathVariable("receiverId") Long receiverId) {
        return messageService.getMessageSorted(senderId, receiverId);
    }

    @GetMapping("get/all/user/{userId}")
    public List<Message> getAllMessagesByUserId(@PathVariable("userId") Long userId) {
        return messageService.findAllMessageSentOrReceivedSorted(userId);
    }

    @Transactional
    @Modifying
    @PutMapping("mark_all_read/sender/{senderId}/receiver/{receiverId}")
    public void markAllRead(@PathVariable("senderId") Long senderId, @PathVariable("receiverId") Long receiverId) {
        messageService.markAllRead(senderId, receiverId);
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
