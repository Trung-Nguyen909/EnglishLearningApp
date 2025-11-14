package com.example.EnglishLearningApp.Controller;

import org.springframework.stereotype.Component;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

@Component
public class GmailSender {

    public static void sendEmail(String toEmail, String subject, String body) {
        final String fromEmail = "chientranminh355@gmail.com"; // Gmail của bạn
        final String password = "iwmr uhjw rine vfxu"; // App Password (không phải mật khẩu chính)

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP server
        props.put("mail.smtp.port", "587"); // TLS Port
        props.put("mail.smtp.auth", "true"); // Enable authentication
        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
