package com.s8.core.web.manganese.generator;

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
	public void compose(StringBuilder builder) {
		
		builder.append("\n<tr><td>");
		
		builder.append("\n<span style=\"");
		builder.append(style.getInline());
		builder.append("\">");
		
		builder.append(text);
		
		builder.append("</span>");
		
		builder.append("\n<td><tr>");
	}

}
