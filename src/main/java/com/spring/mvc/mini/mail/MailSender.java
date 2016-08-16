package com.spring.mvc.mini.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class MailSender {

    private static final Logger LOG = LoggerFactory.getLogger(MailSender.class);

    @Value("${mail.starttls.enable}")
    private String starttlsEnable;

    @Value("${mail.auth}")
    private String auth;

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private String port;

    public void sendMail(final String username,final String password, String fromAddress, Address[] toAddress, String subject, String text) throws Exception {

            Authenticator au =  new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
              };
              
            Message message = new MimeMessage(Session.getInstance(getProperties(), au));
            message.setFrom(new InternetAddress(fromAddress));
            message.setSubject(subject);
            message.setText(text);
            message.setRecipients(Message.RecipientType.TO, toAddress);
            Transport.send(message);
            
            LOG.info("Send Mail Done: " + fromAddress + " to" + toAddress.toString());

    }

    private java.util.Properties getProperties() {
        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        return props;
    }

}
