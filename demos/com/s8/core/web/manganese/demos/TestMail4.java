package com.s8.core.web.manganese.demos;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import com.s8.core.web.manganese.activation.FileDataSource;
import com.s8.core.web.manganese.critical.Credentials;
import com.s8.core.web.manganese.mail.BodyPart;
import com.s8.core.web.manganese.mail.Message;
import com.s8.core.web.manganese.mail.MessagingException;
import com.s8.core.web.manganese.mail.Multipart;
import com.s8.core.web.manganese.mail.handlers.DataHandler;
import com.s8.core.web.manganese.mail.handlers.DataSource;
import com.s8.core.web.manganese.mail.internet.InternetAddress;
import com.s8.core.web.manganese.mail.internet.MimeBodyPart;
import com.s8.core.web.manganese.mail.internet.MimeMessage;
import com.s8.core.web.manganese.mail.internet.MimeMultipart;
import com.s8.core.web.manganese.mail.smtp.SMTPTransport;
import com.s8.core.web.manganese.mail.smtp.SMTP_ConnectionParams;
import com.s8.core.web.manganese.mail.smtp.SMTP_TransportProps;

public class TestMail4 {
	
	/**
	   Outgoing Mail (SMTP) Server
	   requires TLS or SSL: smtp.gmail.com (use authentication)
	   Use Authentication: Yes
	   Port for TLS/STARTTLS: 587
	 */
	public static void main(String[] args) {
		final String toEmail = Credentials.DEMO_RECIPIENT; // can be any email id 
		
		System.out.println("TLSEmail Start");
		

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
		props.put("mail.smtp.port", "587"); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		
		sendImageEmail(toEmail, 
				"TLSEmail Testing Subject: "+new Date(System.currentTimeMillis()), 
				"TLSEmail Testing Body: "+Long.toHexString(System.nanoTime()));
		
	}
	

	/**
	 * Utility method to send simple HTML email
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public static void sendEmail(String toEmail, String subject, String body){
		try
		{
			MimeMessage msg = new MimeMessage();
			//set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));

			msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

			msg.setSubject(subject, "UTF-8");

			msg.setText(body, "UTF-8");
			

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			System.out.println("Message is ready");
			

			System.out.println("EMail Sent Successfully!!");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Utility method to send image in email body
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public static void sendImageEmail(String toEmail, String subject, String body){
		try{
	         MimeMessage msg = new MimeMessage();
	         msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		     msg.addHeader("format", "flowed");
		     msg.addHeader("Content-Transfer-Encoding", "8bit");
		      
		     msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));

		     msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

		     msg.setSubject(subject, "UTF-8");

		     msg.setSentDate(new Date());

		     msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
		      
	         // Create the message body part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         messageBodyPart.setText(body);
	         
	         // Create a multipart message for attachment
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Second part is image attachment
	         messageBodyPart = new MimeBodyPart();
	         String filename = "image.png";
	         DataSource source = new FileDataSource(filename);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(filename);
	         //Trick is to add the content-id header here
	         messageBodyPart.setHeader("Content-ID", "image_id");
	         multipart.addBodyPart(messageBodyPart);

	         //third part for displaying image in the email body
	         messageBodyPart = new MimeBodyPart();
	         messageBodyPart.setContent("<h1>Attached Image</h1>" +
	        		     "<img src='cid:image_id'>", "text/html");
	         multipart.addBodyPart(messageBodyPart);
	         
	         //Set the multipart message to the email message
	         msg.setContent(multipart);

	         // Send message
	        //MnSender.send(msg);
	        
	        
			
	        SMTP_ConnectionParams params = SMTP_ConnectionParams.secure(
	        		Credentials.DEMO_HOST, 
	        		Credentials.DEMO_USERNAME, 
	        		Credentials.DEMO_PASSWORD); // correct password for gmail id
	        
	        SMTP_TransportProps props = new SMTP_TransportProps();
	        
	        SMTPTransport transport = new SMTPTransport(props, params);
	        
	        transport.connect();
	        
	        transport.sendMessage(msg, new String[] { "convert.pierre@gmail.com" });
	        
	        transport.close();
	        
	         System.out.println("EMail Sent Successfully with image!!");
	      }catch (MessagingException e) {
	         e.printStackTrace();
	      } catch (UnsupportedEncodingException e) {
			 e.printStackTrace();
		}
	}


}
