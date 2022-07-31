package com.morse_coders.aucdaisbackend.Message;


import com.morse_coders.aucdaisbackend.Email.EmailDetails;
import com.morse_coders.aucdaisbackend.Email.EmailSender;
import com.morse_coders.aucdaisbackend.Users.Users;
import com.morse_coders.aucdaisbackend.Users.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UsersRepository userRepository;

    private final EmailSender emailSender;

    public MessageService(MessageRepository messageRepository, UsersRepository userRepository, EmailSender emailSender) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
    }

    public List<Message> getMessage(Long senderId, Long receiverId) {
        return messageRepository.findAllMessageBySenderIdAndReceiverId(senderId, receiverId);
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

        EmailDetails emailDetails = new EmailDetails();

        emailDetails.setReceiver(receiver.get().getEmail());
        emailDetails.setFrom(sender.get().getEmail());
        emailDetails.setSubject(sender.get().getFirstName() + " has sent you a message");
        emailDetails.setBody(message.getMessage());
        emailSender.send(emailDetails);


        return messageRepository.save(message);
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}
