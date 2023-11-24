package com.s8.core.web.manganese.demos;

import com.s8.core.web.manganese.critical.Credentials;
import com.s8.core.web.manganese.main.java.Email;
import com.s8.core.web.manganese.main.java.EmailClient;
import com.s8.core.web.manganese.main.java.MailHost;

public class NanoTestMail04 {



	public static void main(String[] args) {


		// simple usage (smtp via SSL/TLS)

		MailHost mailhost = new MailHost();
		mailhost.setHost(Credentials.DEMO_HOST, 465);
		mailhost.setAuth(Credentials.DEMO_USERNAME, Credentials.DEMO_PASSWORD);

		Email email = new Email();
		email.setFrom("pierre.convert@alphaventor.com");
		email.setRecipient("convert.pierre@gmail.com");
		email.setSubject("Hello I am using a new java library");
		email.setBody("Hi. Just wanted to say I am testing SMTP!");

		try {
			EmailClient emailclient = new EmailClient();
			String response = emailclient.send(mailhost, email);
			System.out.println("RESPONSE: " + response);
		} catch( Exception e ) {
			e.printStackTrace();
		}



		// advanced Options

		// use STARTTLS instead of TLS directly

		mailhost.useStarttls(); 

		// use "AUTH LOGIN", default is "AUTH PLAIN" 

		mailhost.setAuthType(MailHost.AUTH_LOGIN); 

		// If you specify an HTML body in addition to setBody(), 
		// the email will be send as multipart/alternative

		email.setHtmlBody("Hi. Just wanted to say I am <b>testing</b> SMTP!");


		// debugging Options

		// print all messaging to stdout, default: false

		//emailclient.setDebug(false);

		// do not actually send the email, instead just print the formatted 
		// email and headers to stdout.

		//emailclient.setEnableSend(false);

	}
}