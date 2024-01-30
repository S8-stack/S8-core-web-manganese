package com.s8.core.web.manganese;

import java.util.Properties;

import com.s8.core.web.manganese.generator.CSS_ClassBase;
import com.s8.core.web.manganese.generator.MgMailGenerator;
import com.s8.core.web.manganese.javax.mail.Authenticator;
import com.s8.core.web.manganese.javax.mail.Message;
import com.s8.core.web.manganese.javax.mail.PasswordAuthentication;
import com.s8.core.web.manganese.javax.mail.Session;
import com.s8.core.web.manganese.javax.mail.Transport;
import com.s8.core.web.manganese.javax.mail.internet.InternetAddress;
import com.s8.core.web.manganese.javax.mail.internet.MimeMessage;


/**
 * This class is used to send email with image.
 * @author w3spoint
 */
public class SendEmail_HTML {


	public static void main(String[] args) {
		//sendEmailTask();

		MailServerCredentials credentials = NoReplyCredentials.create();
		
		
		
		sendEmailTask(credentials);
		 
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

			
			CSS_ClassBase base = new CSS_ClassBase();
			base.parseFile("/Users/pc/qx/git/S8-core-web-manganese/web-sources/S8-core-web-manganese/mg-mail-style.css");
			
			
			MgMailGenerator mailGenerator = new MgMailGenerator(base, ".mg-mail-wrapper");
			mailGenerator.logoBanner(
					".mg-mail-logo-wrapper", 
					".mg-mail-logo", 
					"https://alphaventor.com/assets/logos/AlphaventorLogo-1024px-black-text.png");
			
			mailGenerator.h1(".mg-h1", "Hello dear AlphaVentor user!");
			mailGenerator.h2(".mg-h2", "Welcome to a world of designs");
			mailGenerator.p(".mg-p", "Your code to validate the creation of your account is just below:");
			mailGenerator.addCode(".mg-code-wrapper", "trpoekpokpok");
			mailGenerator.p(".mg-p", "If you're not the initiator of this, please report to pierre.convert@alphaventor.com");
			
			String content = mailGenerator.generate();
			
			message.setContent(content, "text/html");


			// Send message
			Transport.send(message);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}