package com.s8.core.web.manganese.sasl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.s8.core.web.manganese.MgServerException;
import com.s8.core.web.manganese.smtp.SMTP_Connection;
import com.s8.core.web.manganese.smtp.SMTP_Reply;
import com.s8.core.web.manganese.utilities.Base64Encoder;

public class SASL_PlainAuthenticator extends SASL_Authenticator {
	
	
	public final static int SUPPORTED_AUTHENTICATION_PROTOCOL = 334;
	
	public final static int AUTHENTICATION_SUCCESSFUL_CODE = 235;
	
	public final static char ZERO_CHAR = '\0';
	
	public final String username;
	
	public final String password;
	
	
	
	
	

	public SASL_PlainAuthenticator(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}



	@Override
	public void authenticate(SMTP_Connection connection) throws IOException {
		
		/**
		C: AUTH PLAIN
		S: 334
		C: dGVzdAB0ZXN0ADEyMzQ=
		*/
		
		connection.sendCommand("AUTH PLAIN");
		SMTP_Reply reply = connection.receiveReply();
		if(reply.code != SUPPORTED_AUTHENTICATION_PROTOCOL) { throw new IOException("Unsupported authentication protocol"); }
		
		
		String credentials = ZERO_CHAR + username + ZERO_CHAR + password;
		byte[] payload = credentials.getBytes(StandardCharsets.US_ASCII);
		byte[] encode = Base64Encoder.encode(payload);
		String command = new String(encode, StandardCharsets.US_ASCII);
		connection.sendCommand(command);
				
		// S: 235 2.7.0 Authentication successful
		reply = connection.receiveReply();
		if(reply.code != AUTHENTICATION_SUCCESSFUL_CODE) {
			throw new MgServerException(reply);
		}
	}

}
