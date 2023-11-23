/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.s8.core.web.manganese.mail;

import java.util.Vector;

import com.s8.core.web.manganese.mail.event.ConnectionListener;
import com.s8.core.web.manganese.mail.smtp.SMTP_ConnectionParams;

/**
 * An abstract class that contains the functionality
 * common to messaging services, such as stores and transports. <p>
 * A messaging service is created from a <code>Session</code> and is
 * named using a <code>URLName</code>.  A service must be connected
 * before it can be used.  Connection events are sent to reflect
 * its connection status.
 *
 * @author Christopher Cotton
 * @author Bill Shannon
 * @author Kanwar Oberoi
 */

public abstract class MnService implements AutoCloseable {

	/**
	 * The session from which this service was created.
	 */
	// protected Session	session;

	/**
	 * The <code>URLName</code> of this service.
	 */

	protected String host;

	protected int port;

	protected String username;

	protected String password;



	/**
	 * Debug flag for this service.  Set from the session's debug
	 * flag when this service is created.
	 */
	protected boolean	debug = false;

	private boolean	connected = false;

	/*
	 * connectionListeners is a Vector, initialized here,
	 * because we depend on it always existing and depend
	 * on the synchronization that Vector provides.
	 * (Sychronizing on the Service object itself can cause
	 * deadlocks when notifying listeners.)
	 */
	private final Vector<ConnectionListener> connectionListeners
	= new Vector<>();

	/**
	 * The queue of events to be delivered.
	 */
	// private final EventQueue q;

	/**
	 * Constructor.
	 *
	 * @param	session Session object for this service
	 * @param	urlname	URLName object to be used for this service
	 */
	protected MnService(SMTP_ConnectionParams params) {
		super();

		this.host = params.getHost();
		this.port = params.getPort();
		this.username = params.getUsername();
		this.password = params.getPassword();




	}



	/**
	 * Similar to connect(host, user, password) except a specific port
	 * can be specified.
	 *
	 * @param host 	the host to connect to
	 * @param port	the port to connect to (-1 means the default port)
	 * @param user	the user name
	 * @param password	this user's password
	 * @exception AuthenticationFailedException	for authentication failures
	 * @exception MessagingException		for other failures
	 * @exception IllegalStateException	if the service is already connected
	 * @see #connect(java.lang.String, java.lang.String, java.lang.String)
	 * @see com.s8.core.web.manganese.mail.event.ConnectionEvent
	 */
	public synchronized void connect() throws MessagingException {

		// see if the service is already connected
		if (isConnected())
			throw new IllegalStateException("already connected");

		PasswordAuthentication pw;
		boolean connected = false;
		boolean save = false;
		String protocol = null;
		String file = null;




		// try connecting, if the protocol needs some missing
		// information (user, password) it will not connect.
		// if it tries to connect and fails, remember why for later.
		AuthenticationFailedException authEx = null;
		try {
			connected = protocolConnect();
		} catch (AuthenticationFailedException ex) {
			authEx = ex;
		}




		//setURLName(new URLName(protocol, host, port, file, user, password));

		/*
	if (save)
	    session.setPasswordAuthentication(getURLName(),
			    new PasswordAuthentication(user, password));
		 */

		// set our connected state
		setConnected(true);
	}


	/**
	 * The service implementation should override this method to
	 * perform the actual protocol-specific connection attempt.
	 * The default implementation of the <code>connect</code> method
	 * calls this method as needed. <p>
	 *
	 * The <code>protocolConnect</code> method should return
	 * <code>false</code> if a user name or password is required
	 * for authentication but the corresponding parameter is null;
	 * the <code>connect</code> method will prompt the user when
	 * needed to supply missing information.  This method may
	 * also return <code>false</code> if authentication fails for
	 * the supplied user name or password.  Alternatively, this method
	 * may throw an AuthenticationFailedException when authentication
	 * fails.  This exception may include a String message with more
	 * detail about the failure. <p>
	 *
	 * The <code>protocolConnect</code> method should throw an
	 * exception to report failures not related to authentication,
	 * such as an invalid host name or port number, loss of a
	 * connection during the authentication process, unavailability
	 * of the server, etc.
	 *
	 * @param	host		the name of the host to connect to
	 * @param	port		the port to use (-1 means use default port)
	 * @param	user		the name of the user to login as
	 * @param	password	the user's password
	 * @return	true if connection successful, false if authentication failed
	 * @exception AuthenticationFailedException	for authentication failures
	 * @exception MessagingException	for non-authentication failures
	 */
	protected abstract boolean protocolConnect() throws MessagingException;

	/**
	 * Is this service currently connected? <p>
	 *
	 * This implementation uses a private boolean field to 
	 * store the connection state. This method returns the value
	 * of that field. <p>
	 *
	 * Subclasses may want to override this method to verify that any
	 * connection to the message store is still alive.
	 *
	 * @return	true if the service is connected, false if it is not connected
	 */
	public synchronized boolean isConnected() {
		return connected;
	}

	/**
	 * Set the connection state of this service.  The connection state
	 * will automatically be set by the service implementation during the
	 * <code>connect</code> and <code>close</code> methods.
	 * Subclasses will need to call this method to set the state
	 * if the service was automatically disconnected. <p>
	 *
	 * The implementation in this class merely sets the private field
	 * returned by the <code>isConnected</code> method.
	 *
	 * @param connected true if the service is connected,
	 *                  false if it is not connected
	 */
	protected synchronized void setConnected(boolean connected) {
		this.connected = connected;
	}

	/**
	 * Close this service and terminate its connection. A close
	 * ConnectionEvent is delivered to any ConnectionListeners. Any
	 * Messaging components (Folders, Messages, etc.) belonging to this
	 * service are invalid after this service is closed. Note that the service
	 * is closed even if this method terminates abnormally by throwing
	 * a MessagingException. <p>
	 *
	 * This implementation uses <code>setConnected(false)</code> to set
	 * this service's connected state to <code>false</code>. It will then
	 * send a close ConnectionEvent to any registered ConnectionListeners.
	 * Subclasses overriding this method to do implementation specific
	 * cleanup should call this method as a last step to insure event
	 * notification, probably by including a call to <code>super.close()</code>
	 * in a <code>finally</code> clause.
	 *
	 * @see com.s8.core.web.manganese.mail.event.ConnectionEvent
	 * @throws	MessagingException	for errors while closing
	 */
	public synchronized void close() throws MessagingException {
		setConnected(false);
	}

	

	


	/**
	 * Return <code>getURLName.toString()</code> if this service has a URLName,
	 * otherwise it will return the default <code>toString</code>.
	 */
	@Override
	public String toString() {
		return host;
	}



	/**
	 * Stop the event dispatcher thread so the queue can be garbage collected.
	 */
	@Override
	protected void finalize() throws Throwable {
		try {
			
		} finally {
			super.finalize();
		}
	}

}
