package com.s8.core.web.manganese.smtp;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.SSLParameters;

import com.s8.core.web.manganese.MgServerException;
import com.s8.core.web.manganese.sasl.SASL_Authenticator;

public class SMTP_MgClient {


	/**
	 * SMTP server URL
	 */
	public final String host;


	/**
	 * SMTP server port. Options are:
	 * <ul>
	 * <li><b>25</b> is the oldest SMTP port, but using it for email submission is no longer
	 * common. As it doesn’t have security mechanisms, spammers abuse it heavily.
	 * For that reason, internet service providers (ISPs) usually block port number
	 * 25. It’s recommended to use it only as a relay port. </li>
	 * <li><b>465</b> is more secure compared to 25, but it’s not an official SMTP port and is now deprecated. It
	 * supports SSL encryption. Still, it’s recommended to avoid using it whenever possible.</li> 
	 * <li><b>587</b> is the default SMTP port recommended with STARTTLS. Almost all
	 * email service providers support it. </li>
	 * <li><b>2525</b> is an alternative to 587. It can be
	 * used when 587 is blocked or unavailable. 2525 has never been recognized as an
	 * official SMTP port. Regardless, most ISPs allow transactions through this
	 * port.
	 * </li>
	 */
	public final int port;


	/**
	 *  (new String[] {"TLSv1.3"})
	 */
	public final String[] tunnelingProtocols;
	


	/**
	 * 
	 * @param host
	 * @param port
	 * @param tunnelingProtocols
	 */
	public SMTP_MgClient(String host, int port, String[] tunnelingProtocols) {
		super();
		this.host = host;
		this.port = port;
		this.tunnelingProtocols = tunnelingProtocols;
	}



	/**
	 * @throws IOException 
	 * @throws InterruptedException 
	 * 
	 */
	public void sendMail(SASL_Authenticator authenticator, 
			String reversePath, 
			String forwardPath, 
			List<String> dataLines, boolean isVerbose) throws IOException {

		SMTP_Connection connection = createConnection(isVerbose);
		/*
		 * close connection
		 */
		SMTP_Reply reply;
		
		reply = connection.receiveReply();

		
		// Server: 220 server.net Simple Mail Transfer Service Ready
		if(reply.code != SMTP_ReplyCode.DOMAIN_SERVICE_IS_READY) {
			throw new IOException();
		}

		/* Client: EHLO client.org */
		connection.sendCommand_EHLO("alphaventor.com");

		reply = connection.receiveReply();
		if(reply.code != SMTP_ReplyCode.OK) { throw new MgServerException(reply); }
		
		connection.sendCommand("STARTTLS");
		
		reply = connection.receiveReply(); /* GO AHEAD */
		//if(reply.code != SMTP_ReplyCode.DOMAIN_SERVICE_IS_READY) { throw new MgServerException(reply); }
		
		connection.upgradeTLS();
		

		/*
		S: 250-server.net greets client.org
		S: 250-DSN
		S: 250-VRFY
		S: 250 HELP
		 */

		/*
		C: VRFY Maverick
		S: 250 John Maverick <j.maverick@server.net>
		 */
		
		authenticator.authenticate(connection);
		

		/* C: MAIL FROM:"user1@client.org" */
		connection.sendCommand_MAIL_FROM(reversePath);
		reply = connection.receiveReply(); /* S: 250 OK */
		if(reply.code != SMTP_ReplyCode.OK) { throw new MgServerException(reply); }


		/* C: RCPT TO:"j.maverick@server.net" */
		connection.sendCommand_RCPT_TO(forwardPath);
		reply = connection.receiveReply(); /* S: 250 OK */
		if(reply.code != SMTP_ReplyCode.OK) { throw new MgServerException(reply); }


		connection.sendCommand_DATA(); /* C: DATA */
		reply = connection.receiveReply(); /* S: 354 Start mail input; end with <CRLF>.<CRLF> */
		if(reply.code != SMTP_ReplyCode.START_MAIL_INPUT) { throw new MgServerException(reply); }


		/*
		C: Date: Wed, 30 July 2019 06:04:34
		C: From: user1@client.org
		C: Subject: Test email
		C: To: j.maverick@server.net, s.smith@server.net
		C: Hi John and Samantha 
		C: .
		 */
		connection.sendData(dataLines);
		reply = connection.receiveReply(); /* S: 250 OK */
		if(reply.code != SMTP_ReplyCode.OK) { throw new MgServerException(reply); }



		/* C: QUIT */
		connection.sendCommand_QUIT();
		reply = connection.receiveReply(); /* 	S: 221 server.net Service closing transmission channel */
		if(reply.code != SMTP_ReplyCode.CLOSING) { throw new IOException(); }


		connection.close();
	}




	public SMTP_Connection createConnection(boolean isVerbose) throws IOException {

		/*
		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

		SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
		*/
		
		SocketFactory factory2 = SocketFactory.getDefault();

		Socket socket = factory2.createSocket(host, port);
		
		
		SSLParameters parameters = new SSLParameters();

		/* Set TLS 1.3 only in an SSLParameters object. */
		parameters.setProtocols(tunnelingProtocols);

		/* Apply the parameters to an SSLSocket object */
		//socket.setSSLParameters(parameters);


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
		//socket.startHandshake();

		return new SMTP_Connection(this, socket, isVerbose);
	}
	
	


}
