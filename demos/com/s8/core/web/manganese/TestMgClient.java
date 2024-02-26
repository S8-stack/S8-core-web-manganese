package com.s8.core.web.manganese;

import java.io.IOException;

import com.s8.core.web.manganese.sasl.SASL_PlainAuthenticator;
import com.s8.core.web.manganese.smtp.SMTP_MgClient;

public class TestMgClient {

	public static void main(String[] args) throws IOException {

		SMTP_MgClient client = new SMTP_MgClient("smtp.gmail.com", 587, new String[] {"TLSv1.2", "TLSv1.3"});
		
		SASL_PlainAuthenticator authenticator = new SASL_PlainAuthenticator(
				"noreply@alphaventor.com", 
				"aGEvxCd-&_8FGX-dMOiweRE!_XH");
		
		client.sendMail(
				authenticator,
				"noreply@alphaventor.com", 
				"convert.pierre@gmail.com", 
				new String[]{ "A super simple mail", "Hi!"}, true);
		
		
	}

}
