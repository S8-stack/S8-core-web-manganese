package com.s8.core.web.manganese.mail;


/**
 * 
 */
public enum MnStoreProtocol {



	/**
	 * 
	 */
	IMAP("IMAP"),


	/**
	 * 
	 */
	IMAPS("Secure IMAP"),


	/**
	 * 
	 */
	POP3("POP3"),


	/**
	 * 
	 */
	POP3S("Secure POP3");


	/**
	 * 
	 */
	public final String name;


	private MnStoreProtocol(String name) {
		this.name = name;
	}


	public static MnStoreProtocol get(String tag) {
		switch(tag) {

		case "imap": 
		case "IMAP":
			return IMAP;

		case "imaps":
		case "IMAPS":
		case "IMAPSSL":
			return IMAPS;


		case "pop3":
		case "POP3":
			return POP3;

		case "pop3s":
		case "pops":
		case "POP3S":
		case "POPS":
			return POP3S;

		default : return null;

		}
	}
}
