package com.s8.core.web.manganese.mail.smtp;


/**
 * 
 */
public class SMTP_ConnectionParams {
	
	
	public final static int DEFAULT_SECURE_PORT = 587;
	
	/**
	 * 
	 */
	public String host = "localhost";
	
	/**
	 * connection port
	 */
	public int port = DEFAULT_SECURE_PORT;
	
	public boolean isSecured = true;
	
	public String username;
	
	public String password;

	
	
	
	
	public SMTP_ConnectionParams() {
		super();
	}





	/**
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 */
	public SMTP_ConnectionParams(String host, int port, boolean isSecured, String username, String password) {
		super();
		this.host = host;
		this.port = port;
		this.isSecured = isSecured;
		this.username = username;
		this.password = password;
	}
	
	

}
