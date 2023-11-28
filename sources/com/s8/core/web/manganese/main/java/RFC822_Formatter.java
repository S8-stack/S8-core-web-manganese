package com.s8.core.web.manganese.main.java;

public class RFC822_Formatter {
	
	



	/**
	 * Returns a properly formatted address (RFC 822 syntax) of
	 * Unicode characters.
	 *   
	 * @return          Unicode address string
	 * @since           JavaMail 1.2
	 */  
	public static String formatAddress_Unicode(S8MailAddress address, boolean allowUTF8) {
		String p = address.displayed;
		if (p != null) {
			return quotePhrase(p, allowUTF8) + " <" + address.actual + ">";
		}
		else {
			return "<" + address + ">";
		}	
	}
	
	

	/**
	 * RFC822 specials
	 */
	public final static String RFC822 = "()<>@,;:\\\"\t .[]";

	/*
	 * quotePhrase() quotes the words within a RFC822 phrase.
	 *
	 * This is tricky, since a phrase is defined as 1 or more
	 * RFC822 words, separated by LWSP. Now, a word that contains
	 * LWSP is supposed to be quoted, and this is exactly what the 
	 * MimeUtility.quote() method does. However, when dealing with
	 * a phrase, any LWSP encountered can be construed to be the
	 * separator between words, and not part of the words themselves.
	 * To deal with this funkiness, we have the below variant of
	 * MimeUtility.quote(), which essentially ignores LWSP when
	 * deciding whether to quote a word.
	 *
	 * It aint pretty, but it gets the job done :)
	 */
	public static final String RFC822_PHRASE = RFC822.replace(' ', '\0').replace('\t', '\0');



	/**
	 * 
	 * @param phrase
	 * @param allowUTF8
	 * @return
	 */
	public static String quotePhrase(String phrase, boolean allowUTF8) {
		int len = phrase.length();
		boolean needQuoting = false;

		for (int i = 0; i < len; i++) {
			char c = phrase.charAt(i);
			if (c == '"' || c == '\\') { 
				// need to escape them and then quote the whole string
				StringBuilder sb = new StringBuilder(len + 3);
				sb.append('"');
				for (int j = 0; j < len; j++) {
					char cc = phrase.charAt(j);
					if (cc == '"' || cc == '\\')
						// Escape the character
						sb.append('\\');
					sb.append(cc);
				}
				sb.append('"');
				return sb.toString();
			} 
			else if ((c < 040 && c != '\r' && c != '\n' && c != '\t') || 
					(c >= 0177 && !allowUTF8) || RFC822_PHRASE.indexOf(c) >= 0) {
				// These characters cause the string to be quoted
				needQuoting = true;
			}
		}

		if (needQuoting) {
			StringBuilder sb = new StringBuilder(len + 2);
			sb.append('"').append(phrase).append('"');
			return sb.toString();
		} 
		else {
			return phrase;
		}
	}
	

}
