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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URL;


/**
 * The DataHandler class provides a consistent interface to data
 * available in many different sources and formats.
 * It manages simple stream to string conversions and related operations
 * using DataContentHandlers.
 * It provides access to commands that can operate on the data.
 * The commands are found using a CommandMap. <p>
 *
 * <b>DataHandler and the Transferable Interface</b><p>
 * DataHandler implements the Transferable interface so that data can
 * be used in AWT data transfer operations, such as cut and paste and
 * drag and drop. The implementation of the Transferable interface
 * relies on the availability of an installed DataContentHandler
 * object corresponding to the MIME type of the data represented in
 * the specific instance of the DataHandler.<p>
 *
 * <b>DataHandler and CommandMaps</b><p>
 * The DataHandler keeps track of the current CommandMap that it uses to
 * service requests for commands (<code>getCommand</code>,
 * <code>getAllCommands</code>, <code>getPreferredCommands</code>).
 * Each instance of a DataHandler may have a CommandMap associated with
 * it using the <code>setCommandMap</code> method.  If a CommandMap was
 * not set, DataHandler calls the <code>getDefaultCommandMap</code>
 * method in CommandMap and uses the value it returns. See
 * <i>CommandMap</i> for more information. <p>
 *
 * <b>DataHandler and URLs</b><p>
 * The current DataHandler implementation creates a private
 * instance of URLDataSource when it is constructed with a URL.
 *
 * @see javax.activation.CommandMap
 * @see javax.activation.DataContentHandler
 * @see javax.activation.DataSource
 * @see javax.activation.URLDataSource
 */

public class DataHandler implements Transferable {

	// Use the datasource to indicate whether we were started via the
	// DataSource constructor or the object constructor.
	private DataSource dataSource = null;
	private DataSource objDataSource = null;

	// The Object and mimetype from the constructor (if passed in).
	// object remains null if it was instantiated with a
	// DataSource.
	private Object object = null;
	private String objectMimeType = null;

	// Keep track of the CommandMap
	//private CommandMap currentCommandMap = null;

	// our transfer flavors
	private static final DataFlavor emptyFlavors[] = new DataFlavor[0];
	private DataFlavor transferFlavors[] = emptyFlavors;

	
	
	/**
	 * Our MIME_Type
	 */
	public final String MIME_type;
	
	
	/**
	 *  our DataContentHandler
	 */
	public final DataContentHandler dataContentHandler;

	// our DataContentHandlerFactory
	// the short representation of the ContentType (sans params)
	private String shortType = null;

	/**
	 * Create a <code>DataHandler</code> instance referencing the
	 * specified DataSource.  The data exists in a byte stream form.
	 * The DataSource will provide an InputStream to access the data.
	 *
	 * @param ds	the DataSource
	 */
	public DataHandler(String type, DataContentHandler dataContentHandler, DataSource ds) {
		// save a reference to the incoming DS
		this.MIME_type = type;
		this.dataContentHandler = dataContentHandler;
		dataSource = ds;
	}

	/**
	 * Create a <code>DataHandler</code> instance representing an object
	 * of this MIME type.  This constructor is
	 * used when the application already has an in-memory representation
	 * of the data in the form of a Java Object.
	 *
	 * @param obj	the Java Object
	 * @param mimeType	the MIME type of the object
	 */
	public DataHandler(String mimeType, DataContentHandler dataContentHandler, Object obj) {
		this.MIME_type = mimeType;
		this.dataContentHandler = dataContentHandler;
		object = obj;
		objectMimeType = mimeType;
	}

	/**
	 * Create a <code>DataHandler</code> instance referencing a URL.
	 * The DataHandler internally creates a <code>URLDataSource</code>
	 * instance to represent the URL.
	 *
	 * @param url	a URL object
	 */
	public DataHandler(String type, DataContentHandler dataContentHandler, URL url) {
		this.MIME_type = type;
		this.dataContentHandler = dataContentHandler;
		dataSource = new URLDataSource(url);
	}

	/**
	 * Return the CommandMap for this instance of DataHandler.
	 */
	/*
    private synchronized CommandMap getCommandMap() {
	if (currentCommandMap != null)
	    return currentCommandMap;
	else
	    return CommandMap.getDefaultCommandMap();
    }
	 */

