package com.s8.core.web.manganese.mail;

/**
 * This inner class defines the types of recipients allowed by
 * the Message class. The currently defined types are TO,
 * CC and BCC.
 *
 * Note that this class only has a protected constructor, thereby
 * restricting new Recipient types to either this class or subclasses.
 * This effectively implements an enumeration of the allowed Recipient
 * types.
 *
 * The following code sample shows how to use this class to obtain
 * the "TO" recipients from a message.
 * <blockquote><pre>
 *
 * Message msg = folder.getMessages(1);
 * Address[] a = m.getRecipients(Message.RecipientType.TO);
 *
 * </pre></blockquote>
 *
 * @see com.s8.core.web.manganese.mail.Message#getRecipients
 * @see com.s8.core.web.manganese.mail.Message#setRecipients
 * @see com.s8.core.web.manganese.mail.Message#addRecipients
 */
public enum RecipientType {
	
	/**
	 * The "To" (primary) recipients.
	 */
	TO("To"),
	
	/**
	 * The "Cc" (carbon copy) recipients.
	 */
	CC ("Cc"),
	
	/**
	 * The "Bcc" (blind carbon copy) recipients.
	 */
	BCC("Bcc");

	/**
	 * The type of recipient, usually the name of a corresponding
	 * Internet standard header.
	 */
	public final String type;

	private static final long serialVersionUID = -7479791750606340008L;

	/**
	 * Constructor for use by subclasses.
	 *
	 * @param	type	the recipient type
	 */
	private RecipientType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}
}