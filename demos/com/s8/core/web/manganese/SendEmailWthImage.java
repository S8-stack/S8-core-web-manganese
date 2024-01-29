package com.s8.core.web.manganese;

import java.util.Properties;

import com.s8.core.web.manganese.javax.activation.DataContentHandler;
import com.s8.core.web.manganese.javax.activation.DataHandler;
import com.s8.core.web.manganese.javax.activation.DataSource;
import com.s8.core.web.manganese.javax.activation.FileDataSource;
import com.s8.core.web.manganese.javax.mail.Authenticator;
import com.s8.core.web.manganese.javax.mail.Message;
import com.s8.core.web.manganese.javax.mail.Multipart;
import com.s8.core.web.manganese.javax.mail.PasswordAuthentication;
import com.s8.core.web.manganese.javax.mail.Session;
import com.s8.core.web.manganese.javax.mail.Transport;
import com.s8.core.web.manganese.javax.mail.handlers.MailDataContentHandler;
import com.s8.core.web.manganese.javax.mail.internet.InternetAddress;
import com.s8.core.web.manganese.javax.mail.internet.MimeBodyPart;
import com.s8.core.web.manganese.javax.mail.internet.MimeMessage;
import com.s8.core.web.manganese.javax.mail.internet.MimeMultipart;


/**
 * This class is used to send email with image.
 * @author w3spoint
 */
public class SendEmailWthImage {


	public static void main(String[] args) {
		//sendEmailTask();

		MailServerCredentials credentials = NoReplyCredentials.create();
		
		
		sendEmailWthImage(credentials, "convert.pierre@gmail.com", "Test Email",
				"Hi, This is a test email with image.", "/Users/pc/qx/git/S8-core-web-manganese/image.png");
				
		
		//sendEmailTask(credentials);
		 
	}



	
	public static void sendEmailWthImage(MailServerCredentials credentials,
			String receiverEmail,
			String subject, String messageText, String imagePath) {
		
		Properties props = new Properties();
		props.put("mail.smtp.host", credentials.host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.starttls.enable", "true"); //TLS
		

		try {

			Session session = Session.getInstance(props, new Authenticator() {
				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(credentials.username, credentials.password);
				}
			});
			Message emailMessage = new MimeMessage(session);

			//emailMessage.setFrom(new InternetAddress(Credentials.USERNAME));
			emailMessage.setFrom(new InternetAddress(credentials.username, "WebService master"));

			emailMessage.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(receiverEmail));
			emailMessage.setSubject(subject);
			MimeBodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setText(messageText);
			
			
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();
			DataSource source = new FileDataSource(imagePath);
			DataContentHandler dch = MailDataContentHandler.create("image/jpeg");
			messageBodyPart2.setDataHandler(new DataHandler(dch, source));

			messageBodyPart2.setHeader("Content-ID", "logo1.png");      
			//messageBodyPart2.setHeader("name", "logo1.png");      
// add image to the multipart
			messageBodyPart2.setDisposition(MimeBodyPart.INLINE);
			
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);
			multipart.addBodyPart(messageBodyPart2);
			emailMessage.setContent(multipart);

			Transport.send(emailMessage);

			System.out.println("Email send successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error in sending email.");
		}
	}


	public static void sendEmailTask(MailServerCredentials credentials){
		try {

			// Setup mail server
			Properties props = new Properties();
			props.put("mail.smtp.host", credentials.host);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.starttls.enable", "true"); //TLS


			// Get the Session object.// and pass
			Session session = Session.getInstance(props, new Authenticator() {
				protected @Override PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(credentials.username, credentials.password);
				}
			});


			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(credentials.username));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("convert.pierre@gmail.com"));

			// Set Cc: header field of the header.
			/*
	        for(String ccEmail : mail.getCcEmails()){
	            message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccEmail));
	        }
			 */

			// Set Subject: header field
			message.setSubject("Test mail subject");

			message.setText("This is a text with a lot of words...");


			// Send message
			Transport.send(message);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}