	/**
	 * Return the DataSource associated with this instance
	 * of DataHandler.
	 * <p>
	 * For DataHandlers that have been instantiated with a DataSource,
	 * this method returns the DataSource that was used to create the
	 * DataHandler object. In other cases the DataHandler
	 * constructs a DataSource from the data used to construct
	 * the DataHandler. DataSources created for DataHandlers <b>not</b>
	 * instantiated with a DataSource are cached for performance
	 * reasons.
	 *
	 * @return	a valid DataSource object for this DataHandler
	 */
	public DataSource getDataSource() {
		if (dataSource == null) {
			// create one on the fly
			if (objDataSource == null)
				objDataSource = new DataHandlerDataSource(this);
			return objDataSource;
		}
		return dataSource;
	}

	/**
	 * Return the name of the data object. If this DataHandler
	 * was created with a DataSource, this method calls through
	 * to the <code>DataSource.getName</code> method, otherwise it
	 * returns <i>null</i>.
	 *
	 * @return	the name of the object
	 */
	public String getName() {
		if (dataSource != null)
			return dataSource.getName();
		else
			return null;
	}

	/**
	 * Return the MIME type of this object as retrieved from
	 * the source object. Note that this is the <i>full</i>
	 * type with parameters.
	 *
	 * @return	the MIME type
	 */
	public String getContentType() {
		return MIME_type; // obj/type case
	}

	
	/**
	 * Get the InputStream for this object. <p>
	 *
	 * For DataHandlers instantiated with a DataSource, the DataHandler
	 * calls the <code>DataSource.getInputStream</code> method and
	 * returns the result to the caller.
	 * <p>
	 * For DataHandlers instantiated with an Object, the DataHandler
	 * first attempts to find a DataContentHandler for the Object. If
	 * the DataHandler can not find a DataContentHandler for this MIME
	 * type, it throws an UnsupportedDataTypeException.  If it is
	 * successful, it creates a pipe and a thread.  The thread uses the
	 * DataContentHandler's <code>writeTo</code> method to write the
	 * stream data into one end of the pipe.  The other end of the pipe
	 * is returned to the caller.  Because a thread is created to copy
	 * the data, IOExceptions that may occur during the copy can not be
	 * propagated back to the caller. The result is an empty stream.<p>
	 *
	 * @return	the InputStream representing this data
	 * @exception IOException	if an I/O error occurs
	 *
	 * @see javax.activation.DataContentHandler#writeTo
	 * @see javax.activation.UnsupportedDataTypeException
	 */
	public InputStream getInputStream() throws IOException {
		InputStream ins = null;

		if (dataSource != null) {
			ins = dataSource.getInputStream();
		} else {
			DataContentHandler dch = getDataContentHandler();
			// we won't even try if we can't get a dch
			if (dch == null)
				throw new UnsupportedDataTypeException(
						"no DCH for MIME type " + getBaseType());

			if (dch instanceof ObjectDataContentHandler) {
				if (((ObjectDataContentHandler)dch).getDCH() == null)
					throw new UnsupportedDataTypeException(
							"no object DCH for MIME type " + getBaseType());
			}
			// there is none but the default^^^^^^^^^^^^^^^^
			final DataContentHandler fdch = dch;

			// from bill s.
			// ce n'est pas une pipe!
			//
			// NOTE: This block of code needs to throw exceptions, but
			// can't because it is in another thread!!! ARG!
			//
			final PipedOutputStream pos = new PipedOutputStream();
			@SuppressWarnings("resource")
			PipedInputStream pin = new PipedInputStream(pos);
			new Thread(
					new Runnable() {
						public void run() {
							try {
								fdch.writeTo(object, objectMimeType, pos);
							} catch (IOException e) {

							} finally {
								try {
									pos.close();
								} catch (IOException ie) { }
							}
						}
					},
					"DataHandler.getInputStream").start();
			ins = pin;
		}

		return ins;
	}

