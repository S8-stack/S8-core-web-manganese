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

package com.s8.core.web.manganese.legacy.javax.activation.viewers;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import com.s8.core.web.manganese.legacy.javax.activation.CommandObject;
import com.s8.core.web.manganese.legacy.javax.activation.DataHandler;

import java.beans.*;


public class TextEditor extends Panel implements CommandObject,
    ActionListener {
	// UI Vars...
    private TextArea text_area = null;
    private GridBagLayout panel_gb = null;
    private Panel button_panel = null;
    private Button save_button = null;
	// File Vars
    private File text_file = null;
    private String text_buffer = null;
    private InputStream data_ins = null;
    private FileInputStream fis = null;
	
    private DataHandler _dh = null;
    private boolean DEBUG = false;
	/**
	 * Constructor
	 */
    public TextEditor() {
	panel_gb = new GridBagLayout();
	setLayout(panel_gb);
	
	button_panel = new Panel();
	//	button_panel.setBackground(Color.white);
	button_panel.setLayout( new FlowLayout() );
	save_button = new Button("SAVE");
	button_panel.add(save_button);
	addGridComponent(this,
			 button_panel,
			 panel_gb,
			 0,0,
			 1,1,
			 1,0);
	
	// create the text area
	text_area = new TextArea("This is text",24, 80, 
				 TextArea.SCROLLBARS_VERTICAL_ONLY );
	//	text_area.setBackground(Color.lightGray);
	text_area.setEditable( true );
	
	addGridComponent(this,
			 text_area,
			 panel_gb,
			 0,1,
			 1,2,
			 1,1);
	
	// add listeners
	save_button.addActionListener( this );
	
    }

    ////////////////////////////////////////////////////////////////////////
	/**
	 * adds a component to our gridbag layout
	 */
    private void addGridComponent(Container cont, 
				  Component comp,
				  GridBagLayout mygb,
				  int gridx,
				  int gridy, 
				  int gridw,
				  int gridh,
				  int weightx,
				  int weighty) { 
	GridBagConstraints c = new GridBagConstraints(); 
	c.gridx = gridx; 
	c.gridy = gridy; 
	c.gridwidth = gridw; 
	c.gridheight = gridh; 
	c.fill = GridBagConstraints.BOTH;
	c.weighty = weighty;
	c.weightx = weightx;
	c.anchor =  GridBagConstraints.CENTER;
	mygb.setConstraints(comp, c); 
	cont.add(comp); 
    }
	
  //--------------------------------------------------------------------
    public void setCommandContext(String verb, DataHandler dh) throws IOException {
	_dh = dh;
	this.setInputStream( _dh.getInputStream() );

    }
  //--------------------------------------------------------------------

  /**
   * set the data stream, component to assume it is ready to
   * be read.
   */
    public void setInputStream(InputStream ins) throws IOException {
	
	byte data[] = new byte[1024];
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	int bytes_read = 0;
	// check that we can actually read
	
	while((bytes_read = ins.read(data)) >0)
	    baos.write(data, 0, bytes_read);
	ins.close();

      
	// convert the buffer into a string
	// popuplate the buffer
	text_buffer = baos.toString();

	// place in the text area
	text_area.setText(text_buffer);
    }
    ///////////////////////////////////////////////////////////////////////
    private void performSaveOperation(){
	OutputStream fos = null;
	try {
	    fos = _dh.getOutputStream();
	} catch (Exception e) {}
	
	String buffer = text_area.getText();
	
	// make sure we got one
	if(fos == null) {
	    System.out.println("Invalid outputstream in TextEditor!");
	    System.out.println("not saving!");
	    return;
	}
	
	try {
	    fos.write( buffer.getBytes() );
	    fos.flush(); // flush it!
	    fos.close(); // close it!
	} catch(IOException e)
	    {
		System.out.println("TextEditor Save Operation failed with: " + e);
	    }
	
    }
  //--------------------------------------------------------------------
    public void addNotify() {
	super.addNotify();
	invalidate();
    }
  //--------------------------------------------------------------------
    public Dimension getPreferredSize()	{
	return text_area.getMinimumSize(24, 80);
    }
	/////////////////////////////////////////////////////////////////////
	// for ActionListener
    public void actionPerformed(ActionEvent evt){
	if(evt.getSource() == save_button) { // save button pressed!
	    
	    // Save ourselves
	    this.performSaveOperation();
	}
    }
	
}
