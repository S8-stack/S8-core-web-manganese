package com.s8.core.web.manganese;

import java.io.IOException;

import com.s8.core.web.manganese.smtp.SMTP_Reply;

public class MgServerException extends IOException {

	private static final long serialVersionUID = 1L;
	
	public final SMTP_Reply reply;
	
	public MgServerException(SMTP_Reply reply) {
		super(reply.serialize());
		this.reply = reply;
	}

	
}
