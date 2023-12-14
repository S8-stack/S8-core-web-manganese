package com.s8.core.web.manganese;

import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import com.s8.api.flow.mail.ComposeMailS8Request;
import com.s8.core.arch.silicon.SiliconEngine;
import com.s8.core.io.xml.annotations.XML_SetElement;
import com.s8.core.io.xml.annotations.XML_Type;



/**
 * 
 * @author pierreconvert
 *
 */
public class ManganeseWebService {


	@XML_Type(name = "CarbonWebServiceConfiguration")
	public static class Config {

		public String host;
		
		public String username;
		
		public String password;
		
		public int port;
		
		public String defaultSenderDisplayedName;

		public boolean isVerbose;
	
		@XML_SetElement(tag = "server")
		public void setMailServer(String host) { this.host = host; }

		@XML_SetElement(tag = "username")
		public void setUsername(String username) { this.username = username; }
		
		@XML_SetElement(tag = "username")
		public void setPassword(String password) { this.password = password; }
		
		@XML_SetElement(tag = "port")
		public void setPort(int port) { this.port = port; }
		
		@XML_SetElement(tag = "default-displayed-name")
		public void setDefualtDisplayname(String name) { this.defaultSenderDisplayedName = name; }
		
		@XML_SetElement(tag = "is-verbose")
		public void setVerbosity(boolean isVerbose) { this.isVerbose = isVerbose; }
		

		/**
		 * 
		 */
		public Config() {
			super();
		}
	}


	public final static String ROOT_WEB_PATHNAME = "/";

	private final SiliconEngine app;
	
	public final Config config;

	private boolean isVerbose;

	private Session session;
	
	
	
	
	final Lock lock = new ReentrantLock();
	
	boolean isLive;
	
	
	public final ConcurrentLinkedQueue<ComposeMailS8Request> requestsQueue = new ConcurrentLinkedQueue<>();

	
	/**
	 * 
	 * @param xml_Context
	 * @param app
	 * @param config
	 * @throws Exception
	 */
	public ManganeseWebService(SiliconEngine app, Config config) throws Exception {
		super();

		this.app = app;
		this.config = config;
		this.isVerbose = config.isVerbose;
	}
	
	
	
	public void push(ComposeMailS8Request request) {
		
		requestsQueue.add(request);
		
		lock.lock();
		if(!isLive) {
			app.pushAsyncTask(new MailSendingTask(this, request));
			isLive = true;
		}
		lock.unlock();
	}
	
	
	public Session getSession() {
		lock.lock();
		if(session == null) {
			
			Properties props = new Properties();
			props.put("mail.smtp.host", config.host);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", config.port);
			props.put("mail.smtp.starttls.enable", "true"); //TLS
			
			session = Session.getInstance(props, new Authenticator() {
				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(config.username, config.password);
				}
			});
			
			if(isVerbose) {
				System.out.println("Mail session is now created");
			}
		}
		lock .unlock();
		return session;
	}


	
	public String getMailServerUsername() {
		return config.username;
	}
	
	public String getdefaultSenderDisplayedName() {
		return config.defaultSenderDisplayedName;
	}	
	
	
}
