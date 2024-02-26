package com.s8.core.web.manganese.css;


/**
 * 
 */
public class CSS_Property {

	
	/**
	 * 
	 */
	public final String name;
	
	
	/**
	 * 
	 */
	public final String value;

	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public CSS_Property(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	
	public CSS_Property copy() {
		return new CSS_Property(name, value);
	}
}
