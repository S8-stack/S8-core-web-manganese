package com.s8.core.web.manganese.sasl;

import com.s8.core.io.xml.annotations.XML_Type;

@XML_Type(name = "MgAuthenticationConfig", sub = {
		MgAuthenticationToken.class
})
public abstract class MgAuthenticationConfig {

	
	public abstract SASL_Authenticator generateAuthenticator();
}
