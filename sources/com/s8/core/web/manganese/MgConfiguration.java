package com.s8.core.web.manganese;

import com.s8.core.io.xml.annotations.XML_SetElement;
import com.s8.core.io.xml.annotations.XML_Type;
import com.s8.core.web.manganese.sasl.MgAuthenticationConfig;

@XML_Type(name = "ManganeseWebServiceConfiguration")
public class MgConfiguration {

	public String host;
	
	public int port;
	
	public String[] tunnelingProtocols = new String[] { "TLSv1.3" };

	public MgAuthenticationConfig authenticationConfig;

	public String senderEmail;
	
	public String senderName;
	
	public String CSS_pathname;

	public boolean isVerbose;

	@XML_SetElement(tag = "host")
	public void setMailServer(String host) { this.host = host; }

	@XML_SetElement(tag = "port")
	public void setPort(int port) { this.port = port; }
	
	@XML_SetElement(tag = "tunneling-protocols")
	public void setTunnelingProtocols(String protocols) { this.tunnelingProtocols = protocols.split(" *, *"); }
	
	@XML_SetElement(tag = "authentication")
	public void setAuthentication(MgAuthenticationConfig config) { this.authenticationConfig = config; }

	@XML_SetElement(tag = "sender-email")
	public void setDefaultSenderEmail(String email) { this.senderEmail = email; }
	
	@XML_SetElement(tag = "sender-name")
	public void setDefualtDisplayname(String name) { this.senderName = name; }
	
	@XML_SetElement(tag = "CSS-style-pathname")
	public void setCSSStyle(String pathname) { this.CSS_pathname = pathname; }

	@XML_SetElement(tag = "is-verbose")
	public void setVerbosity(boolean isVerbose) { this.isVerbose = isVerbose; }


	/**
	 * 
	 */
	public MgConfiguration() {
		super();
	}
}
