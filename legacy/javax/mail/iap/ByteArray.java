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

package com.s8.core.web.manganese.legacy.javax.mail.iap;

import java.io.ByteArrayInputStream;

/**
 * A simple wrapper around a byte array, with a start position and
 * count of bytes.
 *
 * @author  John Mani
 */

public class ByteArray {
    private byte[] bytes; // the byte array
    private int start;	  // start position
    private int count;	  // count of bytes

    /**
     * Constructor
     *
     * @param	b	the byte array to wrap
     * @param	start	start position in byte array
     * @param	count	number of bytes in byte array
     */
    public ByteArray(byte[] b, int start, int count) {
	bytes = b;
	this.start = start;
	this.count = count;
    }

    /**
     * Constructor that creates a byte array of the specified size.
     *
     * @param	size	the size of the ByteArray
     * @since	JavaMail 1.4.1
     */
    public ByteArray(int size) {
	this(new byte[size], 0, size);
    }

    /**
     * Returns the internal byte array. Note that this is a live
     * reference to the actual data, not a copy.
     *
     * @return	the wrapped byte array
     */
    public byte[] getBytes() {
	return bytes;
    }

    /**
     * Returns a new byte array that is a copy of the data.
     *
     * @return	a new byte array with the bytes from start for count
     */
    public byte[] getNewBytes() {
	byte[] b = new byte[count];
	System.arraycopy(bytes, start, b, 0, count);
	return b;
    }

    /**
     * Returns the start position
     *
     * @return	the start position
     */
    public int getStart() {
	return start;
    }

    /**
     * Returns the count of bytes
     *
     * @return	the number of bytes
     */
    public int getCount() {
	return count;
    }

    /**
     * Set the count of bytes.
     *
     * @param	count	the number of bytes
     * @since	JavaMail 1.4.1
     */
    public void setCount(int count) {
	this.count = count;
    }

    /**
     * Returns a ByteArrayInputStream.
     *
     * @return	the ByteArrayInputStream
     */
    public ByteArrayInputStream toByteArrayInputStream() {
	return new ByteArrayInputStream(bytes, start, count);
    }

    /**
     * Grow the byte array by incr bytes.
     *
     * @param	incr	how much to grow
     * @since	JavaMail 1.4.1
     */
    public void grow(int incr) {
	byte[] nbuf = new byte[bytes.length + incr];
	System.arraycopy(bytes, 0, nbuf, 0, bytes.length);
	bytes = nbuf;
    }
}