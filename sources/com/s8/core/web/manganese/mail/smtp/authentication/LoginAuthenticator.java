package com.s8.core.web.manganese.mail.smtp.authentication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.s8.core.web.manganese.mail.MessagingException;
import com.s8.core.web.manganese.mail.smtp.SMTPTransport;
import com.s8.core.web.manganese.mail.util.BASE64EncoderStream;

/**
 * Perform the authentication handshake for LOGIN authentication.
 */
public class LoginAuthenticator extends Authenticator {


	public LoginAuthenticator(SMTPTransport transport) {
		super(transport, "LOGIN");
	}

	
	
	@Override
	public void doAuth(String host, String authzid, String user, String passwd)
			throws MessagingException, IOException {
		// send username
		resp = transport.simpleCommand(BASE64EncoderStream.encode(
				user.getBytes(StandardCharsets.UTF_8)));
		if (resp == 334) {
			// send passwd
			resp = transport.simpleCommand(BASE64EncoderStream.encode(
					passwd.getBytes(StandardCharsets.UTF_8)));
		}
	}
}