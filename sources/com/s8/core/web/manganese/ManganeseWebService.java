package com.s8.core.web.manganese;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.s8.api.flow.mail.SendMailS8Request;
import com.s8.api.flow.mail.SendMailS8Request.Status;
import com.s8.core.web.manganese.sasl.SASL_Authenticator;
import com.s8.core.web.manganese.smtp.SMTP_MgClient;



/**
 * 
 * @author pierreconvert
 *
 */
public class ManganeseWebService {





	public final static String ROOT_WEB_PATHNAME = "/";

	public final MgConfiguration config;

	private boolean isVerbose;


	public final SMTP_MgClient smtp_client;
	
	public final SASL_Authenticator sasl_authenticator;

	public final ConcurrentLinkedQueue<SendMailS8Request> requestsQueue = new ConcurrentLinkedQueue<>();

	public final MgMailDefaultSettings defaultSettings;

	/**
	 * 
	 * @param xml_Context
	 * @param app
	 * @param config
	 * @throws Exception
	 */
	public ManganeseWebService(MgConfiguration config) throws Exception {
		super();
		this.config = config;
		this.isVerbose = config.isVerbose;
		
		smtp_client = new SMTP_MgClient(config.host, config.port, config.tunnelingProtocols);
		
		sasl_authenticator = config.authenticationConfig.generateAuthenticator();
		
		defaultSettings = new MgMailDefaultSettings(
				config.CSS_pathname, 
				config.senderEmail,
				config.senderName);
	}


	
	


	public final static int NB_ATTEMPTS = 4;


	public void sendMail(SendMailS8Request request) {

		int nAttempts = 0, maxNbAttempts = NB_ATTEMPTS;
		boolean isTerminated = false;

		while(!isTerminated && nAttempts++ < maxNbAttempts) {
			try {
				
				/* create mail */
				MgMail mail = new MgMail(defaultSettings);

				/* compose mail */
				request.compose(mail);
				
				/* compile into lines */
				List<String> body = mail.compile();
				
				smtp_client.sendMail(sasl_authenticator, config.senderEmail, mail.getRecipientMailAddress(), body, isVerbose);
				
				isTerminated = true;
				request.onSent(Status.OK, "Sent");
			} 
			catch (MgServerException e) {
				if(nAttempts == maxNbAttempts) {
					isTerminated = true;
					request.onSent(Status.MAIL_REJECTED_BY_OUTGOING_SERVER, e.getMessage());
				}
			}
			catch (IOException e) {
				isTerminated = true;
				request.onSent(Status.INVALID_COMPOSING, e.getMessage());
			}
		}
	}


	


}
