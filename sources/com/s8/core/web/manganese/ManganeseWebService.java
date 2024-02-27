package com.s8.core.web.manganese;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.s8.api.flow.mail.SendMailS8Request;
import com.s8.api.flow.mail.SendMailS8Request.Status;
import com.s8.core.io.xml.annotations.XML_SetElement;
import com.s8.core.io.xml.annotations.XML_Type;
import com.s8.core.web.manganese.css.CSS_ClassBase;
import com.s8.core.web.manganese.javax.mail.MessagingException;
import com.s8.core.web.manganese.javax.mail.Session;
import com.s8.core.web.manganese.sasl.SASL_Authenticator;
import com.s8.core.web.manganese.sasl.SASL_PlainAuthenticator;
import com.s8.core.web.manganese.smtp.SMTP_MgClient;



/**
 * 
 * @author pierreconvert
 *
 */
public class ManganeseWebService {


	@XML_Type(name = "ManganeseWebServiceConfiguration")
	public static class Config {

		public String host;
		
		public int port;
		
		public String[] tunnelingProtocols = new String[] { "TLSv1.3" };

		public String username;

		public String password;


		public String defaultSenderDisplayedName;
		
		public String CSS_pathname;

		public boolean isVerbose;

		@XML_SetElement(tag = "host")
		public void setMailServer(String host) { this.host = host; }

		@XML_SetElement(tag = "port")
		public void setPort(int port) { this.port = port; }
		
		@XML_SetElement(tag = "tunneling-protocols")
		public void setTunnelingProtocols(String protocols) { this.tunnelingProtocols = protocols.split(" *, *"); }
		
		@XML_SetElement(tag = "username")
		public void setUsername(String username) { this.username = username; }

		@XML_SetElement(tag = "password")
		public void setPassword(String password) { this.password = password; }


		@XML_SetElement(tag = "default-displayed-name")
		public void setDefualtDisplayname(String name) { this.defaultSenderDisplayedName = name; }
		
		@XML_SetElement(tag = "CSS-style-pathname")
		public void setCSSStyle(String pathname) { this.CSS_pathname = pathname; }

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





	public final SMTP_MgClient smtp_client;
	
	public final SASL_Authenticator sasl_authenticator;

	public final ConcurrentLinkedQueue<SendMailS8Request> requestsQueue = new ConcurrentLinkedQueue<>();

	
	private CSS_ClassBase classBase;

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
		
		smtp_client = new SMTP_MgClient(config.host, config.port, config.tunnelingProtocols);
		
		sasl_authenticator = new SASL_PlainAuthenticator(config.username, config.password);
	}


	
	synchronized CSS_ClassBase CSS_getClassBase() {
		if(classBase == null) {
			classBase = new CSS_ClassBase();
			classBase.parseFile(config.CSS_pathname);
		}
		return classBase;
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
				
				/* create mail */
				MgMail mail = new MgMail(this, session);

				/* compose mail */
				request.compose(mail);
				
				/* validate before sending */
				mail.compile();

				List<String> content = mail.getContent();
				
				
				
				smtp_client.sendMail(sasl_authenticator, config.username, mail.getRecipientMailAddress(), content, config.isVerbose);
				
				isTerminated = true;
				request.onSent(Status.OK, "Sent");
			} 
			catch (MessagingException e) {
				if(nAttempts == maxNbAttempts) {
					isTerminated = true;
					request.onSent(Status.MAIL_REJECTED_BY_OUTGOING_SERVER, e.getMessage());
				}
				else {
					disconnect();
					if(isVerbose) { e.printStackTrace(); }
				}
			}
			catch (IOException e) {
				isTerminated = true;
				request.onSent(Status.INVALID_COMPOSING, e.getMessage());
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
