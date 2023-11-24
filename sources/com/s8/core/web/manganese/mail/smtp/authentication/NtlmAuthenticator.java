package com.s8.core.web.manganese.mail.smtp.authentication;

import java.io.IOException;

import com.s8.core.web.manganese.mail.MessagingException;
import com.s8.core.web.manganese.mail.auth.Ntlm;
import com.s8.core.web.manganese.mail.smtp.SMTPTransport;

/**
 * Perform the authentication handshake for NTLM authentication.
 */
public class NtlmAuthenticator extends Authenticator {


	private Ntlm ntlm;
	private int flags = 0;

	public NtlmAuthenticator(SMTPTransport transport) {
		super(transport, "NTLM");
	}

	@Override
	String getInitialResponse(String host, String authzid, String user,
			String passwd) throws MessagingException, IOException {
		ntlm = new Ntlm(transport.getNTLMDomain(), transport.getLocalHost(),
				user, passwd, transport.logger);

		String type1 = ntlm.generateType1Msg(flags);
		return type1;
	}

	@Override
	void doAuth(String host, String authzid, String user, String passwd)
			throws MessagingException, IOException {
		assert ntlm != null;
		String type3 = ntlm.generateType3Msg(
				transport.getLastServerResponse().substring(4).trim());

		resp = transport.simpleCommand(type3);
	}
}