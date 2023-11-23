package com.s8.core.web.manganese.mail.smtp;

import com.s8.core.web.manganese.mail.MnTransportProps;

public class SMTP_TransportProps extends MnTransportProps {
	
	
	public static final String UNKNOWN = "UNKNOWN";	// place holder
	
	public static final String[] UNKNOWN_SA = new String[0]; // place holder
	


	/**
	 * 
	 */
	public boolean auth = true;
	
	
	/** true if we should wait */
	public boolean quitWait = true;
	
	/**
	 * 
	 */
	public String saslRealm = UNKNOWN;
	
	/**
	 * 
	 */
	public String authorizationID = UNKNOWN;
	
	/** enable SASL authentication */
	public boolean enableSASL = false;
	
	/** use canonical host name? */
	public boolean useCanonicalHostName = false;
	
	/** */
	public String[] saslMechanisms = UNKNOWN_SA;

	/** 
	 * for ntlm authentication 
	 */
	public String ntlmDomain = UNKNOWN;

	/**
	 *  throw an exception even on success 
	 */
	public boolean reportSuccess = false;
	
	/** 
	 * use STARTTLS command 
	 */
	public boolean useStartTLS = false;
	
	/** 
	 * require STARTTLS command 
	 */
	public boolean requireStartTLS = false;
	
	/** 
	 * use RSET instead of NOOP 
	 */
	public boolean useRset = false;
	
	/** 
	 * NOOP must return 250 for success 
	 */
	public boolean noopStrict = true;	
	
	
	/** 
	 * hide auth info in debug output 
	 */
	public boolean noauthdebug = true;
	
	
	/** 
	 * include username in debug output? 
	 */
	public boolean debugusername = true;	
	
	/** 
	 * include password in debug output? 
	 */
	public boolean debugpassword = false;	
	
	/** 
	 * Allow UTF-8 usernames and passwords? 
	 */
	public boolean allowutf8 = false;
	
	/** 
	 * chunk size if CHUNKING supported 
	 */
	public int chunkSize = -1;		
	
	
	/**
	 * 
	 */
	public boolean useEhlo = true;
	
	
	
	
	
	
	
	/**
	 * 
	 * @return
	 */
	public SMTP_TransportProps copy() {
		SMTP_TransportProps props = new SMTP_TransportProps();
		
		
		props.debug = debug;
		
		props.auth = auth;
		props.quitWait = quitWait;
		props.saslRealm = saslRealm;
		props.authorizationID = authorizationID;
		props.enableSASL = enableSASL;
		props.useCanonicalHostName = useCanonicalHostName;
		props.saslMechanisms = saslMechanisms;
		props.ntlmDomain = ntlmDomain;
		props.reportSuccess = reportSuccess;
		props.useStartTLS = useStartTLS;
		props.requireStartTLS = requireStartTLS;
		props.useRset = useRset;
		props.noopStrict = noopStrict;
		props.noauthdebug = noauthdebug;
		props.debugusername = debugusername;
		props.debugpassword = debugpassword;
		props.allowutf8 = allowutf8;
		props.chunkSize = chunkSize;
		props.useEhlo = useEhlo;
		return props;
	}
	
	

}
