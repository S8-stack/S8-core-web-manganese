package com.s8.core.web.manganese.sasl;

import java.io.IOException;

import com.s8.core.web.manganese.smtp.SMTP_Connection;

public abstract class SASL_Authenticator {

	
	/**
	 * 
	 * @param connection
	 * @return
	 */
	public abstract void authenticate(SMTP_Connection connection) throws IOException;
	
}
