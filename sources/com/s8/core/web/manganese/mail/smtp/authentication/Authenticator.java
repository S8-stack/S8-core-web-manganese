package com.s8.core.web.manganese.mail.smtp.authentication;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;

import com.s8.core.web.manganese.mail.AuthenticationFailedException;
import com.s8.core.web.manganese.mail.MessagingException;
import com.s8.core.web.manganese.mail.smtp.SMTPTransport;

/**
 * Abstract base class for SMTP authentication mechanism implementations.
 */
public abstract class Authenticator {
	
	public final SMTPTransport transport;
	
	protected int resp;	// the response code, used by subclasses
	
	private final String mech; // the mechanism name, set in the constructor
	
	private final boolean enabled; // is this mechanism enabled by default?

	Authenticator(SMTPTransport transport, String mech) {
		this(transport, mech, true);
	}

	Authenticator(SMTPTransport transport, String mech, boolean enabled) {
		this.transport = transport;
		this.mech = mech.toUpperCase(Locale.ENGLISH);
		this.enabled = enabled;
	}

	public String getMechanism() {
		return mech;
	}

	public boolean enabled() {
		return enabled;
	}

	/**
	 * Start the authentication handshake by issuing the AUTH command.
	 * Delegate to the doAuth method to do the mechanism-specific
	 * part of the handshake.
	 */
	public boolean authenticate(String host, String authzid,
			String user, String passwd) throws MessagingException {
		Throwable thrown = null;
		try {
			// use "initial response" capability, if supported
			String ir = getInitialResponse(host, authzid, user, passwd);
			if (transport.props.noauthdebug && transport.isTracing()) {
				transport.logger.fine("AUTH " + mech + " command trace suppressed");
				transport.suspendTracing();
			}
			if (ir != null)
				resp = transport.simpleCommand("AUTH " + mech + " " +
						(ir.length() == 0 ? "=" : ir));
			else
				resp = transport.simpleCommand("AUTH " + mech);

			/*
			 * A 530 response indicates that the server wants us to
			 * issue a STARTTLS command first.  Do that and try again.
			 */
			if (resp == 530) {
				transport.startTLS();
				if (ir != null)
					resp = transport.simpleCommand("AUTH " + mech + " " + ir);
				else
					resp = transport.simpleCommand("AUTH " + mech);
			}
			if (resp == 334)
				doAuth(host, authzid, user, passwd);
		} catch (IOException ex) {	// should never happen, ignore
			transport.logger.log(Level.FINE, "AUTH " + mech + " failed", ex);
		} catch (Throwable t) {	// crypto can't be initialized?
			transport.logger.log(Level.FINE, "AUTH " + mech + " failed", t);
			thrown = t;
		} finally {
			if (transport.props.noauthdebug && transport.isTracing())
				transport.logger.fine("AUTH " + mech + " " +
						(resp == 235 ? "succeeded" : "failed"));
			transport.resumeTracing();
			if (resp != 235) {
				transport.closeConnection();
				if (thrown != null) {
					if (thrown instanceof Error)
						throw (Error)thrown;
					if (thrown instanceof Exception)
						throw new AuthenticationFailedException(
								transport.getLastServerResponse(),
								(Exception)thrown);
					assert false : "unknown Throwable";	// can't happen
				}
				throw new AuthenticationFailedException(transport.getLastServerResponse());
			}
		}
		return true;
	}

	/**
	 * Provide the initial response to use in the AUTH command,
	 * or null if not supported.  Subclasses that support the
	 * initial response capability will override this method.
	 */
	String getInitialResponse(String host, String authzid, String user,
			String passwd) throws MessagingException, IOException {
		return null;
	}

	abstract void doAuth(String host, String authzid, String user,
			String passwd) throws MessagingException, IOException;
}
