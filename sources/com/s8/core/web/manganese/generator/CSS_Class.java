package com.s8.core.web.manganese.generator;


/**
 * Final
 */
public class CSS_Class {

	public final String name;

	public final CSS_Property[] properties;

	public final String serial;

	public CSS_Class(String name, CSS_Property[] properties) {
		super();
		this.name = name;
		this.properties = properties;

		int nProps = properties.length;
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i<nProps; i++) { 
			CSS_Property property = properties[i];
			if(i>0) { builder.append(' '); }
			builder.append(property.name);
			builder.append(": ");
			builder.append(property.value);
			builder.append(";");
		}
		serial = builder.toString();
	}



	/**
	 * 
	 * @return
	 */
	public CSS_Style toStyle() {
		
		int n = properties.length;
		CSS_Property[] propertiesCopy = new CSS_Property[n];
		for(int i = 0; i < n; i++) { propertiesCopy[i] = properties[i]; }
		CSS_Style style = new CSS_Style(name);
		style.properties = propertiesCopy;
		style.position = n;
		style.isSerialUpToDate = true;
		style.serial = serial;
		
		return style;
	}

}
