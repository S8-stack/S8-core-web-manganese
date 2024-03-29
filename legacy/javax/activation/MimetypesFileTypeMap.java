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

package com.s8.core.web.manganese.legacy.javax.activation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Vector;

import com.s8.core.web.manganese.legacy.javax.activation.registries.LogSupport;
import com.s8.core.web.manganese.legacy.javax.activation.registries.MimeTypeFile;

/**
 * This class extends FileTypeMap and provides data typing of files
 * via their file extension. It uses the <code>.mime.types</code> format. <p>
 *
 * <b>MIME types file search order:</b><p>
 * The MimetypesFileTypeMap looks in various places in the user's
 * system for MIME types file entries. When requests are made
 * to search for MIME types in the MimetypesFileTypeMap, it searches  
 * MIME types files in the following order:
 * <ol>
 * <li> Programmatically added entries to the MimetypesFileTypeMap instance.
 * <li> The file <code>.mime.types</code> in the user's home directory.
 * <li> The file <code>mime.types</code> in the Java runtime.
 * <li> The file or resources named <code>META-INF/mime.types</code>.
 * <li> The file or resource named <code>META-INF/mimetypes.default</code>
 * (usually found only in the <code>activation.jar</code> file).
 * </ol>
 * <p>
 * (The current implementation looks for the <code>mime.types</code> file
 * in the Java runtime in the directory <code><i>java.home</i>/conf</code>
 * if it exists, and otherwise in the directory
 * <code><i>java.home</i>/lib</code>, where <i>java.home</i> is the value
 * of the "java.home" System property.  Note that the "conf" directory was
 * introduced in JDK 9.)
 * <p>
 * <b>MIME types file format:</b><p>
 *
 * <code>
 * # comments begin with a '#'<br>
 * # the format is &lt;mime type&gt; &lt;space separated file extensions&gt;<br>
 * # for example:<br>
 * text/plain    txt text TXT<br>
 * # this would map file.txt, file.text, and file.TXT to<br>
 * # the mime type "text/plain"<br>
 * </code>
 *
 * @author Bart Calder
 * @author Bill Shannon
 */
@SuppressWarnings("removal")
public class MimetypesFileTypeMap extends FileTypeMap {
    /*
     * We manage a collection of databases, searched in order.
     */
    private MimeTypeFile[] DB;
    private static final int PROG = 0;	// programmatically added entries

    private static final String defaultType = "application/octet-stream";

    private static final String confDir;

    static {
	String dir = null;
	try {
	    dir = (String)AccessController.doPrivileged(
		new PrivilegedAction() {
		    public Object run() {
			String home = System.getProperty("java.home");
			String newdir = home + File.separator + "conf";
			File conf = new File(newdir);
			if (conf.exists())
			    return newdir + File.separator;
			else
			    return home + File.separator + "lib" + File.separator;
		    }
		});
	} catch (Exception ex) {
	    // ignore any exceptions
	}
	confDir = dir;
    }

    /**
     * The default constructor.
     */
    public MimetypesFileTypeMap() {
	Vector dbv = new Vector(5);	// usually 5 or less databases
	MimeTypeFile mf = null;
	dbv.addElement(null);		// place holder for PROG entry

	LogSupport.log("MimetypesFileTypeMap: load HOME");
	try {
	    String user_home = System.getProperty("user.home");

	    if (user_home != null) {
		String path = user_home + File.separator + ".mime.types";
		mf = loadFile(path);
		if (mf != null)
		    dbv.addElement(mf);
	    }
	} catch (SecurityException ex) {}

	LogSupport.log("MimetypesFileTypeMap: load SYS");
	try {
	    // check system's home
	    if (confDir != null) {
		mf = loadFile(confDir + "mime.types");
		if (mf != null)
		    dbv.addElement(mf);
	    }
	} catch (SecurityException ex) {}

	LogSupport.log("MimetypesFileTypeMap: load JAR");
	// load from the app's jar file
	loadAllResources(dbv, "META-INF/mime.types");

	LogSupport.log("MimetypesFileTypeMap: load DEF");
	mf = loadResource("/META-INF/mimetypes.default");

	if (mf != null)
	    dbv.addElement(mf);

	DB = new MimeTypeFile[dbv.size()];
	dbv.copyInto(DB);
    }

    /**
     * Load from the named resource.
     */
    private MimeTypeFile loadResource(String name) {
	InputStream clis = null;
	try {
	    clis = SecuritySupport.getResourceAsStream(this.getClass(), name);
	    if (clis != null) {
		MimeTypeFile mf = new MimeTypeFile(clis);
		if (LogSupport.isLoggable())
		    LogSupport.log("MimetypesFileTypeMap: successfully " +
			"loaded mime types file: " + name);
		return mf;
	    } else {
		if (LogSupport.isLoggable())
		    LogSupport.log("MimetypesFileTypeMap: not loading " +
			"mime types file: " + name);
	    }
	} catch (IOException e) {
	    if (LogSupport.isLoggable())
		LogSupport.log("MimetypesFileTypeMap: can't load " + name, e);
	} catch (SecurityException sex) {
	    if (LogSupport.isLoggable())
		LogSupport.log("MimetypesFileTypeMap: can't load " + name, sex);
	} finally {
	    try {
		if (clis != null)
		    clis.close();
	    } catch (IOException ex) { }	// ignore it
	}
	return null;
    }

