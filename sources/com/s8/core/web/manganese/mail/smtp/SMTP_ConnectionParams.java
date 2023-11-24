package com.s8.core.web.manganese.mail.smtp;


/**
 * 
 */
public class SMTP_ConnectionParams {
	
	
	public static SMTP_ConnectionParams secure(String host, String username, String password) {
		return new SMTP_ConnectionParams(host, DEFAULT_SECURE_PORT, true, username, password);
	}
	
	
	
	/**
	 * 
	 */
	public final static int DEFAULT_SECURE_PORT = 465;
	
	
	/**
	 * 
	 */
	public final static int DEFAULT_UNSECURE_PORT = 25;
	
	
	/**
	 * 
	 */
	private final String host;
	
	
	private final int port;
	/**
	 * is secured
	 */
	private boolean isSecured = true;
	
	private final String username;
	
	private final String password;



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
	
	
	/**
	 * 
	 * @return
	 */
	public String getHost() {
		return host;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public boolean isSecured() {
		return isSecured;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}
	

}
