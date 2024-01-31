package com.s8.core.web.manganese;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.s8.api.flow.mail.S8Mail;
import com.s8.core.web.manganese.generator.HTML_MgMailContentGenerator;
import com.s8.core.web.manganese.javax.mail.Message;
import com.s8.core.web.manganese.javax.mail.MessagingException;
import com.s8.core.web.manganese.javax.mail.Session;
import com.s8.core.web.manganese.javax.mail.internet.InternetAddress;
import com.s8.core.web.manganese.javax.mail.internet.MimeMessage;


/**
 * 
 */
public class MgMail implements S8Mail {


	public final ManganeseWebService service;

	final Message emailMessage;

	private final HTML_MgMailContentGenerator generator;


	public final static String DEFAULT_SENDER_NAME = "S8 Mail WebService";

	private boolean isSenderDefined = false;

	private boolean isRecipientsDefined = false;

	private boolean isSubjectSet = false;


	private boolean isLocked = false;

	/**
	 * 
	 * @param session
	 * @param mailServerUsername
	 * @param displayedName
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	MgMail(ManganeseWebService service, Session session) 
			throws UnsupportedEncodingException, MessagingException {
		super();

		this.service = service;

		this.generator = new HTML_MgMailContentGenerator(service.CSS_getClassBase());

		/* create message */
		this.emailMessage = new MimeMessage(session);
	}



	@Override
	public void setDisplayedSender(String name) throws IOException {
		if(isLocked) { throw new IOException("Mail is already closed"); }
		try {
			emailMessage.setFrom(new InternetAddress(service.getMailServerUsername(), name));
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
	public void HTML_setWrapperStyle(String CSS_classname, String CSS_style) throws IOException {
		if(isLocked) { throw new IOException("Mail is already closed"); }
		generator.setWrapperStyle(CSS_classname, CSS_style);
	}



	@Override
	public void HTML_appendBaseElement(String tag, String CSS_classname, String CSS_style, String innerHTMLText)
			throws IOException {
		if(isLocked) { throw new IOException("Mail is already closed"); }
		generator.HTML_appendBaseElement(tag, CSS_classname, CSS_style, innerHTMLText);	
	}




	public void validate() throws IOException {
		if(isLocked) { throw new IOException("Mail is already closed"); }
		if(!isRecipientsDefined) { throw new IOException("No recipient"); }

		try {
			if(!isSenderDefined) {
				emailMessage.setFrom(new InternetAddress(
						service.getMailServerUsername(), 
						service.getdefaultSenderDisplayedName()));
			}

			if(!isSubjectSet) {
				emailMessage.setSubject("WebService email");
			}

			/* generate content */
			String content = generator.generateContent();
			emailMessage.setContent(content, "text/html");

		} 
		catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
			throw new IOException("Failed to validate message");
		}

		isLocked = true;
	}







}
