package com.s8.core.web.manganese.html;

import java.util.ArrayList;
import java.util.List;

import com.s8.core.web.manganese.css.CSS_ClassBase;
import com.s8.core.web.manganese.css.CSS_Style;

public class HTML_MgMailContentGenerator {

	/**
	 * very important
	 */
	public final static String DOCTYPE = "<!DOCTYPE html>";



	public final static String HTML_OPENING = "\n<html>" , HTML_CLOSING = "\n</html>";
	public final static String HEAD_OPENING = "\n<head>" , HEAD_CLOSING = "\n</head>";
	public final static String BODY_OPENING = "\n<body>" , BODY_CLOSING = "\n</body>";


	private final List<HTML_Element> elements = new ArrayList<>();

	private CSS_ClassBase base;

	private CSS_Style wrapperStyle;

	public HTML_MgMailContentGenerator(CSS_ClassBase base) {
		super();
		this.base = base;
	}
	
	public void setBaseClass(CSS_ClassBase base) {
		this.base = base;
	}
	
	
	public void setWrapperStyle(String classname, String style) {
		if(classname != null) {
			CSS_Style computedStyle = base.CSS_getClass(classname).toStyle();
			if(style != null) {
				base.parseStyle(style).forEach(property -> {
					computedStyle.setProperty(property);	
				});
			}
			wrapperStyle = computedStyle;
		}
		else {
			wrapperStyle = null;
		}
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
	
	

	public void HTML_appendBaseElement(String tag, String classname, String style, String text) {
		CSS_Style appliedStyle = null;
		if(classname != null) {
			CSS_Style computedStyle = base.CSS_getClass(classname).toStyle();
			if(style != null) {
				base.parseStyle(style).forEach(property -> {
					computedStyle.setProperty(property);	
				});
			}
			appliedStyle = computedStyle;
		}
		elements.add(new HTML_BaseElement(tag, appliedStyle, text));
	}


	public void generateContent(List<String> lines) {

		lines.add(DOCTYPE);
		lines.add(HTML_OPENING);
		lines.add(HEAD_OPENING);
		lines.add(HEAD_CLOSING);
		lines.add(BODY_OPENING);
		
		lines.add("\n<div style=\"" + wrapperStyle.getInline() + "\">");
		/* builder.append("\n<table>"); */

		elements.forEach(element -> element.compose(lines));


		/* builder.append("\n</table>"); */
		lines.add("\n</div>");
		lines.add(BODY_CLOSING);
		lines.add(HTML_CLOSING);

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
	}


}
