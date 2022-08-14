package com.morse_coders.aucdaisbackend.Message;


import com.morse_coders.aucdaisbackend.Email.EmailDetails;
import com.morse_coders.aucdaisbackend.Email.EmailSender;
import com.morse_coders.aucdaisbackend.Users.Users;
import com.morse_coders.aucdaisbackend.Users.UsersRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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

    public List<Message> getMessageSorted(Long senderId, Long receiverId) {
        return messageRepository.findAllMessageBySenderIdOrReceiverIdSorted(senderId, receiverId);
    }

    public List<Message> findAllMessageSentOrReceivedSorted(Long senderId){
        List<Message> messages = messageRepository.findAllMessageSentOrReceivedSorted(senderId);
        List<Message> curatedList = new ArrayList<>();

        if (messages.size() > 0) {
            System.out.println("It is different message: ");
            System.out.println(0 + " " + messages.get(0).getMessage() + "\n\n");
            for(int i=0;i<messages.size();i++){
                if(curatedList.isEmpty()){
                    curatedList.add(messages.get(i));
                }
                else{
                    boolean alreadyin = false;
                    for(int j=0;j<curatedList.size();j++) {

                        if ((curatedList.get(j).getReceiver().getId().equals(messages.get(i).getReceiver().getId()) &&
                                curatedList.get(j).getSender().getId().equals(messages.get(i).getSender().getId()))


                                || (curatedList.get(j).getReceiver().getId().equals(messages.get(i).getSender().getId()) &&
                                curatedList.get(j).getSender().getId().equals(messages.get(i).getReceiver().getId()))) {
                            alreadyin = true;
                            break;
                        }
                    }
                    if (!alreadyin) {
                        curatedList.add(messages.get(i));
                    }
                }
            }
            return curatedList;
        }
        else{
            return null;
        }

    }

    @Transactional
    @Modifying
    public void markAllRead(Long senderId, Long receiverId) {
        messageRepository.markAllRead(senderId, receiverId);
    }
}
