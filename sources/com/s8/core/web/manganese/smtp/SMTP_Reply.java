package com.s8.core.web.manganese.smtp;

public class SMTP_Reply {

	public final static int MAX_NB_REPLY_LINES = 8;

	public final int code;



	public final String[] lines;

	public final int nLines;


	public SMTP_Reply(int code, String[] lines, int nLines) {
		super();
		this.code = code;
		this.lines = lines;
		this.nLines = nLines;
	}


	public String 	serialize() {
		StringBuilder builder = new StringBuilder();
		builder.append("SERVER REPLY CODE = "+code+"\n");
		for(int i = 0; i<nLines; i++) {
			builder.append("\t"+lines[i]+"\n");
		}
		return builder.toString();
	}
}
