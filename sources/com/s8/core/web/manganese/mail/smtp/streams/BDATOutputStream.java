package com.s8.core.web.manganese.mail.smtp.streams;

import java.io.IOException;
import java.io.OutputStream;

import com.s8.core.web.manganese.mail.smtp.SMTPTransport;

/**
 * An SMTPOutputStream that wraps a ChunkedOutputStream.
 */
public class BDATOutputStream extends SMTPOutputStream {

	/**
	 * Create a BDATOutputStream that wraps a ChunkedOutputStream
	 * of the given size and built on top of the specified
	 * underlying output stream.
	 *
	 * @param	out	the underlying output stream
	 * @param	size	the chunk size
	 */
	public BDATOutputStream(SMTPTransport transport, OutputStream out, int size) {
		super(new ChunkedOutputStream(transport, out, size));
	}

	/**
	 * Close this output stream.
	 *
	 * @exception	IOException	for I/O errors
	 */
	@Override
	public void close() throws IOException {
		out.close();
	}
}