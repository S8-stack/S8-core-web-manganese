package com.s8.core.web.manganese.mail.smtp.authentication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.s8.core.web.manganese.mail.AuthenticationFailedException;
import com.s8.core.web.manganese.mail.MessagingException;
import com.s8.core.web.manganese.mail.smtp.SMTPTransport;
import com.s8.core.web.manganese.mail.util.ASCIIUtility;
import com.s8.core.web.manganese.mail.util.BASE64EncoderStream;

/**
 * Perform the authentication handshake for XOAUTH2 authentication.
 */
public class OAuth2Authenticator extends Authenticator {

	
	
	public OAuth2Authenticator(SMTPTransport transport) {
		super(transport, "XOAUTH2", false);	// disabled by default
	}

	
	@Override
	String getInitialResponse(String host, String authzid, String user,
			String passwd) throws MessagingException, IOException {
		String resp = "user=" + user + "\001auth=Bearer " +
				passwd + "\001\001";
		byte[] b = BASE64EncoderStream.encode(
				resp.getBytes(StandardCharsets.UTF_8));
		return ASCIIUtility.toString(b);
	}
	
	

	@Override
	void doAuth(String host, String authzid, String user, String passwd)
			throws MessagingException, IOException {
		// should never get here
		throw new AuthenticationFailedException("OAUTH2 asked for more");
	}
}