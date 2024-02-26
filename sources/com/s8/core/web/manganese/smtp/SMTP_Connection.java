package com.s8.core.web.manganese.smtp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * 
 */
public class SMTP_Connection {

	

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

	
	
	private final SMTP_MgClient client;
	/**
	 * 
	 */
	private Socket socket;
	
	public boolean isVerbose;

	/**
	 * 
	 */
	private BufferedReader in;

	/**
	 * 
	 */
	private BufferedWriter out;

	/**
	 * 
	 * @param socket
	 * @throws IOException
	 */
	public SMTP_Connection(SMTP_MgClient client, Socket socket, boolean isVerbose) throws IOException {
		super();
		this.client = client;
		this.socket = socket;
		this.isVerbose = isVerbose;
		/* read response from in */
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII));

		this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII));
	}

	

	/**
	 * 
	 * The format for multiline replies requires that every line, except the last,
	 * begin with the reply code, followed immediately by a hyphen, "-" (also known
	 * as minus), followed by text. The last line will begin with the reply code,
	 * followed immediately by <SP>, optionally some text, and <CRLF>. As noted
	 * above, servers SHOULD send the <SP> if subsequent text is not sent, but
	 * clients MUST be prepared for it to be omitted.
	 * 
	 * For example:
	 * 
	 * 250-First line 250-Second line 250-234 Text beginning with numbers 250 The
	 * last line
	 * 
	 * In a multiline reply, the reply code on each of the lines MUST be the same.
	 * It is reasonable for the client to rely on this, so it can make processing
	 * decisions based on the code in any line, assuming that all others will be the
	 * same. In a few cases, there is important data for the client in the reply
	 * "text". The client will be able to identify these cases from the current
	 * context.
	 * 
	 * @return
	 * @throws IOException
	 */
	public SMTP_Reply receiveReply() throws IOException {

		int code = -1;
		String[] lines = new String[SMTP_Reply.MAX_NB_REPLY_LINES];
		int position = 0;

		boolean hasMoreLines = true;

		while (hasMoreLines && position < lines.length) {

			String line = in.readLine();
			
			if (line == null) {
				throw new IOException("Failed to read next line");
			}
			
			if(isVerbose) {
				System.out.println(line);
			}
			/* retrieve line code */
			int lineCode = Integer.parseInt(line.substring(0, 3));
			if (code >= 0) {
				if (lineCode != code) {
					throw new IOException("Different reply code for this line");
				}
			} else {
				code = lineCode;
			}

			/* has more lines */
			char continuationChar = line.charAt(3);
			if (continuationChar == SP) {
				hasMoreLines = false;
			} else if (continuationChar == HYPHEN) {
				hasMoreLines = true;
			} else {
				throw new IOException("Unsupported continuation char");
			}

			lines[position++] = line.substring(4);
		}

		return new SMTP_Reply(code, lines, position);
	}

	/**
	 * 
	 * Commands HELO/EHLO – initiates the start of the SMTP session. Syntax: "EHLO"
	 * SP ( Domain / address-literal ) CRLF or"HELO" SP Domain CRLF
	 * 
	 * @param domainAddress Domain / address-literal
	 * @throws IOException
	 */
	public void sendCommand_EHLO(String domainAddress) throws IOException {
		sendCommand("EHLO "+domainAddress);
	}

	/**
	 * MAIL FROM – initiates mail transaction and includes the reverse path and
	 * sometimes optional parameters. Syntax: MAIL FROM:<reverse-path> [SP
	 * <mail-parameters> ] <CRLF>
	 * 
	 * @throws IOException
	 */
	public void sendCommand_MAIL_FROM(String reversePath) throws IOException {
		sendCommand("MAIL FROM:<"+reversePath+">");
	}

	/**
	 * RCPT TO – indicates the recipient(s) and includes their email address (a.k.a.
	 * forward-path) as an argument. Syntax: RCPT TO:<forward-path> [ SP
	 * <rcpt-parameters> ] <CRLF>
	 * 
	 * @param forwardPath
	 * @throws IOException
	 */
	public void sendCommand_RCPT_TO(String forwardPath) throws IOException {
		sendCommand("RCPT TO:<"+forwardPath+">");
	}

	/**
	 * DATA – asks the server’s permission to transfer data and does so once it
	 * receives a positive reply. Syntax: "DATA" CRLF
	 * 
	 * @throws IOException
	 */
	public void sendCommand_DATA() throws IOException {
		sendCommand("DATA");
	}

	/**
	 * QUIT – initiates the termination of the connection Syntax: "QUIT" CRLF
	 * 
	 * @throws IOException
	 */
	public void sendCommand_QUIT() throws IOException {
		sendCommand("QUIT");
	}
	
	
	public void sendCommand(String command) throws IOException {
		out.append(command);
		out.append(CRLF);
		out.flush();
		if(isVerbose) {
			System.out.print("\tClient > "+command+"\n");
		}
	}

	/**
	 * 4.1.1.4. DATA (DATA)
	 * 
	 * The receiver normally sends a 354 response to DATA, and then treats the lines
	 * (strings ending in <CRLF> sequences, as described in Section 2.3.7) following
	 * the command as mail data from the sender. This command causes the mail data
	 * to be appended to the mail data buffer. The mail data may contain any of the
	 * 128 ASCII character codes, although experience has indicated that use of
	 * control characters other than SP, HT, CR, and LF may cause problems and
	 * SHOULD be avoided when possible.
	 * 
	 * The mail data are terminated by a line containing only a period, that is, the
	 * character sequence "<CRLF>.<CRLF>", where the first <CRLF> is actually the
	 * terminator of the previous line (see Section 4.5.2). This is the end of mail
	 * data indication. The first <CRLF> of this terminating sequence is also the
	 * <CRLF> that ends the final line of the data (message text) or, if there was
	 * no mail data, ends the DATA command itself (the "no mail data" case does not
	 * conform to this specification since it would require that neither the trace
	 * header fields required by this specification nor the message header section
	 * required by RFC 5322 [4] be transmitted). An extra <CRLF> MUST NOT be added,
	 * as that would cause an empty line to be added to the message. The only
	 * exception to this rule would arise if the message body were passed to the
	 * originating SMTP-sender with a final "line" that did not end in <CRLF>; in
	 * that case, the originating SMTP system MUST either reject the message as
	 * invalid or add <CRLF> in order to have the receiving SMTP server recognize
	 * the "end of data" condition.
	 * 
	 * @param lines
	 * @throws IOException 
	 */
	public void sendData(String[] lines) throws IOException {
		int nLines = lines.length;
		for(int i = 0; i<nLines; i++) {
			out.append(lines[i]);
			out.append(CRLF);
			if(isVerbose) {
				System.out.print("\tClient > "+lines[i]+"\n");
			}
		}
		out.append(DATA_TERMINATION);
		out.append(CRLF);
		if(isVerbose) {
			System.out.print("\tClient > .\n");
		}
		out.flush();
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		out.flush();
		socket.close();
	}

	
	public void upgradeTLS() throws IOException {
		

		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

		SSLSocket secureSocket = (SSLSocket) factory.createSocket(socket, null, true);
		
		secureSocket.setUseClientMode(true);
		
		SSLParameters parameters = new SSLParameters();

		/* Set TLS 1.3 only in an SSLParameters object. */
		parameters.setProtocols(client.tunnelingProtocols);

		/* Apply the parameters to an SSLSocket object */
		secureSocket.setSSLParameters(parameters);


		
		
		/*
		 * send http request
		 *
		 * Before any application data is sent or received, the
		 * SSL socket will do SSL handshaking first to set up
		 * the security attributes.
		 *
		 * SSL handshaking can be initiated by either flushing data
		 * down the pipe, or by starting the handshaking by hand.
		 *
		 * Handshaking is started manually in this example because
		 * PrintWriter catches all IOExceptions (including
		 * SSLExceptions), sets an internal error flag, and then
		 * returns without rethrowing the exception.
		 *
		 * Unfortunately, this means any error messages are lost,
		 * which caused lots of confusion for others using this
		 * code.  The only way to tell there was an error is to call
		 * PrintWriter.checkError().
		 */
		secureSocket.startHandshake();
		
		if(isVerbose) {
			System.out.println("[Handshake] succeed!");
		}
		
		this.socket = secureSocket;
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII));
		this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII));
	}
	
}