	/**
	 * Write the data to an <code>OutputStream</code>.<p>
	 *
	 * If the DataHandler was created with a DataSource, writeTo
	 * retrieves the InputStream and copies the bytes from the
	 * InputStream to the OutputStream passed in.
	 * <p>
	 * If the DataHandler was created with an object, writeTo
	 * retrieves the DataContentHandler for the object's type.
	 * If the DataContentHandler was found, it calls the
	 * <code>writeTo</code> method on the <code>DataContentHandler</code>.
	 *
	 * @param os	the OutputStream to write to
	 * @exception IOException	if an I/O error occurs
	 */
	public void writeTo(OutputStream os) throws IOException {
		// for the DataSource case
		if (dataSource != null) {
			InputStream is = null;
			byte data[] = new byte[8*1024];
			int bytes_read;

			is = dataSource.getInputStream();

			try {
				while ((bytes_read = is.read(data)) > 0) {
					os.write(data, 0, bytes_read);
				}
			} finally {
				is.close();
				is = null;
			}
		} else { // for the Object case
			DataContentHandler dch = getDataContentHandler();
			dch.writeTo(object, objectMimeType, os);
		}
	}

	/**
	 * Get an OutputStream for this DataHandler to allow overwriting
	 * the underlying data.
	 * If the DataHandler was created with a DataSource, the
	 * DataSource's <code>getOutputStream</code> method is called.
	 * Otherwise, <code>null</code> is returned.
	 *
	 * @return the OutputStream
	 * @exception	IOException	for failures creating the OutputStream
	 *
	 * @see javax.activation.DataSource#getOutputStream
	 * @see javax.activation.URLDataSource
	 */
	public OutputStream getOutputStream() throws IOException {
		if (dataSource != null)
			return dataSource.getOutputStream();
		else
			return null;
	}

	/**
	 * Return the DataFlavors in which this data is available. <p>
	 *
	 * Returns an array of DataFlavor objects indicating the flavors
	 * the data can be provided in. The array is usually ordered
	 * according to preference for providing the data, from most
	 * richly descriptive to least richly descriptive.<p>
	 *
	 * The DataHandler attempts to find a DataContentHandler that
	 * corresponds to the MIME type of the data. If one is located,
	 * the DataHandler calls the DataContentHandler's
	 * <code>getTransferDataFlavors</code> method. <p>
	 *
	 * If a DataContentHandler can <i>not</i> be located, and if the
	 * DataHandler was created with a DataSource (or URL), one
	 * DataFlavor is returned that represents this object's MIME type
	 * and the <code>java.io.InputStream</code> class.  If the
	 * DataHandler was created with an object and a MIME type,
	 * getTransferDataFlavors returns one DataFlavor that represents
	 * this object's MIME type and the object's class.
	 *
	 * @return	an array of data flavors in which this data can be transferred
	 * @see javax.activation.DataContentHandler#getTransferDataFlavors
	 */
	public synchronized DataFlavor[] getTransferDataFlavors() {
		/*
    	if (factory != oldFactory) // if the factory has changed, clear cache
	    transferFlavors = emptyFlavors;
		 */

		// if it's not set, set it...
		if (transferFlavors == emptyFlavors)
			transferFlavors = getDataContentHandler().getTransferDataFlavors();
		if (transferFlavors == emptyFlavors)
			return transferFlavors;	// no need to clone an empty array
		else
			return transferFlavors.clone();
	}

