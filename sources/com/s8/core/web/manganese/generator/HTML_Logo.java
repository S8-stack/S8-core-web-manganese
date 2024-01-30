package com.s8.core.web.manganese.generator;

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
	public void compose(StringBuilder builder) {
		
		builder.append("\n<tr>");
		builder.append("\n<td>");

		
		builder.append("\n<div style=\"");
		builder.append(wrapperStyle.getInline());
		builder.append("\">");
		
		builder.append("\n<div style=\"");
		builder.append(logoStyle.getInline());
		builder.append("\">");
		
		builder.append("\n</div>");
		builder.append("\n</div>");

		builder.append("\n<td>");
		builder.append("\n<tr>");
	}

}
