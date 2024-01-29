package com.s8.core.web.manganese.javax.mail.handlers;

import com.s8.core.web.manganese.javax.activation.DataContentHandler;
import com.s8.core.web.manganese.javax.mail.MessagingException;

public class MailDataContentHandler {


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
	public static DataContentHandler create(String type) throws MessagingException {

		/* <text> */
		if(type.startsWith("text/")) {
			if(type.startsWith("text/plain")) {
				return new text_plain();
			}
			else if(type.startsWith("text/html")) {
				return new text_html();
			}
			else if(type.startsWith("text/xml")) {
				return new text_xml();
			}
			else {
				throw new MessagingException("Unsupported MIME type: "+type);
			}
		}
		else if(type.startsWith("multipart/")) {
			return new multipart_mixed();
		}
		else if(type.startsWith("message/rfc822")) {
			return new message_rfc822();
		}
		/* <image> */
		else if(type.startsWith("image/")) {
			if(type.startsWith("image/gif")) {
				return new image_gif();
			}
			else if(type.startsWith("image/jpeg")) {
				return new image_jpeg();
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
