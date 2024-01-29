package com.s8.core.web.manganese.settings;

import com.s8.core.web.manganese.javax.mail.MessagingException;

public class MgContentEncoding {
	
	

	/**
	 * 
	 * @param simpleMT simple MIME type
	 * @return
	 * @throws IOException 
	 */



	/**
	 * 
	 * @param type MIME_Type
	 * @return
	 */
	public static String get(String type) throws MessagingException {

		/* <text> */
		if(type.startsWith("text/")) {
			if(type.startsWith("text/plain")) {
				return "8bit";
			}
			else if(type.startsWith("text/html")) {
				return "8bit";
			}
			else if(type.startsWith("text/xml")) {
				return "8bit";
			}
			else {
				throw new MessagingException("Unsupported MIME type: "+type);
			}
		}
		else if(type.startsWith("multipart/")) {
			throw new MessagingException("Content encoding has no meaning for multipart");
		}
		else if(type.startsWith("message/rfc822")) {
			return "8bit";
		}
		/* <image> */
		else if(type.startsWith("image/")) {
			if(type.startsWith("image/gif")) {
				return "base64";
			}
			else if(type.startsWith("image/jpeg")) {
				return "base64";
			}
			else if(type.startsWith("image/png")) {
				return "base64";
			}
			else {
				throw new MessagingException("Unsupported MIME type: "+type);
			}
		}
		else {
			throw new MessagingException("Unsupported MIME type: "+type);
		}
	}

}
