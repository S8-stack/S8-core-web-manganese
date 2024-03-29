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

package com.s8.core.web.manganese.legacy.javax.mail.util;

import java.io.IOException;

/**
 * An IOException that indicates a socket connection attempt failed.
 * Unlike java.net.ConnectException, it includes details of what we
 * were trying to connect to.
 *
 * @see		java.net.ConnectException
 * @author	Bill Shannon
 * @since 	JavaMail 1.5.0
 */

public class SocketConnectException extends IOException {
    /**
     * The socket host name.
     */
    private String host;
    /**
     * The socket port.
     */
    private int port;
    /**
     * The connection timeout.
     */
    private int cto;
    /**
     * The generated serial id.
     */
    private static final long serialVersionUID = 3997871560538755463L;

    /**
     * Constructs a SocketConnectException.
     *
     * @param	msg	error message detail
     * @param	cause	the underlying exception that indicates the failure
     * @param	host	the host we were trying to connect to
     * @param	port	the port we were trying to connect to
     * @param	cto	the timeout for the connection attempt
     */
    public SocketConnectException(String msg, Exception cause,
				    String host, int port, int cto) {
	super(msg);
	initCause(cause);
	this.host = host;
	this.port = port;
	this.cto = cto;
    }

    /**
     * The exception that caused the failure.
     *
     * @return	the exception
     */
    public Exception getException() {
	// the "cause" is always an Exception; see constructor above
	Throwable t = getCause();
	assert t == null || t instanceof Exception;
	return (Exception) t;
    }

    /**
     * The host we were trying to connect to.
     *
     * @return	the host
     */
    public String getHost() {
	return host;
    }

    /**
     * The port we were trying to connect to.
     *
     * @return	the port
     */
    public int getPort() {
	return port;
    }

    /**
     * The timeout used for the connection attempt.
     *
     * @return	the connection timeout
     */
    public int getConnectionTimeout() {
	return cto;
    }
}
