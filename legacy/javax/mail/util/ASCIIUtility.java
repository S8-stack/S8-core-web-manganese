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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;

public class ASCIIUtility {

    // Private constructor so that this class is not instantiated
    private ASCIIUtility() { }
	
    /**
     * Convert the bytes within the specified range of the given byte 
     * array into a signed integer in the given radix . The range extends 
     * from <code>start</code> till, but not including <code>end</code>. <p>
     *
     * Based on java.lang.Integer.parseInt()
     *
     * @param	b	the bytes
     * @param	start	the first byte offset
     * @param	end	the last byte offset
     * @param	radix	the radix
     * @return		the integer value
     * @exception	NumberFormatException	for conversion errors
     */
    public static int parseInt(byte[] b, int start, int end, int radix)
		throws NumberFormatException {
	if (b == null)
	    throw new NumberFormatException("null");
	
	int result = 0;
	boolean negative = false;
	int i = start;
	int limit;
	int multmin;
	int digit;

	if (end > start) {
	    if (b[i] == '-') {
		negative = true;
		limit = Integer.MIN_VALUE;
		i++;
	    } else {
		limit = -Integer.MAX_VALUE;
	    }
	    multmin = limit / radix;
	    if (i < end) {
		digit = Character.digit((char)b[i++], radix);
		if (digit < 0) {
		    throw new NumberFormatException(
			"illegal number: " + toString(b, start, end)
			);
		} else {
		    result = -digit;
		}
	    }
	    while (i < end) {
		// Accumulating negatively avoids surprises near MAX_VALUE
		digit = Character.digit((char)b[i++], radix);
		if (digit < 0) {
		    throw new NumberFormatException("illegal number");
		}
		if (result < multmin) {
		    throw new NumberFormatException("illegal number");
		}
		result *= radix;
		if (result < limit + digit) {
		    throw new NumberFormatException("illegal number");
		}
		result -= digit;
	    }
	} else {
	    throw new NumberFormatException("illegal number");
	}
	if (negative) {
	    if (i > start + 1) {
		return result;
	    } else {	/* Only got "-" */
		throw new NumberFormatException("illegal number");
	    }
	} else {
	    return -result;
	}
    }

    /**
     * Convert the bytes within the specified range of the given byte 
     * array into a signed integer . The range extends from 
     * <code>start</code> till, but not including <code>end</code>.
     *
     * @param	b	the bytes
     * @param	start	the first byte offset
     * @param	end	the last byte offset
     * @return		the integer value
     * @exception	NumberFormatException	for conversion errors
     */
    public static int parseInt(byte[] b, int start, int end)
		throws NumberFormatException {
	return parseInt(b, start, end, 10);
    }

    /**
     * Convert the bytes within the specified range of the given byte 
     * array into a signed long in the given radix . The range extends 
     * from <code>start</code> till, but not including <code>end</code>. <p>
     *
     * Based on java.lang.Long.parseLong()
     *
     * @param	b	the bytes
     * @param	start	the first byte offset
     * @param	end	the last byte offset
     * @param	radix	the radix
     * @return		the long value
     * @exception	NumberFormatException	for conversion errors
     */
    public static long parseLong(byte[] b, int start, int end, int radix)
		throws NumberFormatException {
	if (b == null)
	    throw new NumberFormatException("null");
	
	long result = 0;
	boolean negative = false;
	int i = start;
	long limit;
	long multmin;
	int digit;

	if (end > start) {
	    if (b[i] == '-') {
		negative = true;
		limit = Long.MIN_VALUE;
		i++;
	    } else {
		limit = -Long.MAX_VALUE;
	    }
	    multmin = limit / radix;
	    if (i < end) {
		digit = Character.digit((char)b[i++], radix);
		if (digit < 0) {
		    throw new NumberFormatException(
			"illegal number: " + toString(b, start, end)
			);
		} else {
		    result = -digit;
		}
	    }
	    while (i < end) {
		// Accumulating negatively avoids surprises near MAX_VALUE
		digit = Character.digit((char)b[i++], radix);
		if (digit < 0) {
		    throw new NumberFormatException("illegal number");
		}
		if (result < multmin) {
		    throw new NumberFormatException("illegal number");
		}
		result *= radix;
		if (result < limit + digit) {
		    throw new NumberFormatException("illegal number");
		}
		result -= digit;
	    }
	} else {
	    throw new NumberFormatException("illegal number");
	}
	if (negative) {
	    if (i > start + 1) {
		return result;
	    } else {	/* Only got "-" */
		throw new NumberFormatException("illegal number");
	    }
	} else {
	    return -result;
	}
    }

    /**
     * Convert the bytes within the specified range of the given byte 
     * array into a signed long . The range extends from 
     * <code>start</code> till, but not including <code>end</code>. <p>
     *
     * @param	b	the bytes
     * @param	start	the first byte offset
     * @param	end	the last byte offset
     * @return		the long value
     * @exception	NumberFormatException	for conversion errors
     */
    public static long parseLong(byte[] b, int start, int end)
		throws NumberFormatException {
	return parseLong(b, start, end, 10);
    }

    /**
     * Convert the bytes within the specified range of the given byte 
     * array into a String. The range extends from <code>start</code>
     * till, but not including <code>end</code>.
     *
     * @param	b	the bytes
     * @param	start	the first byte offset
     * @param	end	the last byte offset
     * @return		the String
     */
    public static String toString(byte[] b, int start, int end) {
	int size = end - start;
	char[] theChars = new char[size];

	for (int i = 0, j = start; i < size; )
	    theChars[i++] = (char)(b[j++]&0xff);
	
	return new String(theChars);
    }

    /**
     * Convert the bytes into a String.
     *
     * @param	b	the bytes
     * @return		the String
     * @since	JavaMail 1.4.4
     */
    public static String toString(byte[] b) {
	return toString(b, 0, b.length);
    }

    public static String toString(ByteArrayInputStream is) {
	int size = is.available();
	char[] theChars = new char[size];
	byte[] bytes    = new byte[size];

	is.read(bytes, 0, size);
	for (int i = 0; i < size;)
	    theChars[i] = (char)(bytes[i++]&0xff);
	
	return new String(theChars);
    }


    public static byte[] getBytes(String s) {
	char [] chars= s.toCharArray();
	int size = chars.length;
	byte[] bytes = new byte[size];
    	
	for (int i = 0; i < size;)
	    bytes[i] = (byte) chars[i++];
	return bytes;
    }

    public static byte[] getBytes(InputStream is) throws IOException {

	int len;
	int size = 1024;
	byte [] buf;


	if (is instanceof ByteArrayInputStream) {
	    size = is.available();
	    buf = new byte[size];
	    len = is.read(buf, 0, size);
	}
	else {
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    buf = new byte[size];
	    while ((len = is.read(buf, 0, size)) != -1)
		bos.write(buf, 0, len);
	    buf = bos.toByteArray();
	}
	return buf;
    }
}
