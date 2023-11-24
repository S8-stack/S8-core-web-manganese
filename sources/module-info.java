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
	exports com.s8.core.web.manganese.mail.internet;
	
	
	requires java.logging;
	requires java.desktop;
	requires java.security.sasl;
	requires java.naming;
	
	requires org.slf4j;
	
	
}