	/**
	 * Returns whether the specified data flavor is supported
	 * for this object.<p>
	 *
	 * This method iterates through the DataFlavors returned from
	 * <code>getTransferDataFlavors</code>, comparing each with
	 * the specified flavor.
	 *
	 * @param flavor	the requested flavor for the data
	 * @return		true if the data flavor is supported
	 * @see javax.activation.DataHandler#getTransferDataFlavors
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		DataFlavor[] lFlavors = getTransferDataFlavors();

		for (int i = 0; i < lFlavors.length; i++) {
			if (lFlavors[i].equals(flavor))
				return true;
		}
		return false;
	}

	/**
	 * Returns an object that represents the data to be
	 * transferred. The class of the object returned is defined by the
	 * representation class of the data flavor.<p>
	 *
	 * <b>For DataHandler's created with DataSources or URLs:</b><p>
	 *
	 * The DataHandler attempts to locate a DataContentHandler
	 * for this MIME type. If one is found, the passed in DataFlavor
	 * and the type of the data are passed to its <code>getTransferData</code>
	 * method. If the DataHandler fails to locate a DataContentHandler
	 * and the flavor specifies this object's MIME type and the
	 * <code>java.io.InputStream</code> class, this object's InputStream
	 * is returned.
	 * Otherwise it throws an UnsupportedFlavorException. <p>
	 *
	 * <b>For DataHandler's created with Objects:</b><p>
	 *
	 * The DataHandler attempts to locate a DataContentHandler
	 * for this MIME type. If one is found, the passed in DataFlavor
	 * and the type of the data are passed to its getTransferData
	 * method. If the DataHandler fails to locate a DataContentHandler
	 * and the flavor specifies this object's MIME type and its class,
	 * this DataHandler's referenced object is returned.  
	 * Otherwise it throws an UnsupportedFlavorException.
	 *
	 * @param flavor	the requested flavor for the data
	 * @return		the object
	 * @exception UnsupportedFlavorException	if the data could not be
	 *			converted to the requested flavor
	 * @exception IOException	if an I/O error occurs
	 * @see javax.activation.ActivationDataFlavor
	 */
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		return getDataContentHandler().getTransferData(flavor, dataSource);
	}

	/**
	 * Set the CommandMap for use by this DataHandler.
	 * Setting it to <code>null</code> causes the CommandMap to revert
	 * to the CommandMap returned by the
	 * <code>CommandMap.getDefaultCommandMap</code> method.
	 * Changing the CommandMap, or setting it to <code>null</code>,
	 * clears out any data cached from the previous CommandMap.
	 *
	 * @param commandMap	the CommandMap to use in this DataHandler
	 *
	 * @see javax.activation.CommandMap#setDefaultCommandMap
	 */
	/*
    public synchronized void setCommandMap(CommandMap commandMap) {
	if (commandMap != currentCommandMap || commandMap == null) {
	    // clear cached values...
	    transferFlavors = emptyFlavors;
	    dataContentHandler = null;

	    currentCommandMap = commandMap;
	}
    }
	 */

	/**
	 * Return the <i>preferred</i> commands for this type of data.
	 * This method calls the <code>getPreferredCommands</code> method
	 * in the CommandMap associated with this instance of DataHandler.
	 * This method returns an array that represents a subset of
	 * available commands. In cases where multiple commands for the
	 * MIME type represented by this DataHandler are present, the
	 * installed CommandMap chooses the appropriate commands.
	 *
	 * @return	the CommandInfo objects representing the preferred commands
	 *
	 * @see javax.activation.CommandMap#getPreferredCommands
	 */
	/*
    public CommandInfo[] getPreferredCommands() {
	if (dataSource != null)
	    return getCommandMap().getPreferredCommands(getBaseType(),
							dataSource);
	else
	    return getCommandMap().getPreferredCommands(getBaseType());
    }
	 */

	/**
	 * Return all the commands for this type of data.
	 * This method returns an array containing all commands
	 * for the type of data represented by this DataHandler. The
	 * MIME type for the underlying data represented by this DataHandler
	 * is used to call through to the <code>getAllCommands</code> method
	 * of the CommandMap associated with this DataHandler.
	 *
	 * @return	the CommandInfo objects representing all the commands
	 *
	 * @see javax.activation.CommandMap#getAllCommands
	 */
	/*
    public CommandInfo[] getAllCommands() {
	if (dataSource != null)
	    return getCommandMap().getAllCommands(getBaseType(), dataSource);
	else
	    return getCommandMap().getAllCommands(getBaseType());
    }
	 */

	/**
	 * Get the command <i>cmdName</i>. Use the search semantics as
	 * defined by the CommandMap installed in this DataHandler. The
	 * MIME type for the underlying data represented by this DataHandler
	 * is used to call through to the <code>getCommand</code> method
	 * of the CommandMap associated with this DataHandler.
	 *
	 * @param cmdName	the command name
	 * @return	the CommandInfo corresponding to the command
	 *
	 * @see javax.activation.CommandMap#getCommand
	 */
	/*
    public CommandInfo getCommand(String cmdName) {
	if (dataSource != null)
	    return getCommandMap().getCommand(getBaseType(), cmdName,
								dataSource);
	else
	    return getCommandMap().getCommand(getBaseType(), cmdName);
    }
	 */

	/**
	 * Return the data in its preferred Object form. <p>
	 *
	 * If the DataHandler was instantiated with an object, return
	 * the object. <p>
	 *
	 * If the DataHandler was instantiated with a DataSource,
	 * this method uses a DataContentHandler to return the content
	 * object for the data represented by this DataHandler. If no
	 * <code>DataContentHandler</code> can be found for the
	 * the type of this data, the DataHandler returns an
	 * InputStream for the data.
	 *
	 * @return the content.
	 * @exception IOException if an IOException occurs during
	 *                              this operation.
	 */
	public Object getContent() throws IOException {
		if (object != null)
			return object;
		else
			return getDataContentHandler().getContent(getDataSource());
	}

	/**
	 * A convenience method that takes a CommandInfo object
	 * and instantiates the corresponding command, usually
	 * a JavaBean component.
	 * <p>
	 * This method calls the CommandInfo's <code>getCommandObject</code>
	 * method with the <code>ClassLoader</code> used to load
	 * the <code>javax.activation.DataHandler</code> class itself.
	 *
	 * @param cmdinfo	the CommandInfo corresponding to a command
	 * @return	the instantiated command object
	 */
	public Object getBean(CommandInfo cmdinfo) {
		Object bean = null;

		try {
			// make the bean
			ClassLoader cld = null;
			// First try the "application's" class loader.
			cld = SecuritySupport.getContextClassLoader();
			if (cld == null)
				cld = this.getClass().getClassLoader();
			bean = cmdinfo.getCommandObject(this, cld);
		} catch (IOException e) {
		} catch (ClassNotFoundException e) { }

		return bean;
	}

	/**
	 * Get the DataContentHandler for this DataHandler: <p>
	 *
	 * If a DataContentHandlerFactory is set, use it.
	 * Otherwise look for an object to serve DCH in the
	 * following order: <p>
	 *
	 * 1) if a factory is set, use it <p>
	 * 2) if a CommandMap is set, use it <p>
	 * 3) use the default CommandMap <p>
	 *
	 * In any case, wrap the real DataContentHandler with one of our own
	 * to handle any missing cases, fill in defaults, and to ensure that
	 * we always have a non-null DataContentHandler.
	 *
	 * @return	the requested DataContentHandler
	 */
	private synchronized DataContentHandler getDataContentHandler() {
		return dataContentHandler;
	}

	/**
	 * Use the MimeType class to extract the MIME type/subtype,
	 * ignoring the parameters.  The type is cached.
	 */
	private synchronized String getBaseType() {
		if (shortType == null) {
			String ct = getContentType();
			try {
				MimeType mt = new MimeType(ct);
				shortType = mt.getBaseType();
			} catch (MimeTypeParseException e) {
				shortType = ct;
			}
		}
		return shortType;
	}

	
}

