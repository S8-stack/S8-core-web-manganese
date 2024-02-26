package com.s8.core.web.manganese.css;

public class CSS_Style {

	public final String name;
	
	int position = 0;

	CSS_Property[] properties = new CSS_Property[4];

	boolean isSerialUpToDate = false;

	String serial;
	
	


	public CSS_Style(String name) {
		super();
		this.name = name;
	}
	
	public void setProperty(CSS_Property property) {
		int index = getPropertyIndex(name);
		if(index > 0) { 
			properties[index] = property;
		}
		else {
			allocate();
			properties[position++] = property;
		}
		isSerialUpToDate = false;
	}


	/**
	 */
	public void setProperty(String name, String value) {
		setProperty(new CSS_Property(name, value));
	}


	/**
	 * 
	 * @return
	 */
	public String getInline() {
		if(!isSerialUpToDate) {
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i<position; i++) { CSS_Property property = properties[i];
			if(i>0) { builder.append(' '); }
			builder.append(property.name);
			builder.append(": ");
			builder.append(property.value);
			builder.append(";");
			}
			serial = builder.toString();
			isSerialUpToDate = true;
		}
		return serial;
	}


	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	private int getPropertyIndex(String propertyName) {
		for(int i = 0; i<position; i++) {
			if(properties[i].name.equals(propertyName)) { 
				return i;
			}
		}
		return -1;
	}


	/**
	 * 
	 */
	private void allocate() {
		int n = properties.length;
		if(position >= n) {
			CSS_Property[] extension = new CSS_Property[2*n];
			for(int i = 0; i<n; i++) {
				extension[i] = properties[i];
			}
			properties = extension;
		}
	}

	public CSS_Style copy() {
		
		
		CSS_Style styleCopy = new CSS_Style(name);
		
		int n = properties.length;
		CSS_Property[] propertiesCopy = new CSS_Property[2*n];
		for(int i = 0; i < position; i++) { propertiesCopy[i] = properties[i].copy(); }
		styleCopy.properties = propertiesCopy;
		
		if(isSerialUpToDate) {
			styleCopy.isSerialUpToDate = true;
			styleCopy.serial = serial;
		}
		
		return styleCopy;
	}
	
}
