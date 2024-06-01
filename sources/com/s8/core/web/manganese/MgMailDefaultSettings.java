package com.s8.core.web.manganese;

import com.s8.core.web.manganese.css.CSS_ClassBase;

public class MgMailDefaultSettings {
	
	
	public final String senderEmailAddress;
	
	public final String senderDisplayedName;
	
	public final CSS_ClassBase classBase;
	

	public MgMailDefaultSettings(String cssPathname, String senderUsername, String senderDisplayedName) {
		super();
		
		this.senderEmailAddress = senderUsername;
		this.senderDisplayedName = senderDisplayedName;
		

		classBase = new CSS_ClassBase();
		classBase.parseFile(cssPathname);
	}


	/**
	 * 
	 * @return
	 */
	public String getSenderEmailAddress() {
		return senderEmailAddress;
	}

	
	/**
	 * 
	 * @return
	 */
	public String getSenderDisplayedName() {
		return senderDisplayedName;
	}


	public synchronized CSS_ClassBase CSS_getClassBase() {
		return classBase;
	}

}