    /**
     * Load all of the named resource.
     */
    private void loadAllResources(Vector v, String name) {
	boolean anyLoaded = false;
	try {
	    URL[] urls;
	    ClassLoader cld = null;
	    // First try the "application's" class loader.
	    cld = SecuritySupport.getContextClassLoader();
	    if (cld == null)
		cld = this.getClass().getClassLoader();
	    if (cld != null)
		urls = SecuritySupport.getResources(cld, name);
	    else
		urls = SecuritySupport.getSystemResources(name);
	    if (urls != null) {
		if (LogSupport.isLoggable())
		    LogSupport.log("MimetypesFileTypeMap: getResources");
		for (int i = 0; i < urls.length; i++) {
		    URL url = urls[i];
		    InputStream clis = null;
		    if (LogSupport.isLoggable())
			LogSupport.log("MimetypesFileTypeMap: URL " + url);
		    try {
			clis = SecuritySupport.openStream(url);
			if (clis != null) {
			    v.addElement(new MimeTypeFile(clis));
			    anyLoaded = true;
			    if (LogSupport.isLoggable())
				LogSupport.log("MimetypesFileTypeMap: " +
				    "successfully loaded " +
				    "mime types from URL: " + url);
			} else {
			    if (LogSupport.isLoggable())
				LogSupport.log("MimetypesFileTypeMap: " +
				    "not loading " +
				    "mime types from URL: " + url);
			}
		    } catch (IOException ioex) {
			if (LogSupport.isLoggable())
			    LogSupport.log("MimetypesFileTypeMap: can't load " +
						url, ioex);
		    } catch (SecurityException sex) {
			if (LogSupport.isLoggable())
			    LogSupport.log("MimetypesFileTypeMap: can't load " +
						url, sex);
		    } finally {
			try {
			    if (clis != null)
				clis.close();
			} catch (IOException cex) { }
		    }
		}
	    }
	} catch (Exception ex) {
	    if (LogSupport.isLoggable())
		LogSupport.log("MimetypesFileTypeMap: can't load " + name, ex);
	}

	// if failed to load anything, fall back to old technique, just in case
	if (!anyLoaded) {
	    LogSupport.log("MimetypesFileTypeMap: !anyLoaded");
	    MimeTypeFile mf = loadResource("/" + name);
	    if (mf != null)
		v.addElement(mf);
	}
    }

    /**
     * Load the named file.
     */
    private MimeTypeFile loadFile(String name) {
	MimeTypeFile mtf = null;

	try {
	    mtf = new MimeTypeFile(name);
	} catch (IOException e) {
	    //	e.printStackTrace();
	}
	return mtf;
    }

    /**
     * Construct a MimetypesFileTypeMap with programmatic entries
     * added from the named file.
     *
     * @param mimeTypeFileName	the file name
     * @exception	IOException	for errors reading the file
     */
    public MimetypesFileTypeMap(String mimeTypeFileName) throws IOException {
	this();
	DB[PROG] = new MimeTypeFile(mimeTypeFileName);
    }

    /**
     * Construct a MimetypesFileTypeMap with programmatic entries
     * added from the InputStream.
     *
     * @param is	the input stream to read from
     */
    public MimetypesFileTypeMap(InputStream is) {
	this();
	try {
	    DB[PROG] = new MimeTypeFile(is);
	} catch (IOException ex) {
	    // XXX - really should throw it
	}
    }

    /**
     * Prepend the MIME type values to the registry.
     *
     * @param mime_types A .mime.types formatted string of entries.
     */
    public synchronized void addMimeTypes(String mime_types) {
	// check to see if we have created the registry
	if (DB[PROG] == null)
	    DB[PROG] = new MimeTypeFile(); // make one

	DB[PROG].appendToRegistry(mime_types);
    }

    /**
     * Return the MIME type of the file object.
     * The implementation in this class calls
     * <code>getContentType(f.getName())</code>.
     *
     * @param f	the file
     * @return	the file's MIME type
     */
    public String getContentType(File f) {
	return this.getContentType(f.getName());
    }

    /**
     * Return the MIME type based on the specified file name.
     * The MIME type entries are searched as described above under
     * <i>MIME types file search order</i>.
     * If no entry is found, the type "application/octet-stream" is returned.
     *
     * @param filename	the file name
     * @return		the file's MIME type
     */
    public synchronized String getContentType(String filename) {
	int dot_pos = filename.lastIndexOf("."); // period index

	if (dot_pos < 0)
	    return defaultType;

	String file_ext = filename.substring(dot_pos + 1);
	if (file_ext.length() == 0)
	    return defaultType;

	for (int i = 0; i < DB.length; i++) {
	    if (DB[i] == null)
		continue;
	    String result = DB[i].getMIMETypeString(file_ext);
	    if (result != null)
		return result;
	}
	return defaultType;
    }

    /**
     * for debugging...
     *
    public static void main(String[] argv) throws Exception {
	MimetypesFileTypeMap map = new MimetypesFileTypeMap();
	System.out.println("File " + argv[0] + " has MIME type " +
						map.getContentType(argv[0]));
	System.exit(0);
    }
    */
}
