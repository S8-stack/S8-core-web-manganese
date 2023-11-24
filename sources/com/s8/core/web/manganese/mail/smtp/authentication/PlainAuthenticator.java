package com.s8.core.web.manganese.mail.smtp.authentication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.s8.core.web.manganese.mail.AuthenticationFailedException;
import com.s8.core.web.manganese.mail.MessagingException;
import com.s8.core.web.manganese.mail.smtp.SMTPTransport;
import com.s8.core.web.manganese.mail.util.ASCIIUtility;
import com.s8.core.web.manganese.mail.util.BASE64EncoderStream;


/**
 * Perform the authentication handshake for PLAIN authentication.
 */
public class PlainAuthenticator extends Authenticator {


	public PlainAuthenticator(SMTPTransport transport) {
		super(transport, "PLAIN");
	}

	@Override
	String getInitialResponse(String host, String authzid, String user,
			String passwd) throws MessagingException, IOException {
		// return "authzid<NUL>user<NUL>passwd"
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		OutputStream b64os = new BASE64EncoderStream(bos, Integer.MAX_VALUE);
		
		if (authzid != null)
			b64os.write(authzid.getBytes(StandardCharsets.UTF_8));
		b64os.write(0);
		b64os.write(user.getBytes(StandardCharsets.UTF_8));
		b64os.write(0);
		b64os.write(passwd.getBytes(StandardCharsets.UTF_8));
		b64os.flush(); 	// complete the encoding
		
		b64os.close();

		return ASCIIUtility.toString(bos.toByteArray());
	}

	@Override
	void doAuth(String host, String authzid, String user, String passwd)
			throws MessagingException, IOException {
		// should never get here
		throw new AuthenticationFailedException("PLAIN asked for more");
	}

}
