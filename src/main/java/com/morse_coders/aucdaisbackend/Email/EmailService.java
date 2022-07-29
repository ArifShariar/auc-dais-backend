package com.morse_coders.aucdaisbackend.Email;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;


@Service
public class EmailService implements EmailSender{
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public String send(EmailDetails emailDetails) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailDetails.getReceiver());
            message.setSubject(emailDetails.getSubject());
            message.setText(emailDetails.getBody());
            message.setFrom(emailDetails.getFrom());
            System.out.println("trying to send email");
            javaMailSender.send(message);
            return "Email sent successfully";
        } catch (Exception e) {
            return "Error sending email";
        }

    }
}
