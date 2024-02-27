package com.s8.core.web.manganese.mime;

import java.io.IOException;

public class MIME_Type {
	

	
	public static String fromExtension(String filename) throws IOException {
		
		
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
			throw new IOException("Unreckognized extension MIME type");
		}
	}



}
