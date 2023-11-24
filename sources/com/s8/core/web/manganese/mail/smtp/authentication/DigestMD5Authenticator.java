package com.s8.core.web.manganese.mail.smtp.authentication;

import java.io.IOException;

import com.s8.core.web.manganese.mail.MessagingException;
import com.s8.core.web.manganese.mail.smtp.DigestMD5;
import com.s8.core.web.manganese.mail.smtp.SMTPTransport;

/**
 * Perform the authentication handshake for DIGEST-MD5 authentication.
 */
public class DigestMD5Authenticator extends Authenticator {

	
	/**
	 * 
	 */
	private DigestMD5 md5support;	// only create if needed


	public DigestMD5Authenticator(SMTPTransport transport) {
		super(transport, "DIGEST-MD5");
	}

	private synchronized DigestMD5 getMD5() {
		if (md5support == null)
			md5support = new DigestMD5(transport.logger);
		return md5support;
	}

	@Override
	void doAuth(String host, String authzid, String user, String passwd)
			throws MessagingException, IOException {
		DigestMD5 md5 = getMD5();
		assert md5 != null;

		byte[] b = md5.authClient(host, user, passwd, transport.getSASLRealm(),
				transport.getLastServerResponse());
		resp = transport.simpleCommand(b);
		if (resp == 334) { // client authenticated by server
			if (!md5.authServer(transport.getLastServerResponse())) {
				// server NOT authenticated by client !!!
				resp = -1;
			} 
			else {
				// send null response
				resp = transport.simpleCommand(new byte[0]);
			}
		}
	}
}
