package com.s8.core.web.manganese;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.s8.api.flow.mail.S8Mail;
import com.s8.core.web.manganese.html.HTML_MgMailContentGenerator;
import com.s8.core.web.manganese.mime.MIME_Composer;
import com.s8.core.web.manganese.mime.MIME_Header;


/**
 * 
 */
public class MgMail implements S8Mail {


	public final ManganeseWebService service;


	private final HTML_MgMailContentGenerator html_generator;


	public final static String DEFAULT_SENDER_NAME = "S8 Mail WebService";

	private String senderName;

	private boolean isSenderNameDefined = false;

	private String recipientEmail;

	private boolean isRecipientDefined = false;

	private String subject;

	private boolean isSubjectSet = false;


	/**
	 * 
	 * @param session
	 * @param mailServerUsername
	 * @param displayedName
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	MgMail(ManganeseWebService service) throws UnsupportedEncodingException {
		super();

		this.service = service;

		this.html_generator = new HTML_MgMailContentGenerator(service.CSS_getClassBase());

	}



	@Override
	public void setDisplayedSender(String name) {
		this.senderName = name;
		this.isSenderNameDefined = true;
	}


	@Override
	public void setRecipient(String email) {
		this.recipientEmail = email;
		this.isRecipientDefined = true;
	}


	@Override
	public void setSubject(String text) {
		this.subject = text;
		this.isSubjectSet = true;
	}


	@Override
	public void HTML_setWrapperStyle(String CSS_classname, String CSS_style) {
		html_generator.setWrapperStyle(CSS_classname, CSS_style);
	}


	@Override
	public void HTML_appendBaseElement(String tag, 
			String CSS_classname, 
			String CSS_style, 
			String innerHTMLText) {
		html_generator.HTML_appendBaseElement(tag, CSS_classname, CSS_style, innerHTMLText);	
	}




	public List<String> compile() throws IOException {
		
		if(!isRecipientDefined) { throw new IOException("No recipient"); }

		if(!isSenderNameDefined) {
			setDisplayedSender(service.getdefaultSenderDisplayedName() + '<' + service.getMailServerUsername() + '>');
		}

		if(!isSubjectSet) {
			setSubject("WebService email");
		}

		/* generate content */
		List<String> body = new ArrayList<>();

		body.add(MIME_Composer.composeHeaders(
				new MIME_Header("From", senderName),
				new MIME_Header("To", recipientEmail),
				new MIME_Header("Subject", subject),
				new MIME_Header("Date", new Date(System.currentTimeMillis()).toString()),
				new MIME_Header("Content-Type", "text/html; charset=UTF-8"),
				new MIME_Header("MIME-Version", "1.0")));

		html_generator.generateContent(body);

		return body;
	}



	/**
	 * 
	 * @return
	 */
	public String getSubject() {
		return subject;
	}


	/**
	 * 
	 * @return
	 */
	public String getRecipientMailAddress() {
		return recipientEmail;
	}


	/**
	 * 
	 * @return
	 */
	public String getSenderName() {
		return senderName;
	}



}
