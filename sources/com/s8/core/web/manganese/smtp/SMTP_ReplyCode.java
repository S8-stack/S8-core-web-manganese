package com.s8.core.web.manganese.smtp;



/**
 * 
 * From Klensin | Standards Track [Page 51] of RFC 5321 (SMTP) October 2008
 * 4.2.3.  Reply Codes in Numeric Order
 * 
 * 211  System status, or system help reply

   214  Help message (Information on how to use the receiver or the
      meaning of a particular non-standard command; this reply is useful
      only to the human user)

   220  <domain> Service ready

   221  <domain> Service closing transmission channel

   250  Requested mail action okay, completed

   251  User not local; will forward to <forward-path> (See Section 3.4)

   252  Cannot VRFY user, but will accept message and attempt delivery
      (See Section 3.5.3)

   354  Start mail input; end with <CRLF>.<CRLF>

   421  <domain> Service not available, closing transmission channel
      (This may be a reply to any command if the service knows it must
      shut down)

   450  Requested mail action not taken: mailbox unavailable (e.g.,
      mailbox busy or temporarily blocked for policy reasons)

   451  Requested action aborted: local error in processing

   452  Requested action not taken: insufficient system storage

   455  Server unable to accommodate parameters

   500  Syntax error, command unrecognized (This may include errors such
      as command line too long)

   501  Syntax error in parameters or arguments

   502  Command not implemented (see Section 4.2.4)

   503  Bad sequence of commands

   504  Command parameter not implemented

   550  Requested action not taken: mailbox unavailable (e.g., mailbox
      not found, no access, or command rejected for policy reasons)




Klensin                     Standards Track                    [Page 52]

RFC 5321                          SMTP                      October 2008


   551  User not local; please try <forward-path> (See Section 3.4)

   552  Requested mail action aborted: exceeded storage allocation

   553  Requested action not taken: mailbox name not allowed (e.g.,
      mailbox syntax incorrect)

   554  Transaction failed (Or, in the case of a connection-opening
      response, "No SMTP service here")

   555  MAIL FROM/RCPT TO parameters not recognized or not implemented
 */
public class SMTP_ReplyCode {
	
	
	

	
	/**
	 * 220 – <domain> service is ready 
	 */
	public final static int DOMAIN_SERVICE_IS_READY = 220;
	
	/**
	 * 250 – the requested action is okay or completed 
	 */
	public final static int OK = 250;
	
	
	/**
	 * 211 – system status or reply to HELP
	 */
	public final static int SYSTEM_STATUS = 211;
	
	
	/**
	 * 221 – <domain> closing the transmission channel 
	 */
	public final static int CLOSING = 221;
	

	/**
	 * 354 – start mail input (usually responds to DATA command) 
	 */
	public final static int START_MAIL_INPUT = 354;

	
	
	/**
	 * 500 – syntax error or command couldn’t be recognized
	 */
	public final static int SYNTAX_ERROR = 500;
	
	/**
	 * 503 – bad sequence of commands
	 */
	public final static int BAD_COMMANDS_SEQUENCE = 503;
	

	/**
	 * 252 – the server can’t verify the user. 
	 * It will still accept the message and attempt the delivery (responds to VRFY command)
	 */
	public final static int CANNOT_VERIFY_USER = 252;
	
	/**
	 * 450 – mailbox unavailable 
	 */
	public final static int UNAVAILABLE_MAILBOX = 450;
	
	/**
	 * 510 – invalid email address
	 */
	public final static int INVALID_EMAIL_ADDRESS = 510;
	
	
	
	
	
	
	public final int code;

	private SMTP_ReplyCode(int code) {
		this.code = code;
	}
	
	
	
	public static String parse(int code) {
		switch(code) {
		
		case 250 : return "OK";
		case 211 : return "SYSTEM_STATUS";
		case 220 : return "DOMAIN_SERVICE_IS_READY";
		case 221 : return "CLOSING";
		case 354 : return "START_MAIL_INPUT";
		
		case 500 : return "SYNTAX_ERROR";
		case 503 : return "BAD_COMMANDS_SEQUENCE";
		case 252 : return "CANNOT_VERIFY_USER";
		case 450 : return "UNAVAILABLE_MAILBOX";
		case 510 : return "INVALID_EMAIL_ADDRESS";
		
		default : return "<Unknown>";
		
		}
	}
	
	
	
}
