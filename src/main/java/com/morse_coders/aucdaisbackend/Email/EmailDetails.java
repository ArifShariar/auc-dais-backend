package com.morse_coders.aucdaisbackend.Email;


public class EmailDetails {
    private String receiver;
    private String subject;
    private String body;
    private String from;

    public EmailDetails() {
    }

    public EmailDetails(String receiver, String subject, String body, String from) {
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
        this.from = from;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
