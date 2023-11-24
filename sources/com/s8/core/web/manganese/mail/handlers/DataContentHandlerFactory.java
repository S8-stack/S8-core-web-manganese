package com.s8.core.web.manganese.mail.handlers;

public class DataContentHandlerFactory {


	public static DataContentHandler createFor(String mimeType) throws UnsupportedDataTypeException {
		switch(mimeType) {


		case "text/plain":
			return new DataContentHandler_text_plain();

		case "text/html":
			return new DataContenHandler_text_html();

		case "text/xml":
			return new DataContentHandler_text_xml();

		case "multipart/mixed":
			return new DataContentHandler_multipart_mixed();

		case "image/jpeg":
			return new DataContentHandler_image_jpeg();

		case "image/gif":
			return new DataContentHandler_image_gif();

		default : throw new UnsupportedDataTypeException(mimeType);

		}
	}
}
