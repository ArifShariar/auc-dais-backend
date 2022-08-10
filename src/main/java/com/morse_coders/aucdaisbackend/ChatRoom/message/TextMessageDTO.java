package com.morse_coders.aucdaisbackend.ChatRoom.message;

import com.morse_coders.aucdaisbackend.ChatRoom.chatroom.Chatroom;
import com.morse_coders.aucdaisbackend.Users.Users;

import javax.persistence.*;

@Entity
@Table(name = "text_message_dto")
public class TextMessageDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGenerator")
    @SequenceGenerator(name = "SequenceGenerator", sequenceName = "text_message_dto_seq", allocationSize = 1)
    private Long id;

    private String type; // either "PRIVATE" or "GROUP"

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Users sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Users receiver;

    private String message;

    public TextMessageDTO() {}

    public TextMessageDTO(Long id, String type, Chatroom chatroom, Users sender, Users receiver, String message) {
        this.id = id;
        this.type = type;
        this.chatroom = chatroom;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public TextMessageDTO(String type, Chatroom chatroom, Users sender, Users receiver, String message) {
        this.type = type;
        this.chatroom = chatroom;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Chatroom getChatroom() {
        return chatroom;
    }

    public void setChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
    }

    public Users getSender() {
        return sender;
    }

    public void setSender(Users sender) {
        this.sender = sender;
    }

    public Users getReceiver() {
        return receiver;
    }

    public void setReceiver(Users receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        String printable = "Sender: " + sender.getFirstName() + " " + sender.getLastName() + "\n";
        printable += "Receiver: " + receiver.getFirstName() + " " + receiver.getLastName() + "\n";
        printable += "Message: " + message + "\n";
        return printable;
    }
}