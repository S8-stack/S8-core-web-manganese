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

package com.s8.core.web.manganese.legacy.javax.activation.registries;

import java.io.*;
import java.util.*;

public class MimeTypeFile {
    private String fname = null;
    private Hashtable type_hash = new Hashtable();

    /**
     * The construtor that takes a filename as an argument.
     *
     * @param new_fname The file name of the mime types file.
     */
    public MimeTypeFile(String new_fname) throws IOException {
	File mime_file = null;
	FileReader fr = null;

	fname = new_fname; // remember the file name

	mime_file = new File(fname); // get a file object

	fr = new FileReader(mime_file);

	try {
	    parse(new BufferedReader(fr));
	} finally {
	    try {
		fr.close(); // close it
	    } catch (IOException e) {
		// ignore it
	    }
	}
    }

    public MimeTypeFile(InputStream is) throws IOException {
	parse(new BufferedReader(new InputStreamReader(is, "iso-8859-1")));
    }

    /**
     * Creates an empty DB.
     */
    public MimeTypeFile() {
    }

    /**
     * get the MimeTypeEntry based on the file extension
     */
    public MimeTypeEntry getMimeTypeEntry(String file_ext) {
	return (MimeTypeEntry)type_hash.get((Object)file_ext);
    }

    /**
     * Get the MIME type string corresponding to the file extension.
     */
    public String getMIMETypeString(String file_ext) {
	MimeTypeEntry entry = this.getMimeTypeEntry(file_ext);

	if (entry != null)
	    return entry.getMIMEType();
	else
	    return null;
    }

    /**
     * Appends string of entries to the types registry, must be valid
     * .mime.types format.
     * A mime.types entry is one of two forms:
     *
     *	type/subtype	ext1 ext2 ...
     * or
     *	type=type/subtype desc="description of type" exts=ext1,ext2,...
     *
     * Example:
     * # this is a test
     * audio/basic            au
     * text/plain             txt text
     * type=application/postscript exts=ps,eps
     */
    public void appendToRegistry(String mime_types) {
	try {
	    parse(new BufferedReader(new StringReader(mime_types)));
	} catch (IOException ex) {
	    // can't happen
	}
    }

    /**
     * Parse a stream of mime.types entries.
     */
    private void parse(BufferedReader buf_reader) throws IOException {
	String line = null, prev = null;

	while ((line = buf_reader.readLine()) != null) {
	    if (prev == null)
		prev = line;
	    else
		prev += line;
	    int end = prev.length();
	    if (prev.length() > 0 && prev.charAt(end - 1) == '\\') {
		prev = prev.substring(0, end - 1);
		continue;
	    }
	    this.parseEntry(prev);
	    prev = null;
	}
	if (prev != null)
	    this.parseEntry(prev);
    }

    /**
     * Parse single mime.types entry.
     */
    private void parseEntry(String line) {
	String mime_type = null;
	String file_ext = null;
	line = line.trim();

	if (line.length() == 0) // empty line...
	    return; // BAIL!

	// check to see if this is a comment line?
	if (line.charAt(0) == '#')
	    return; // then we are done!

	// is it a new format line or old format?
	if (line.indexOf('=') > 0) {
	    // new format
	    LineTokenizer lt = new LineTokenizer(line);
	    while (lt.hasMoreTokens()) {
		String name = lt.nextToken();
		String value = null;
		if (lt.hasMoreTokens() && lt.nextToken().equals("=") &&
							lt.hasMoreTokens())
		    value = lt.nextToken();
		if (value == null) {
		    if (LogSupport.isLoggable())
			LogSupport.log("Bad .mime.types entry: " + line);
		    return;
		}
		if (name.equals("type"))
		    mime_type = value;
		else if (name.equals("exts")) {
		    StringTokenizer st = new StringTokenizer(value, ",");
		    while (st.hasMoreTokens()) {
			file_ext = st.nextToken();
			MimeTypeEntry entry =
				new MimeTypeEntry(mime_type, file_ext);
			type_hash.put(file_ext, entry);
			if (LogSupport.isLoggable())
			    LogSupport.log("Added: " + entry.toString());
		    }
		}
	    }
	} else {
	    // old format
	    // count the tokens
	    StringTokenizer strtok = new StringTokenizer(line);
	    int num_tok = strtok.countTokens();

	    if (num_tok == 0) // empty line
		return;

	    mime_type = strtok.nextToken(); // get the MIME type

	    while (strtok.hasMoreTokens()) {
		MimeTypeEntry entry = null;

		file_ext = strtok.nextToken();
		entry = new MimeTypeEntry(mime_type, file_ext);
		type_hash.put(file_ext, entry);
		if (LogSupport.isLoggable())
		    LogSupport.log("Added: " + entry.toString());
	    }
	}
    }

