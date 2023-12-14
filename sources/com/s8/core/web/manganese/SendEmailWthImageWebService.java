package com.s8.core.web.manganese;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * This class is used to send email with image.
 * @author w3spoint
 */
public class SendEmailWthImageWebService {


	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		//sendEmailTask();

		
		new SendEmailWthImage ("convert.pierre@gmail.com", "Test Email",
				"Hi, This is a test email with image.", "image.png");
		 
	}



	public SendEmailWthImageWebService(String receiverEmail,
			String subject, String messageText, String imagePath) {
		
		Properties props = new Properties();
		props.put("mail.smtp.host", Credentials.HOST);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.starttls.enable", "true"); //TLS
		

		try {

			Session session = Session.getInstance(props, new Authenticator() {
				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(Credentials.USERNAME, Credentials.PASSWORD);
				}
			});
			Message emailMessage = new MimeMessage(session);

			//emailMessage.setFrom(new InternetAddress(Credentials.USERNAME));
			emailMessage.setFrom(new InternetAddress(Credentials.USERNAME, "WebService master"));

			emailMessage.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(receiverEmail));
			emailMessage.setSubject(subject);
			MimeBodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setText(messageText);
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();
			DataSource source = new FileDataSource(imagePath);
			messageBodyPart2.setDataHandler(new DataHandler(source));
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);
			multipart.addBodyPart(messageBodyPart2);
			emailMessage.setContent(multipart);

			Transport.send(emailMessage);

			System.out.println("Email send successfully.");
		} catch (Exception e) {
			System.err.println("Error in sending email.");
		}
	}


	public static void sendEmailTask(){
		try {

			// Setup mail server
			Properties props = new Properties();
			props.put("mail.smtp.host", Credentials.HOST);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.starttls.enable", "true"); //TLS


			// Get the Session object.// and pass
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected @Override PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(Credentials.USERNAME, Credentials.PASSWORD);
				}
			});


			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(Credentials.USERNAME));

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