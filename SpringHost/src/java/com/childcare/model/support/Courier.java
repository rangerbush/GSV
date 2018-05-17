/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.childcare.model.support;

import com.sun.mail.smtp.SMTPTransport;
import com.sun.mail.util.BASE64EncoderStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * This class is responsible for sending mail to certain mail address
 * @author New User
 */
public class Courier {
    
    
    public  static void sendMail() throws  RuntimeException
    {
 Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.starttls.enable", "true");
		//props.put("mail.smtp.socketFactory.port", "465");
		//props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
                System.out.println("Courier:  Creating Session."); 
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
                                        System.out.println("Courier:  Authenticating."); 
					return new PasswordAuthentication("zaixiawuming@gmail.com","nazgul784508");
				}
			});
                System.out.println("Courier:  Setting Debug mode."); 
                session.setDebug(true);
		try {
                        System.out.println("Courier:  Debug Session begins."); 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("zaixiawuming@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("zaixiawuming@gmail.com"));
			message.setSubject("Testing Subject");
			message.setText("War, war never end." +
					"\n\n No spam to my email, please!");
                        System.out.println("Courier:  Message Set. Starting to Send.");
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

    //this method is used
        public  static void sendMail(String text) throws  RuntimeException
    {
                Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.126.com");
                props.put("mail.smtp.starttls.enable", "true");
		//props.put("mail.smtp.socketFactory.port", "465");
		//props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		//props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
                                        System.out.println("Courier:  Authenticating."); 
					return new PasswordAuthentication("zaixiawuming_90@126.com","qweqwe789");
				}
			});
                session.setDebug(true);
		try {         
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("zaixiawuming_90@126.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("andrewsymons@gmail.com"));
			message.setSubject("Device Activity Report" );
			message.setText(text);
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
        
    public  static void resetPassword(String text,String receiver) throws  MessagingException, UnsupportedEncodingException
    {
    
                Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.126.com");
                props.put("mail.smtp.starttls.enable", "true");
		//props.put("mail.smtp.socketFactory.port", "465");
		//props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		//props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
                                        System.out.println("Courier:  Authenticating."); 
					return new PasswordAuthentication("zaixiawuming_90@126.com","qweqwe789"); //126邮箱授权码
				}
			});
                session.setDebug(true);      
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("zaixiawuming_90@126.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(receiver));
			message.setSubject("GSV: Reset your password" );
			message.setText(text);
			Transport.send(message);

        /*
        
            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getDefaultInstance(props);
            session.setDebug(true);

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("zaixiawuming@gmail.com", "gsv_password_reset"));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            msg.setSubject("Reseting your GSV password");
            msg.setContent(text, "text/html");

            SMTPTransport transport = new SMTPTransport(session, null);
            transport.connect("smtp.gmail.com", "zaixiawuming@gmail.com", null);
            transport.issueCommand("AUTH XOAUTH2 " + new String(BASE64EncoderStream.encode(String.format("user=%s\1auth=Bearer %s\1\1", "zaixiawuming@gmail.com", smtpUserAccessToken).getBytes())), 235);
            transport.sendMessage(msg, msg.getAllRecipients());
*/
    }
        
        

}
