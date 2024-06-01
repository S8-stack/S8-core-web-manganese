package com.s8.core.web.manganese.sasl;

import com.s8.core.io.xml.annotations.XML_SetElement;
import com.s8.core.io.xml.annotations.XML_Type;


@XML_Type(name = "MgAuthenticationToken")
public class MgAuthenticationToken extends MgAuthenticationConfig {
	
	private String publicKey;
	
	private String privateKey;
	
	
	@XML_SetElement(tag = "public-key")
	public void setPublicKey(String key) { this.publicKey = key; }

	@XML_SetElement(tag = "private-key")
	public void setPrivateKey(String key) { this.privateKey = key; }

	@Override
	public SASL_Authenticator generateAuthenticator() {
		return new SASL_PlainAuthenticator(publicKey, privateKey);
	}
}
