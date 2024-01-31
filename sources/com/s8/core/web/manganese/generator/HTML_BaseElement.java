package com.s8.core.web.manganese.generator;


/**
 * 
 */
public class HTML_BaseElement extends HTML_Element {


	public final String tag;

	public final CSS_Style style;

	public final String text;


	public HTML_BaseElement(String tag, CSS_Style style, String text) {
		super();
		this.tag = tag;
		this.style = style;
		this.text = text;
	}



	/**
	 * 
	 * @param builder
	 */
	public void compose(StringBuilder builder) {

		/*
		builder.append("\n<tr>");
		builder.append("\n<td>");
		*/

		/* opening tag */
		builder.append("<");
		builder.append(tag);
		if(style != null) {
			builder.append(" style=\"");
			builder.append(style.getInline());
		}
		builder.append("\">");	
		/* </opening-tag> */

		if(text != null) {
			builder.append(text);			
		}

		/* <closing-tag> */
		builder.append("</");
		builder.append(tag);
		builder.append(">");
		/* </closing-tag> */

		/*
		builder.append("\n</td>");
		builder.append("\n</tr>");
		*/
	}

}
