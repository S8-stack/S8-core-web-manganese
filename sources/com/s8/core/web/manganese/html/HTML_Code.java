package com.s8.core.web.manganese.html;

import java.util.List;

import com.s8.core.web.manganese.css.CSS_Style;

public class HTML_Code extends HTML_Element {

	
	/*
	 * <div class="mg-mail-logo-wrapper">
        <div class="mg-mail-logo" style="background-image: url(AlphaventorLogo-1024px-black-text.png)">
        </div>
    </div>
	 */
	
	private CSS_Style style;
	
	
	private final String text;
	
	
	
	
	public HTML_Code(CSS_Style style, String text) {
		super();
		this.style = style;
		this.text = text;
	}




	@Override
	public void compose(List<String> lines) {
		
		lines.add("\n<tr><td>");
		
		lines.add("\n<span style=\"");
		lines.add(style.getInline());
		lines.add("\">");
		
		lines.add(text);
		
		lines.add("</span>");
		
		lines.add("\n<td><tr>");
	}

}
