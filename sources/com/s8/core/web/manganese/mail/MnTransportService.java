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

import com.s8.core.web.manganese.mail.event.TransportEvent;
import com.s8.core.web.manganese.mail.event.TransportListener;

/**
 * An abstract class that models a message transport.
 * Subclasses provide actual implementations. <p>
 *
 * Note that <code>Transport</code> extends the <code>Service</code>
 * class, which provides many common methods for naming transports,
 * connecting to transports, and listening to connection events.
 *
 * @author John Mani
 * @author Max Spivak
 * @author Bill Shannon
 * 
 * @see com.s8.core.web.manganese.mail.MnService
 * @see com.s8.core.web.manganese.mail.event.ConnectionEvent
 * @see com.s8.core.web.manganese.mail.event.TransportEvent
 */

public abstract class MnTransportService extends MnService {

	/**
	 * Constructor.
	 *
	 * @param	session Session object for this Transport.
	 * @param	urlname	URLName object to be used for this Transport
	 */
	public MnTransportService(Session session, URLName urlname) {
		super(session, urlname);
	}


	/**
	 * Send the Message to the specified list of addresses. An appropriate
	 * TransportEvent indicating the delivery status is delivered to any 
	 * TransportListener registered on this Transport. Also, if any of
	 * the addresses is invalid, a SendFailedException is thrown.
	 * Whether or not the message is still sent succesfully to
	 * any valid addresses depends on the Transport implementation. <p>
	 *
	 * Unlike the static <code>send</code> method, the <code>sendMessage</code>
	 * method does <em>not</em> call the <code>saveChanges</code> method on
	 * the message; the caller should do so.
	 *
	 * @param msg	The Message to be sent
	 * @param addresses	array of addresses to send this message to
	 * @see 		com.s8.core.web.manganese.mail.event.TransportEvent
	 * @exception SendFailedException if the send failed because of
	 *			invalid addresses.
	 * @exception MessagingException if the connection is dead or not in the 
	 * 				connected state
	 */
	public abstract void sendMessage(Message msg, String[] addresses) 
			throws MessagingException;

	// Vector of Transport listeners
	private volatile Vector<TransportListener> transportListeners = null;

	/**
	 * Add a listener for Transport events. <p>
	 *
	 * The default implementation provided here adds this listener
	 * to an internal list of TransportListeners.
	 *
	 * @param l         the Listener for Transport events
	 * @see             com.s8.core.web.manganese.mail.event.TransportEvent
	 */
	public synchronized void addTransportListener(TransportListener l) {
		if (transportListeners == null)
			transportListeners = new Vector<>();
		transportListeners.addElement(l);
	}

	/**
	 * Remove a listener for Transport events. <p>
	 *
	 * The default implementation provided here removes this listener
	 * from the internal list of TransportListeners.
	 *
	 * @param l         the listener
	 * @see             #addTransportListener
	 */
	public synchronized void removeTransportListener(TransportListener l) {
		if (transportListeners != null)
			transportListeners.removeElement(l);
	}

	/**
	 * Notify all TransportListeners. Transport implementations are
	 * expected to use this method to broadcast TransportEvents.<p>
	 *
	 * The provided default implementation queues the event into
	 * an internal event queue. An event dispatcher thread dequeues
	 * events from the queue and dispatches them to the registered
	 * TransportListeners. Note that the event dispatching occurs
	 * in a separate thread, thus avoiding potential deadlock problems.
	 *
	 * @param	type	the TransportEvent type
	 * @param	validSent valid addresses to which message was sent
	 * @param	validUnsent valid addresses to which message was not sent
	 * @param	invalid the invalid addresses
	 * @param	msg	the message
	 */
	protected void notifyTransportListeners(int type, String[] validSent,
			String[] validUnsent,
			String[] invalid, Message msg) {
		if (transportListeners == null)
			return;

		TransportEvent e = new TransportEvent(this, type, validSent, 
				validUnsent, invalid, msg);
		queueEvent(e, transportListeners);
	}
}
