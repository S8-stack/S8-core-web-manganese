package com.s8.core.web.manganese.smtp;

public class SMTP_Syntax {
	

	/**
	 * Lines consist of zero or more data characters terminated by the sequence
	 * ASCII character "CR" (hex value 0D) followed immediately by ASCII character
	 * "LF" (hex value 0A). This termination sequence is denoted as <CRLF> in this
	 * document. Conforming implementations MUST NOT recognize or generate any other
	 * character or character sequence as a line terminator. Limits MAY be imposed
	 * on line lengths by servers (see Section 4).
	 * 
	 * "\r" = \u000d: carriage return CR "\n" = \u000a: linefeed LF
	 */
	public final static String CRLF = "\r\n";

	public final static char SP = ' ';

	public final static char HYPHEN = '-';
	
	public final static char DATA_TERMINATION = '.';


}
