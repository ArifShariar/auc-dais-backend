package com.morse_coders.aucdaisbackend.Message;


import com.morse_coders.aucdaisbackend.Users.Users;
import com.morse_coders.aucdaisbackend.Users.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UsersRepository userRepository;

    public MessageService(MessageRepository messageRepository, UsersRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public Message getMessage(Long senderId, Long receiverId) {
        return messageRepository.findMessageBySenderIdAndReceiverId(senderId, receiverId);
    }

    public List<Message> getMessages(Long userId) {
        return messageRepository.findAllBySenderId(userId);
    }

    public List<Message> getMessagesReceived(Long userId) {
        return messageRepository.findAllByReceiverId(userId);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }


    public Message sendMessage(Message message, Long senderId, Long receiverId) {
        Optional<Users> sender = userRepository.findById(senderId);
        Optional<Users> receiver = userRepository.findById(receiverId);
        sender.ifPresent(message::setSender);
        receiver.ifPresent(message::setReceiver);
        return messageRepository.save(message);
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}
