package com.s8.core.web.manganese.mail;


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


	public static MnTransportProtocol get(String tag) {
		if(tag != null) {
			switch(tag) {


			case "smtp":
			case "SMTP":
				return SMTP;

			case "smtps":
			case "SMTPS":
				return SMTPS;


			default : return null;

			}	
		}
		else {
			return null;
		}
		
	}
}
