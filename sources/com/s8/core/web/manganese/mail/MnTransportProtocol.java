package com.s8.core.web.manganese.mail;

import com.s8.core.web.manganese.mail.smtp.SMTPSSLTransport;
import com.s8.core.web.manganese.mail.smtp.SMTPTransport;

/**
 * 
 */
public enum MnTransportProtocol {


	/**
	 * 
	 */
	SMTP("SMTP"),

	/**
	 * 
	 */
	SMTPS("Secure SMTP");


	/**
	 * 
	 */
	public final String name;


	private MnTransportProtocol(String name) {
		this.name = name;
	}


	/**
	 * 
	 * @param tag
	 * @param session
	 * @param urlname
	 * @return
	 * @throws NoSuchProviderException
	 */
	public static MnTransportService provideServiceFor(String tag, Session session, URLName urlname) 
			throws NoSuchProviderException {
		if(tag != null) {
			switch(tag) {


			case "smtp":
			case "SMTP":
				return new SMTPTransport(session, urlname);

			case "smtps":
			case "SMTPS":
				return new SMTPSSLTransport(session, urlname);

			}	
		}
		throw new NoSuchProviderException("No provider for : " + tag);
	}
}