/**
 * The DataHanderDataSource class implements the
 * DataSource interface when the DataHandler is constructed
 * with an Object and a mimeType string.
 */
class DataHandlerDataSource implements DataSource {
	DataHandler dataHandler = null;

	/**
	 * The constructor.
	 */
	public DataHandlerDataSource(DataHandler dh) {
		this.dataHandler = dh;
	}

	/**
	 * Returns an <code>InputStream</code> representing this object.
	 * @return	the <code>InputStream</code>
	 */
	public InputStream getInputStream() throws IOException {
		return dataHandler.getInputStream();
	}

	/**
	 * Returns the <code>OutputStream</code> for this object.
	 * @return	the <code>OutputStream</code>
	 */
	public OutputStream getOutputStream() throws IOException {
		return dataHandler.getOutputStream();
	}

	/**
	 * Returns the MIME type of the data represented by this object.
	 * @return	the MIME type
	 */
	public String getContentType() {
		return dataHandler.getContentType();
	}

	/**
	 * Returns the name of this object.
	 * @return	the name of this object
	 */
	public String getName() {
		return dataHandler.getName(); // what else would it be?
	}
}

/*
 * DataSourceDataContentHandler
 *
 * This is a <i>private</i> DataContentHandler that wraps the real
 * DataContentHandler in the case where the DataHandler was instantiated
 * with a DataSource.
 */
class DataSourceDataContentHandler implements DataContentHandler {
	private DataSource ds = null;
	private DataFlavor transferFlavors[] = null;
	private DataContentHandler dch = null;

