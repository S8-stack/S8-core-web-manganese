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

package com.s8.core.web.manganese.legacy.javax.mail.imap.protocol;

import java.util.ArrayList;
import java.util.List;

import com.s8.core.web.manganese.legacy.javax.mail.iap.ProtocolException;
import com.s8.core.web.manganese.legacy.javax.mail.iap.Response;

/**
 * This class and its inner class represent the response to the
 * NAMESPACE command. <p>
 *
 * See <A HREF="http://www.ietf.org/rfc/rfc2342.txt">RFC 2342</A>.
 *
 * @author Bill Shannon
 */

public class Namespaces {

    /**
     * A single namespace entry.
     */
    public static class Namespace {
	/**
	 * Prefix string for the namespace.
	 */
	public String prefix;

	/**
	 * Delimiter between names in this namespace.
	 */
	public char delimiter;

	/**
	 * Parse a namespace element out of the response.
	 *
	 * @param	r	the Response to parse
	 * @exception	ProtocolException	for any protocol errors
	 */
	public Namespace(Response r) throws ProtocolException {
	    // Namespace_Element = "(" string SP (<"> QUOTED_CHAR <"> / nil)
	    //		*(Namespace_Response_Extension) ")"
	    if (!r.isNextNonSpace('('))
		throw new ProtocolException(
					"Missing '(' at start of Namespace");
	    // first, the prefix
	    prefix = r.readString();
	    if (!r.supportsUtf8())
		prefix = BASE64MailboxDecoder.decode(prefix);
	    r.skipSpaces();
	    // delimiter is a quoted character or NIL
	    if (r.peekByte() == '"') {
		r.readByte();
		delimiter = (char)r.readByte();
		if (delimiter == '\\')
		    delimiter = (char)r.readByte();
		if (r.readByte() != '"')
		    throw new ProtocolException(
				    "Missing '\"' at end of QUOTED_CHAR");
	    } else {
		String s = r.readAtom();
		if (s == null)
		    throw new ProtocolException("Expected NIL, got null");
		if (!s.equalsIgnoreCase("NIL"))
		    throw new ProtocolException("Expected NIL, got " + s);
		delimiter = 0;
	    }
	    // at end of Namespace data?
	    if (r.isNextNonSpace(')'))
		return;

	    // otherwise, must be a Namespace_Response_Extension
	    //    Namespace_Response_Extension = SP string SP
	    //	    "(" string *(SP string) ")"
	    r.readString();
	    r.skipSpaces();
	    r.readStringList();
	    if (!r.isNextNonSpace(')'))
		throw new ProtocolException("Missing ')' at end of Namespace");
	}
    };

    /**
     * The personal namespaces.
     * May be null.
     */
    public Namespace[] personal;

    /**
     * The namespaces for other users.
     * May be null.
     */
    public Namespace[] otherUsers;

    /**
     * The shared namespace.
     * May be null.
     */
    public Namespace[] shared;

    /**
     * Parse out all the namespaces.
     *
     * @param	r	the Response to parse
     * @throws	ProtocolException	for any protocol errors
     */
    public Namespaces(Response r) throws ProtocolException {
	personal = getNamespaces(r);
	otherUsers = getNamespaces(r);
	shared = getNamespaces(r);
    }

    /**
     * Parse out one of the three sets of namespaces.
     */
    private Namespace[] getNamespaces(Response r) throws ProtocolException {
	//    Namespace = nil / "(" 1*( Namespace_Element) ")"
	if (r.isNextNonSpace('(')) {
	    List<Namespace> v = new ArrayList<>();
	    do {
		Namespace ns = new Namespace(r);
		v.add(ns);
	    } while (!r.isNextNonSpace(')'));
	    return v.toArray(new Namespace[v.size()]);
	} else {
	    String s = r.readAtom();
	    if (s == null)
		throw new ProtocolException("Expected NIL, got null");
	    if (!s.equalsIgnoreCase("NIL"))
		throw new ProtocolException("Expected NIL, got " + s);
	    return null;
	}
    }
}
