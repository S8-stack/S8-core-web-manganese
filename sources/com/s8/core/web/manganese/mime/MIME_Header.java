package com.s8.core.web.manganese.mime;

public class MIME_Header {

	public final String name;
	
	public final String value;

	
	public MIME_Header(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	

	public String compose() {
		return name + MIME_Syntax.COLUMN + value + MIME_Syntax.CRLF;
	}
	
	
}
