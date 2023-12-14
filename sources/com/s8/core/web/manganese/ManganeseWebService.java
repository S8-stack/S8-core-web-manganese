package com.s8.core.web.manganese;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import com.s8.api.flow.mail.SendMailS8Request;
import com.s8.api.flow.mail.SendMailS8Request.Status;
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

	public final Config config;

	private boolean isVerbose;

	private Session session;






	public final ConcurrentLinkedQueue<SendMailS8Request> requestsQueue = new ConcurrentLinkedQueue<>();


	/**
	 * 
	 * @param xml_Context
	 * @param app
	 * @param config
	 * @throws Exception
	 */
	public ManganeseWebService(Config config) throws Exception {
		super();
		this.config = config;
		this.isVerbose = config.isVerbose;
	}




	/**
	 * 
	 * @return
	 */
	private synchronized Session getSession() {

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
		return session;
	}


	private synchronized void disconnect() {
		session = null;
	}



	public final static int NB_ATTEMPTS = 4;


	public void sendMail(SendMailS8Request request) {

		int nAttempts = 0, maxNbAttempts = NB_ATTEMPTS;
		boolean isTerminated = false;

		while(!isTerminated && nAttempts++ < maxNbAttempts) {
			try {
				
				/* retrieve session */
				Session session = getSession();

				/* create mail */
				MgMail mail = new MgMail(this, session);

				/* compose mail */
				request.compose(mail);	
				
				/* validate before sending */
				mail.validate();

				/* send */
				Transport.send(mail.emailMessage);
				
				isTerminated = true;
				request.onSent(Status.OK);
			} 
			catch (MessagingException e) {
				if(nAttempts == maxNbAttempts) {
					isTerminated = true;
					request.onSent(Status.MAIL_REJECTED_BY_OUTGOING_SERVER);
				}
				else {
					disconnect();
					if(isVerbose) { e.printStackTrace(); }
				}
			}
			catch (IOException e) {
				isTerminated = true;
				request.onSent(Status.INVALID_COMPOSING);
			}
		}
	}


	public String getMailServerUsername() {
		return config.username;
	}

	public String getdefaultSenderDisplayedName() {
		return config.defaultSenderDisplayedName;
	}	


}
