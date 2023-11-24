package com.s8.core.web.manganese.mail.smtp.streams;

import java.io.IOException;
import java.io.OutputStream;

import com.s8.core.web.manganese.mail.MessagingException;
import com.s8.core.web.manganese.mail.smtp.SMTPTransport;

/**
 * An OutputStream that buffers data in chunks and uses the
 * RFC 3030 BDAT SMTP command to send each chunk.
 */
public class ChunkedOutputStream extends OutputStream {
	
	public final SMTPTransport transport;
	
	private final OutputStream out;
	private final byte[] buf;
	private int count = 0;

	/**
	 * Create a ChunkedOutputStream built on top of the specified
	 * underlying output stream.
	 *
	 * @param	out	the underlying output stream
	 * @param	size	the chunk size
	 */
	public ChunkedOutputStream(SMTPTransport transport, OutputStream out, int size) {
		this.transport = transport;
		this.out = out;
		buf = new byte[size];
	}

	/**
	 * Writes the specified <code>byte</code> to this output stream.
	 *
	 * @param	b	the byte to write
	 * @exception	IOException	for I/O errors
	 */
	@Override
	public void write(int b) throws IOException {
		buf[count++] = (byte)b;
		if (count >= buf.length)
			flush();
	}

	/**
	 * Writes len bytes to this output stream starting at off.
	 *
	 * @param	b	bytes to write
	 * @param	off	offset in array
	 * @param	len	number of bytes to write
	 * @exception	IOException	for I/O errors
	 */
	@Override
	public void write(byte b[], int off, int len) throws IOException {
		while (len > 0) {
			int size = Math.min(buf.length - count, len);
			if (size == buf.length) {
				// avoid the copy
				bdat(b, off, size, false);
			} else {
				System.arraycopy(b, off, buf, count, size);
				count += size;
			}
			off += size;
			len -= size;
			if (count >= buf.length)
				flush();
		}
	}

	/**
	 * Flush this output stream.
	 *
	 * @exception	IOException	for I/O errors
	 */
	@Override
	public void flush() throws IOException {
		bdat(buf, 0, count, false);
		count = 0;
	}

	/**
	 * Close this output stream.
	 *
	 * @exception	IOException	for I/O errors
	 */
	@Override
	public void close() throws IOException {
		bdat(buf, 0, count, true);
		count = 0;
	}

	/**
	 * Send the specified bytes using the BDAT command.
	 */
	private void bdat(byte[] b, int off, int len, boolean last)
			throws IOException {
		if (len > 0 || last) {
			try {
				if (last)
					transport.sendCommand("BDAT " + len + " LAST");
				else
					transport.sendCommand("BDAT " + len);
				out.write(b, off, len);
				out.flush();
				int ret = transport.readServerResponse();
				if (ret != 250)
					throw new IOException(transport.lastServerResponse);
			} catch (MessagingException mex) {
				throw new IOException("BDAT write exception", mex);
			}
		}
	}
}