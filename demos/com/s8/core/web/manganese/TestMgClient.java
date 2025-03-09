package com.s8.core.web.manganese;

import java.io.IOException;
import java.util.List;

import com.s8.core.web.manganese.sasl.SASL_PlainAuthenticator;
import com.s8.core.web.manganese.smtp.SMTP_MgClient;

public class TestMgClient {

	public static void main(String[] args) throws IOException, InterruptedException {

		
		SMTP_MgClient client = new SMTP_MgClient("in-v3.mailjet.com", 587, new String[] {"TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3"});
		
		/*
		SASL_PlainAuthenticator authenticator = new SASL_PlainAuthenticator(
				"noreply@alphaventor.com", 
				"aGEvxCd-&_8FGX-dMOiweRE!_XH");
		
		*/
		SASL_PlainAuthenticator authenticator = new SASL_PlainAuthenticator(
				"0ef07c62eeda25cb5b5d5058cef045a0", 
				"4022391b095dbf4809e67b53d9c72366");
		
		
		MgMailDefaultSettings defaultSettings = new MgMailDefaultSettings(
				"/Users/pc/qx/git/S8-core-web-manganese/web-sources/S8-core-web-manganese/mg-mail-style.css", 
				"noreply@alphaventor.com", "S8 Service");
		
		MgMail mail = new MgMail(defaultSettings);
		
		mail.setRecipient("convert.pierre@gmail.com");
		
		mail.setSubject("Sign-Up email confirmation");
		
		mail.html_setWrapperStyle(".mg-mail-wrapper", null);
		mail.html_appendBaseElement("div", 
				".mg-mail-banner", 
				"background-image: url(https://alphaventor.com/assets/logos/AlphaventorLogo-1024px-black-text.png);", 
				null);
		
		mail.html_appendBaseElement("h1", ".mg-h1", null, "Hello dear AlphaVentor user!");
		mail.html_appendBaseElement("h2", ".mg-h2", null, "Welcome to a world of designs");
		mail.html_appendBaseElement("p", ".mg-p", null, 
				"Please find below your validation code for the creation of your account:");
		
		mail.html_appendBaseElement("div", ".mg-code-wrapper", null, "IUOEN");
		
		mail.html_appendBaseElement("p", ".mg-p", null, 
				"If you're not the initiator of this, please report to pierre.convert@alphaventor.com");
		
		
		
		
		List<String> lines = mail.compile();
		
		client.sendMail(
				authenticator,
				"pierre.convert@alphaventor.com", 
				"convert.pierre@gmail.com", 
				lines, true);
		
		
	}

}