	/**
	 * The constructor.
	 */
	public DataSourceDataContentHandler(DataContentHandler dch, DataSource ds) {
		this.ds = ds;
		this.dch = dch;
	}

	/**
	 * Return the DataFlavors for this <code>DataContentHandler</code>.
	 * @return	the DataFlavors
	 */
	public DataFlavor[] getTransferDataFlavors() {

		if (transferFlavors == null) {
			if (dch != null) { // is there a dch?
				transferFlavors = dch.getTransferDataFlavors();
			} else {
				transferFlavors = new DataFlavor[1];
				transferFlavors[0] =
						new ActivationDataFlavor(ds.getContentType(),
								ds.getContentType());
			}
		}
		return transferFlavors;
	}

	/**
	 * Return the Transfer Data of type DataFlavor from InputStream.
	 * @param df	the DataFlavor
	 * @param ds	the DataSource
	 * @return		the constructed Object
	 */
	public Object getTransferData(DataFlavor df, DataSource ds) throws
	UnsupportedFlavorException, IOException {

		if (dch != null)
			return dch.getTransferData(df, ds);
		else if (df.equals(getTransferDataFlavors()[0])) // only have one now
			return ds.getInputStream();
		else
			throw new UnsupportedFlavorException(df);
	}

	public Object getContent(DataSource ds) throws IOException {

		if (dch != null)
			return dch.getContent(ds);
		else
			return ds.getInputStream();
	}

	/**
	 * Write the object to the output stream.
	 */
	public void writeTo(Object obj, String mimeType, OutputStream os)
			throws IOException {
		if (dch != null)
			dch.writeTo(obj, mimeType, os);
		else
			throw new UnsupportedDataTypeException(
					"no DCH for content type " + ds.getContentType());
	}
}

/*
 * ObjectDataContentHandler
 *
 * This is a <i>private</i> DataContentHandler that wraps the real
 * DataContentHandler in the case where the DataHandler was instantiated
 * with an object.
 */
class ObjectDataContentHandler implements DataContentHandler {
	private DataFlavor transferFlavors[] = null;
	private Object obj;
	private String mimeType;
	private DataContentHandler dch = null;

	/**
	 * The constructor.
	 */
	public ObjectDataContentHandler(DataContentHandler dch,
			Object obj, String mimeType) {
		this.obj = obj;
		this.mimeType = mimeType;
		this.dch = dch;
	}

	/**
	 * Return the DataContentHandler for this object.
	 * Used only by the DataHandler class.
	 */
	public DataContentHandler getDCH() {
		return dch;
	}

	/**
	 * Return the DataFlavors for this <code>DataContentHandler</code>.
	 * @return	the DataFlavors
	 */
	public synchronized DataFlavor[] getTransferDataFlavors() {
		if (transferFlavors == null) {
			if (dch != null) {
				transferFlavors = dch.getTransferDataFlavors();
			} else {
				transferFlavors = new DataFlavor[1];
				transferFlavors[0] = new ActivationDataFlavor(obj.getClass(),
						mimeType, mimeType);
			}
		}
		return transferFlavors;
	}

	/**
	 * Return the Transfer Data of type DataFlavor from InputStream.
	 * @param df	the DataFlavor
	 * @param ds	the DataSource
	 * @return		the constructed Object
	 */
	public Object getTransferData(DataFlavor df, DataSource ds)
			throws UnsupportedFlavorException, IOException {

		if (dch != null)
			return dch.getTransferData(df, ds);
		else if (df.equals(getTransferDataFlavors()[0])) // only have one now
			return obj;
		else
			throw new UnsupportedFlavorException(df);

	}

	public Object getContent(DataSource ds) {
		return obj;
	}

	/**
	 * Write the object to the output stream.
	 */
	public void writeTo(Object obj, String mimeType, OutputStream os)
			throws IOException {
		if (dch != null)
			dch.writeTo(obj, mimeType, os);
		else if (obj instanceof byte[])
			os.write((byte[])obj);
		else if (obj instanceof String) {
			OutputStreamWriter osw = new OutputStreamWriter(os);
			osw.write((String)obj);
			osw.flush();
		} else
			throw new UnsupportedDataTypeException(
					"no object DCH for MIME type " + this.mimeType);
	}
}
