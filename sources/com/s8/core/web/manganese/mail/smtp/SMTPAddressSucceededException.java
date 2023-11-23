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

package com.s8.core.web.manganese.mail.smtp;

import com.s8.core.web.manganese.mail.MessagingException;

/**
 * This exception is chained off a SendFailedException when the
 * <code>mail.smtp.reportsuccess</code> property is true.  It
 * indicates an address to which the message was sent.  The command
 * will be an SMTP RCPT command and the return code will be the
 * return code from that command.
 *
 * @since JavaMail 1.3.2
 */

public class SMTPAddressSucceededException extends MessagingException {
	
    protected String addr;	// address that succeeded
    protected String cmd;		// command issued to server
    protected int rc;			// return code from SMTP server

    private static final long serialVersionUID = -1168335848623096749L;

    /**
     * Constructs an SMTPAddressSucceededException with the specified 
     * address, return code, and error string.
     *
     * @param addr	the address that succeeded
     * @param cmd	the command that was sent to the SMTP server
     * @param rc	the SMTP return code indicating the success
     * @param err	the error string from the SMTP server
     */
    public SMTPAddressSucceededException(String addr,
				String cmd, int rc, String err) {
	super(err);
	this.addr = addr;
	this.cmd = cmd;
	this.rc = rc;
    }

    /**
     * Return the address that succeeded.
     *
     * @return	the address
     */
    public String getAddress() {
	return addr;
    }

    /**
     * Return the command that succeeded.
     *
     * @return	the command
     */
    public String getCommand() {
	return cmd;
    }


    /**
     * Return the return code from the SMTP server that indicates the
     * reason for the success.  See
     * <A HREF="http://www.ietf.org/rfc/rfc821.txt">RFC 821</A>
     * for interpretation of the return code.
     *
     * @return	the return code
     */
    public int getReturnCode() {
	return rc;
    }
}
