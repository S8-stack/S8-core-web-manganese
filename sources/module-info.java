/**
 * 
 */
/**
 * 
 */


module com.s8.core.web.manganese {
	
	exports com.s8.core.web.manganese.activation;
	
	exports com.s8.core.web.manganese.mail;
	exports com.s8.core.web.manganese.mail.smtp;
	exports com.s8.core.web.manganese.mail.pop3;
	exports com.s8.core.web.manganese.mail.imap;
	exports com.s8.core.web.manganese.mail.internet;
	exports com.s8.core.web.manganese.mail.search;
	
	uses com.s8.core.web.manganese.mail.Provider;
	
	
	
	requires java.logging;
	requires java.desktop;
	requires java.security.sasl;
	
	
}