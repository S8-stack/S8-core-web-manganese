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

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * The CommandInfo class is used by CommandMap implementations to
 * describe the results of command requests. It provides the requestor
 * with both the verb requested, as well as an instance of the
 * bean. There is also a method that will return the name of the
 * class that implements the command but <i>it is not guaranteed to
 * return a valid value</i>. The reason for this is to allow CommandMap
 * implmentations that subclass CommandInfo to provide special
 * behavior. For example a CommandMap could dynamically generate
 * JavaBeans. In this case, it might not be possible to create an
 * object with all the correct state information solely from the class
 * name.
 */

@SuppressWarnings("removal")
public class CommandInfo {
    private String verb;
    private String className;

    /**
     * The Constructor for CommandInfo.
     * @param verb The command verb this CommandInfo decribes.
     * @param className The command's fully qualified class name.
     */
    public CommandInfo(String verb, String className) {
	this.verb = verb;
	this.className = className;
    }

    /**
     * Return the command verb.
     *
     * @return the command verb.
     */
    public String getCommandName() {
	return verb;
    }

    /**
     * Return the command's class name. <i>This method MAY return null in
     * cases where a CommandMap subclassed CommandInfo for its
     * own purposes.</i> In other words, it might not be possible to
     * create the correct state in the command by merely knowing
     * its class name. <b>DO NOT DEPEND ON THIS METHOD RETURNING
     * A VALID VALUE!</b>
     *
     * @return The class name of the command, or <i>null</i>
     */
    public String getCommandClass() {
	return className;
    }

    /**
     * Return the instantiated JavaBean component.
     * <p>
     * If the current runtime environment supports
     * {@link java.beans.Beans#instantiate Beans.instantiate},
     * use it to instantiate the JavaBeans component.  Otherwise, use
     * {@link java.lang.Class#forName Class.forName}.
     * <p>
     * The component class needs to be public.
     * On Java SE 9 and newer, if the component class is in a named module,
     * it needs to be in an exported package.
     * <p>
     * If the bean implements the <code>javax.activation.CommandObject</code>
     * interface, call its <code>setCommandContext</code> method.
     * <p>
     * If the DataHandler parameter is null, then the bean is
     * instantiated with no data. NOTE: this may be useful
     * if for some reason the DataHandler that is passed in
     * throws IOExceptions when this method attempts to
     * access its InputStream. It will allow the caller to
     * retrieve a reference to the bean if it can be
     * instantiated.
     * <p>
     * If the bean does NOT implement the CommandObject interface,
     * this method will check if it implements the
     * java.io.Externalizable interface. If it does, the bean's
     * readExternal method will be called if an InputStream
     * can be acquired from the DataHandler.<p>
     *
     * @param dh	The DataHandler that describes the data to be
     *			passed to the command.
     * @param loader	The ClassLoader to be used to instantiate the bean.
     * @return The bean
     * @exception	IOException	for failures reading data
     * @exception	ClassNotFoundException	if command object class can't
     *						be found
     * @see java.beans.Beans#instantiate
     * @see javax.activation.CommandObject
     */
    public Object getCommandObject(DataHandler dh, ClassLoader loader)
			throws IOException, ClassNotFoundException {
	Object new_bean = null;

	// try to instantiate the bean
	new_bean = Beans.instantiate(loader, className);

	// if we got one and it is a CommandObject
	if (new_bean != null) {
	    if (new_bean instanceof CommandObject) {
		((CommandObject)new_bean).setCommandContext(verb, dh);
	    } else if (new_bean instanceof Externalizable) {
		if (dh != null) {
		    InputStream is = dh.getInputStream();
		    if (is != null) {
			((Externalizable)new_bean).readExternal(
					       new ObjectInputStream(is));
		    }
		}
	    }
	}

	return new_bean;
    }

    /**
     * Helper class to invoke Beans.instantiate reflectively or the equivalent
     * with core reflection when module java.desktop is not readable.
     */
    private static final class Beans {
        static final Method instantiateMethod;

        static {
            Method m;
            try {
                Class<?> c = Class.forName("java.beans.Beans");
                m = c.getDeclaredMethod("instantiate", ClassLoader.class, String.class);
            } catch (ClassNotFoundException e) {
                m = null;
            } catch (NoSuchMethodException e) {
                m = null;
            }
            instantiateMethod = m;
        }

        /**
         * Equivalent to invoking java.beans.Beans.instantiate(loader, cn)
         */
        static Object instantiate(ClassLoader loader, String cn)
                throws IOException, ClassNotFoundException {

            Exception exception;

            if (instantiateMethod != null) {

                // invoke Beans.instantiate
                try {
                    return instantiateMethod.invoke(null, loader, cn);
                } catch (InvocationTargetException e) {
                    exception = e;
                } catch (IllegalAccessException e) {
                    exception = e;
                }

            } else {

		SecurityManager security = System.getSecurityManager();
		if (security != null) {
		    // if it's ok with the SecurityManager, it's ok with me.
		    String cname = cn.replace('/', '.');
		    if (cname.startsWith("[")) {
			int b = cname.lastIndexOf('[') + 2;
			if (b > 1 && b < cname.length()) {
			    cname = cname.substring(b);
			}
		    }
		    int i = cname.lastIndexOf('.');
		    if (i != -1) {
			security.checkPackageAccess(cname.substring(0, i));
		    }
		}

                // Beans.instantiate specified to use SCL when loader is null
                if (loader == null) {
                    loader = (ClassLoader)
		        AccessController.doPrivileged(new PrivilegedAction() {
			    public Object run() {
				ClassLoader cl = null;
				try {
				    cl = ClassLoader.getSystemClassLoader();
				} catch (SecurityException ex) { }
				return cl;
			    }
			});
                }
                Class<?> beanClass = Class.forName(cn, true, loader);
                try {
                    return beanClass.newInstance();
                } catch (Exception ex) {
                    throw new ClassNotFoundException(beanClass + ": " + ex, ex);
                }

            }
            return null;
        }
    }
}
