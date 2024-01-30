package com.s8.core.web.manganese.generator;

import java.util.ArrayList;
import java.util.List;

public class MgMailGenerator {

	/**
	 * very important
	 */
	public final static String DOCTYPE = "<!DOCTYPE html>";



	public final static String HTML_OPENING = "\n<html>" , HTML_CLOSING = "\n</html>";
	public final static String HEAD_OPENING = "\n<head>" , HEAD_CLOSING = "\n</head>";
	public final static String BODY_OPENING = "\n<body>" , BODY_CLOSING = "\n</body>";


	private final List<HTML_Element> elements = new ArrayList<>();

	public final CSS_ClassBase base;

	private final String wrapperClassname;

	public MgMailGenerator(CSS_ClassBase base, String wrapperClassname) {
		super();
		this.base = base;
		this.wrapperClassname = wrapperClassname;
	}



	public void h1(String classname, String text, CSS_Property... props) {
		HTML_appendSimpleElement("h1", classname, text, props);
	}

	public void h2(String classname, String text, CSS_Property... props) {
		HTML_appendSimpleElement("h2", classname, text, props);
	}

	public void p(String className, String text, CSS_Property... props) {
		HTML_appendSimpleElement("p", className, text, props);
	}

	public void div(String classname, String text, CSS_Property... props) {
		HTML_appendSimpleElement("div", classname, text, props);
	}
	
	
	
	
	public void logoBanner(String wrapperClassname, String logoClassname, String logoURL) {
		CSS_Style wrapperStyle = base.CSS_getClass(wrapperClassname).toStyle();
		CSS_Style logoStyle = base.CSS_getClass(logoClassname).toStyle();
		logoStyle.setProperty("background-image", "url("+logoURL+")");
		elements.add(new HTML_Logo(wrapperStyle, logoStyle));
	}
	
	public void addCode(String classname, String code) {
		CSS_Style style = base.CSS_getClass(classname).toStyle();
		elements.add(new HTML_Code(style, code));
	}
	
	

	public void HTML_appendSimpleElement(String tag, String classname, String text, CSS_Property... props) {
		CSS_Style style = base.CSS_getClass(classname).toStyle();
		if(props != null) { 
			int nProps = props.length; 
			for(int i = 0; i<nProps; i++) { style.setProperty(props[i]); }
		}
		elements.add(new HTML_SimpleElement(tag, style, text));
	}


	public String generate() {

		StringBuilder builder = new StringBuilder();
		builder.append(DOCTYPE);
		builder.append(HTML_OPENING);
		builder.append(HEAD_OPENING);
		builder.append(HEAD_CLOSING);
		builder.append(BODY_OPENING);

		CSS_Style wrapperStyle = base.CSS_getClass(wrapperClassname).toStyle();
		builder.append("\n<div>");
		builder.append("\n<table style=\"" + wrapperStyle.getInline() + "\">");

		elements.forEach(element -> element.compose(builder));


		builder.append("\n</table>");
		builder.append("\n</div>");
		builder.append(BODY_CLOSING);
		builder.append(HTML_CLOSING);

		/*
		<!DOCTYPE html>
		<html>
		<head>
		<link rel="stylesheet" href="mg-mail-style.css">
		</head>
		<body>
		<div id="mg-mail-wrapper">
		    <div class="mg-mail-logo">
		        <div style="background-image: url(AlphaventorLogo-1024px-black-text.jpg)">
		        </div>
		    </div>
		    <h1>This is a heading</h1>
		    <p>This is a paragraph.</p>
		</div>
		</body>
		</html>
		 */

		return builder.toString();
	}


}
