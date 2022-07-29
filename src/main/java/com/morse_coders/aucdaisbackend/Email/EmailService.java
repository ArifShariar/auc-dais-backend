package com.morse_coders.aucdaisbackend.Email;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;


@Service
public class EmailService implements EmailSender{
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public String send(EmailDetails emailDetails) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,"utf-8");
            helper.setTo(emailDetails.getReceiver());
            helper.setSubject(emailDetails.getSubject());
            helper.setText(emailDetails.getBody(), true);
            helper.setFrom(emailDetails.getFrom());
            javaMailSender.send(message);
            return "Email sent successfully";
        } catch (Exception e) {
            return "Error sending email";
        }

    }
}
