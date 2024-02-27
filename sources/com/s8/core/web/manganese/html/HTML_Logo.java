package com.s8.core.web.manganese.html;

import java.util.List;

import com.s8.core.web.manganese.css.CSS_Style;

public class HTML_Logo extends HTML_Element {

	
	/*
	 * <div class="mg-mail-logo-wrapper">
        <div class="mg-mail-logo" style="background-image: url(AlphaventorLogo-1024px-black-text.png)">
        </div>
    </div>
	 */
	
	private CSS_Style wrapperStyle;
	
	private CSS_Style logoStyle;
	
	
	
	
	public HTML_Logo(CSS_Style wrapperStyle, CSS_Style logoStyle) {
		super();
		this.wrapperStyle = wrapperStyle;
		this.logoStyle = logoStyle;
	}




	@Override
	public void compose(List<String> lines) {
		
		lines.add("\n<tr>");
		lines.add("\n<td>");

		
		lines.add("\n<div style=\"");
		lines.add(wrapperStyle.getInline());
		lines.add("\">");
		
		lines.add("\n<div style=\"");
		lines.add(logoStyle.getInline());
		lines.add("\">");
		
		lines.add("\n</div>");
		lines.add("\n</div>");

		lines.add("\n<td>");
		lines.add("\n<tr>");
	}

}
