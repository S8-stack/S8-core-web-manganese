package com.s8.core.web.manganese.utilities;

/**
 * 
 */
public class Base64Encoder {

	/**
	 * This array maps the characters to their 6 bit values
	 */
	public final static char TABLE[] = {
			'A','B','C','D','E','F','G','H', // 0
			'I','J','K','L','M','N','O','P', // 1
			'Q','R','S','T','U','V','W','X', // 2
			'Y','Z','a','b','c','d','e','f', // 3
			'g','h','i','j','k','l','m','n', // 4
			'o','p','q','r','s','t','u','v', // 5
			'w','x','y','z','0','1','2','3', // 6
			'4','5','6','7','8','9','+','/'  // 7
	};
	
	
	/**
	 * 
	 */
	public final static char PADDING_CHAR = '=';


	/**
	 * Base64 encode a byte array.  No line breaks are inserted.
	 * This method is suitable for short strings, such as those
	 * in the IMAP AUTHENTICATE protocol, but not to encode the
	 * entire content of a MIME part.
	 *
	 * @param	inBuffer	the byte array
	 * @return		the encoded byte array
	 */
	public static byte[] encode(byte[] inBuffer) {
		if (inBuffer.length == 0) {
			return inBuffer;
		}	
		return encode(inBuffer, 0, inBuffer.length);
	}



	/**
	 * Return the corresponding encoded size for the given number
	 * of bytes, not including any CRLF.
	 */
	private static int encodedSize(int size) {
		return ((size + 2) / 3) * 4;
	}

	/**
	 * Internal use only version of encode.  Allow specifying which
	 * part of the input buffer to encode.  If outbuf is non-null,
	 * it's used as is.  Otherwise, a new output buffer is allocated.
	 */
	public static byte[] encode(byte[] inBuffer, int offset, int length) {
		
		byte[] outBuffer = new byte[encodedSize(length)];
			
		int inPosition, outPosition;
		int val;
		
		for (inPosition = offset, outPosition = 0; length >= 3; length -= 3, outPosition += 4) {
			
			/* retrieve value from inBuffer */
			val = inBuffer[inPosition++] & 0xff;
			val <<= 8;
			val |= inBuffer[inPosition++] & 0xff;
			val <<= 8;
			val |= inBuffer[inPosition++] & 0xff;
			
			outBuffer[outPosition+3] = (byte) TABLE[val & 0x3f];
			val >>= 6;
			outBuffer[outPosition+2] = (byte) TABLE[val & 0x3f];
			val >>= 6;
			outBuffer[outPosition+1] = (byte) TABLE[val & 0x3f];
			val >>= 6;
			outBuffer[outPosition+0] = (byte) TABLE[val & 0x3f];
		}
		
		/**
		 * Done with groups of three, finish up any odd bytes left
		 */
		if(length == 1) {
			val = inBuffer[inPosition++] & 0xff;
			val <<= 4;
			outBuffer[outPosition+3] = (byte) PADDING_CHAR;	// pad character;
			outBuffer[outPosition+2] = (byte) PADDING_CHAR;	// pad character;
			outBuffer[outPosition+1] = (byte) TABLE[val & 0x3f];
			val >>= 6;
			outBuffer[outPosition+0] = (byte) TABLE[val & 0x3f];
		} 
		else if (length == 2) {
			
			val = inBuffer[inPosition++] & 0xff;
			val <<= 8;
			val |= inBuffer[inPosition++] & 0xff;
			val <<= 2;
			
			outBuffer[outPosition+3] = (byte) PADDING_CHAR;	// pad character;
			
			outBuffer[outPosition+2] = (byte) TABLE[val & 0x3f];
			val >>= 6;
			outBuffer[outPosition+1] = (byte) TABLE[val & 0x3f];
			val >>= 6;
			outBuffer[outPosition+0] = (byte) TABLE[val & 0x3f];
		}
		return outBuffer;
	}

}