    // for debugging
    /*
    public static void main(String[] argv) throws Exception {
	MimeTypeFile mf = new MimeTypeFile(argv[0]);
	System.out.println("ext " + argv[1] + " type " +
						mf.getMIMETypeString(argv[1]));
	System.exit(0);
    }
    */
}

class LineTokenizer {
    private int currentPosition;
    private int maxPosition;
    private String str;
    private Vector stack = new Vector();
    private static final String singles = "=";	// single character tokens

    /**
     * Constructs a tokenizer for the specified string.
     * <p>
     *
     * @param   str            a string to be parsed.
     */
    public LineTokenizer(String str) {
	currentPosition = 0;
	this.str = str;
	maxPosition = str.length();
    }

    /**
     * Skips white space.
     */
    private void skipWhiteSpace() {
	while ((currentPosition < maxPosition) &&
	       Character.isWhitespace(str.charAt(currentPosition))) {
	    currentPosition++;
	}
    }

    /**
     * Tests if there are more tokens available from this tokenizer's string.
     *
     * @return  <code>true</code> if there are more tokens available from this
     *          tokenizer's string; <code>false</code> otherwise.
     */
    public boolean hasMoreTokens() {
	if (stack.size() > 0)
	    return true;
	skipWhiteSpace();
	return (currentPosition < maxPosition);
    }

    /**
     * Returns the next token from this tokenizer.
     *
     * @return     the next token from this tokenizer.
     * @exception  NoSuchElementException  if there are no more tokens in this
     *               tokenizer's string.
     */
    public String nextToken() {
	int size = stack.size();
	if (size > 0) {
	    String t = (String)stack.elementAt(size - 1);
	    stack.removeElementAt(size - 1);
	    return t;
	}
	skipWhiteSpace();

	if (currentPosition >= maxPosition) {
	    throw new NoSuchElementException();
	}

	int start = currentPosition;
	char c = str.charAt(start);
	if (c == '"') {
	    currentPosition++;
	    boolean filter = false;
	    while (currentPosition < maxPosition) {
		c = str.charAt(currentPosition++);
		if (c == '\\') {
		    currentPosition++;
		    filter = true;
		} else if (c == '"') {
		    String s;

		    if (filter) {
			StringBuffer sb = new StringBuffer();
			for (int i = start + 1; i < currentPosition - 1; i++) {
			    c = str.charAt(i);
			    if (c != '\\')
				sb.append(c);
			}
			s = sb.toString();
		    } else
			s = str.substring(start + 1, currentPosition - 1);
		    return s;
		}
	    }
	} else if (singles.indexOf(c) >= 0) {
	    currentPosition++;
	} else {
	    while ((currentPosition < maxPosition) && 
		   singles.indexOf(str.charAt(currentPosition)) < 0 &&
		   !Character.isWhitespace(str.charAt(currentPosition))) {
		currentPosition++;
	    }
	}
	return str.substring(start, currentPosition);
    }

    public void pushToken(String token) {
	stack.addElement(token);
    }
}
