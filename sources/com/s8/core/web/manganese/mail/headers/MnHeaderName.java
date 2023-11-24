package com.s8.core.web.manganese.mail.headers;


/**
 * 
 */
public enum MnHeaderName {

	RETURN_PATH("Return-Path", 0),
	
	RECEIVED("Received", 1),
	
	RESENT_DATE("Resent-Date", 2),
	
	RESENT_FROM("Resent-From", 3),
	
	RESENT_SENDER("Resent-Sender", 4),
	
	RESENT_TO("Resent-To", 5),
	
	RESENT_CC("Resent-Cc", 6),
	
	RESENT_BCC("Resent-Bcc", 7),
	
	RESENT_MESSAGE_ID("Resent-Message-Id", 8),
	
	DATE("Date", 9),
	
	FROM("From", 10),
	
	SENDER("Sender", 11),
	
	REPLY_TO("Reply-To", 12),
	
	TO("To", 13),
	
	CC("Cc", 14),
	
	BCC("Bcc", 15),
	
	MESSAGE_ID("Message-Id", 16),
	
	IN_REPLY_TO("In-Reply-To", 17),
	
	REFERENCES("References", 18),
	
	SUBJECT("Subject", 19),
	
	COMMENTS("Comments", 20),
	
	KEYWORDS("Keywords", 21),
	
	ERRORS_TO("Errors-To", 22),
	
	MIME_VERSION("MIME-Version", 23),
	
	CONTENT_TYPE("Content-Type", 24),
	
	CONTENT_TRANSFER_ENCODING("Content-Transfer-Encoding", 25),
	
	CONTENT_MD5("Content-MD5", 26),
	
	WILD_CARD(":", 27),
	
	CONTENT_LENGTH("Content-Length", 28),
	
	STATUS("Status", 29);
	
	
	
	public final String key;
	
	public final int order;
	
	private MnHeaderName(String key, int order) {
		this.key = key;
		this.order = order;
	}
	
	public static int compareOrder(MnHeaderName left, MnHeaderName right){
		if(left != null && right != null) {
			if(left.order > right.order) {
				return 1;
			}
			else if(left.order < right.order) {
				return -1;
			}
			else {
				return 0;
			}	
		}
		else {
			return 0;
		}
	}
}
