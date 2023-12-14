package com.s8.core.web.manganese;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

import com.s8.api.flow.mail.ComposeMailS8Request;
import com.s8.api.flow.mail.ComposeMailS8Request.Status;
import com.s8.core.arch.silicon.async.AsyncSiTask;
import com.s8.core.arch.silicon.async.MthProfile;


/**
 * 
 */
public class MailSendingTask implements AsyncSiTask {

	public @Override String describe() { return "Compose then send mail"; }

	public @Override MthProfile profile() { return MthProfile.WEB_REQUEST_PROCESSING; }


	/**
	 * service
	 */
	public final ManganeseWebService service;


	/**
	 * request
	 */
	public final ComposeMailS8Request request;


	/**
	 * 
	 * @param service
	 * @param request
	 */
	public MailSendingTask(ManganeseWebService service, ComposeMailS8Request request) {
		super();
		this.service = service;
		this.request = request;
	}




	@Override
	public void run() {

		/* retrieve session */
		Session session = service.getSession();

		
		try {
			/* create mail */
			MgMail mail = new MgMail(service, session);
			
			/** compose mail */
			request.compose(mail);
			
			/* validate mail */
			mail.validate();
			
			/* send */
			try {
				Transport.send(mail.emailMessage);
				
				request.onSent(Status.OK);
			}
			catch (MessagingException e) {
				e.printStackTrace();
				request.onSent(Status.MAIL_REJECTED_BY_OUTGOING_SERVER);
			}
			
		} 
		catch (IOException | MessagingException e) {
			e.printStackTrace();
			request.onFailed(e);
		}

	}

	

}
