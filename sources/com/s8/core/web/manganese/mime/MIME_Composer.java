package com.s8.core.web.manganese.mime;

public class MIME_Composer {

	
	
	public static String composeHeaders(MIME_Header... headers) {
		StringBuilder builder = new StringBuilder();
		int nHeaders = headers.length;
		for(int i = 0; i<nHeaders; i++) {
			builder.append(headers[i].compose());
		}
		builder.append(MIME_Syntax.CRLF);
		return builder.toString();
	}
}
