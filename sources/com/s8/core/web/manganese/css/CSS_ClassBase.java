package com.s8.core.web.manganese.css;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSS_ClassBase {
	
	/**
	 * 
	 */
	public final static String CLASS_REGEX = "(\\.?[\\w\\-]+) *[{](.*?)[}]";
	
	public final static String PROPERTY_REGEX = " *([\\w\\-]+) *: *(.*?);";
	
	public final Pattern classPattern = Pattern.compile(CLASS_REGEX);
	
	public final Pattern propertyPattern = Pattern.compile(PROPERTY_REGEX);
	
	private final Map<String, CSS_Class> classes = new HashMap<>();

	
	public CSS_ClassBase() {
		super();
	}
	
	
	public void DEBUG_print() {
		classes.forEach((name, style) -> { System.out.println("style: "+name+" = "+style.serial); });
	}
	
	
	
	public CSS_Class CSS_getClass(String className) {
		return classes.get(className);
	}
	


	/**
	 * 
	 * @param file
	 */
	public synchronized void parseString(String file) {
		
		
		
		Matcher classMatcher = classPattern.matcher(file);

		boolean hasClass = classMatcher.find();
		while(hasClass) {

			String className = classMatcher.group(1);
			String classProperties = classMatcher.group(2);
			
			List<CSS_Property> properties = parseStyle(classProperties);
			
			/* build array */
			int nProperties = properties.size();
			CSS_Property[] props = new CSS_Property[nProperties];
			for(int i=0; i<nProperties; i++) { props[i] = properties.get(i); }
			
			classes.put(className, new CSS_Class(className, props));
			
			hasClass = classMatcher.find();
		}
	}

	
	public synchronized void parseFile(String pathname) {
		parseString(readFile(pathname));
	}
	
	
	
	
	/**
	 * UTF8
	 * @param pathname
	 * @return
	 */
	public static String readFile(String pathname) {
		StringBuilder builder = new StringBuilder();
		Path path = Paths.get(pathname);
		try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
			String line = null;
			while((line = reader.readLine()) != null){ //while there is content on the current line
				builder.append(line.strip());
			}
		}catch(IOException ex){
			ex.printStackTrace(); //handle an exception here
		}
		return builder.toString();
	}

	
	public List<CSS_Property> parseStyle(String style) {
		
		
		List<CSS_Property> properties = new ArrayList<>();
		
		
		Matcher propertyMatcher = propertyPattern.matcher(style);
		boolean hasProperty = propertyMatcher.find();
		while(hasProperty) {
		
			String propertyName = propertyMatcher.group(1);
			String propertyValue = propertyMatcher.group(2);
			properties.add(new CSS_Property(propertyName, propertyValue));
			
			hasProperty = propertyMatcher.find();
		}
		
		return properties;
	}
	
	
}
