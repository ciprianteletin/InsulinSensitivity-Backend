package com.insulin.service;

import com.insulin.utils.ThreadManager;
import com.sun.mail.smtp.SMTPTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

import static com.insulin.shared.constants.EmailConstants.*;

/**
 * Service class used in order to send e-mails to the user, for example, when creating a new account.
 */
@Service
public class EmailService {
    private final ThreadManager threadManager = new ThreadManager();
    @Value("${email.username}")
    private String USERNAME;
    @Value("${email.password}")
    private String PASSWORD;

    public void sendRegisterEmail(String firstName, String email) {
        threadManager.runCallableVoid(() -> //
                sendMessage(email, String.format(REGISTER_TEXT_MESSAGE, firstName), EMAIL_SUBJECT));
    }

    public void sendDeleteEmail(String firstName, String email) {
        threadManager.runCallableVoid(() ->  //
                sendMessage(email, String.format(DELETE_TEXT_MESSAGE, firstName), DELETE_SUBJECT));
    }

    public void sendResetPasswordEmail(String email, String randomSecret) {
        threadManager.runCallableVoid(() ->
                sendMessage(email, String.format(RESET_PASSWORD_MESSAGE, randomSecret), RESET_SUBJECT));
    }

    public void sendContactEmail(String message) {
        threadManager.runCallableVoid(() -> //
                sendMessage(USERNAME, message, "Contact us"));
    }

    /**
     * Generic method to send an email to an user, no matter of the scope of the message.
     * It is derived from the need of sending multiple emails with different messages (Register, delete)
     */
    private Void sendMessage(String email, String textMessage, String subject) throws MessagingException {
        Message message = createMessage(email, textMessage, subject);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
        return null;
    }

    /**
     * Creates the message that is passed to the user.
     */
    private Message createMessage(String email, String textMessage, String subject) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(subject);
        message.setText(textMessage);
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    /**
     * Initialize the default properties required in order to send an e-mail.
     */
    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH, "true");
        properties.put(SMTP_PORT, DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE, "true");
        properties.put(SMTP_STARTTLS_REQUIRED, "true");
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }
}
