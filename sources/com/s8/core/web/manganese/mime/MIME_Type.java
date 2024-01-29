package com.s8.core.web.manganese.mime;

import com.s8.core.web.manganese.javax.mail.MessagingException;

public class MIME_Type {

	public static String fromExtension(String filename) throws MessagingException {
		
		
		if(filename.endsWith(".jpeg") || filename.endsWith(".jpg")) {
			return "image/jpeg";
		}
		else if(filename.endsWith(".gif")) {
			return "image/gif";
		}
		else if(filename.endsWith(".png")) {
			return "image/jpeg";
		}
		else if(filename.endsWith(".html")) {
			return "text/html";
		}
		else {
			throw new MessagingException("Unreckognized extension MIME type");
		}
	}



}
