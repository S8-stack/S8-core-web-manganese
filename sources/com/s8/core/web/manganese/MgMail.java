package com.s8.core.web.manganese;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.s8.api.flow.mail.S8Mail;

public class MgMail implements S8Mail {


	public final ManganeseWebService service;

	public final Message emailMessage;


	private String mailServerUsername;


	public final static String DEFAULT_SENDER_NAME = "S8 Mail WebService";

	private boolean isSenderDefined = false;

	private boolean isRecipientsDefined = false;

	private boolean isSubjectSet = false;

	private final List<MimeBodyPart> parts = new ArrayList<>();

	private boolean isLocked = false;

	/**
	 * 
	 * @param session
	 * @param mailServerUsername
	 * @param displayedName
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	public MgMail(ManganeseWebService service, Session session) 
			throws UnsupportedEncodingException, MessagingException {
		super();

		this.service = service;

		/* create message */
		this.emailMessage = new MimeMessage(session);
	}



	@Override
	public void setDisplayedSender(String name) throws IOException {
		if(isLocked) { throw new IOException("Mail is already closed"); }
		try {
			emailMessage.setFrom(new InternetAddress(mailServerUsername, name));
			this.isSenderDefined = true;
		} 
		catch (UnsupportedEncodingException | MessagingException e) {
			throw new IOException("Invalid sender: "+e.getMessage());
		}
	}



	@Override
	public void setRecipient(String email) throws IOException {
		if(isLocked) { throw new IOException("Mail is already closed"); }
		try {
			InternetAddress[] address = InternetAddress.parse(email);
			emailMessage.setRecipients(Message.RecipientType.TO, address);
			isRecipientsDefined = true;
		} catch (MessagingException e) {
			throw new IOException("Failed to set recipients: "+e.getMessage());
		}

	}


	@Override
	public void setSubject(String text) throws IOException {
		if(isLocked) { throw new IOException("Mail is already closed"); }
		try {
			emailMessage.setSubject(text);
			isSubjectSet = true;
		} 
		catch (MessagingException e) {
			e.printStackTrace();
			throw new IOException("Failed to set subject: "+e.getMessage());
		}
	}

	@Override
	public void appendText(String text) throws IOException {
		if(isLocked) { throw new IOException("Mail is already closed"); }

		MimeBodyPart messageBodyPart = new MimeBodyPart();
		try {
			messageBodyPart.setText(text);
			parts.add(messageBodyPart);
		} 
		catch (MessagingException e) {
			e.printStackTrace();
			throw new IOException("Failed to add text to mail, due to: "+e.getMessage());
		}
	}


	public void validate() throws IOException {
		if(isLocked) { throw new IOException("Mail is already closed"); }
		if(!isRecipientsDefined) { throw new IOException("No recipient"); }
		if(parts.isEmpty()) { throw new IOException("No object in this mail"); }

		try {
			if(!isSenderDefined) {
				emailMessage.setFrom(new InternetAddress(mailServerUsername, service.getdefaultSenderDisplayedName()));
			}
			
			if(!isSubjectSet) {
				emailMessage.setSubject("WebService email");
			}
			
			/* compile content */
			Multipart multipart = new MimeMultipart();
			for(MimeBodyPart part : parts) { multipart.addBodyPart(part); }
			emailMessage.setContent(multipart);
		} 
		catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
			throw new IOException("Failed to validate message");
		}
		
		isLocked = true;
	}

}
