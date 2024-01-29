/**
 * 
 */
/**
 * 
 */


module com.s8.core.web.manganese {
	
	
	exports com.s8.core.web.manganese;

	
	requires java.mail;
	
	/**
	 * he warning is shown because the JAR you refer as jasperreprots module does
	 * not contain a module-info.class nor an Automatic-Module-Name: ... entry in
	 * the MANIFEST.MF.
	 * 
	 * So the module name jasperreports is derived from the file name of the JAR.
	 * Renaming the JAR can change the module name breaking your module-info.java as
	 * requires jasperreports; would also need to be adapted to the new JAR file
	 * name and then the changed module-info.java to be compiled again. That is why
	 * the warning says the module name that is automatically derived from the file
	 * name of the JAR is unstable.
	 * 
	 * Nowadays, almost all dependencies contain at least a MANIFEST.MF with
	 * Automatic-Module-Name. Most likely you are using an outdated dependency built
	 * for Java 8 or lower.
	 */
	//requires javax.activation;
	
	
	
	/*
	import com.sun.mail.smtp.SMTPTransport;
	import com.sun.mail.util.BASE64EncoderStream;

	import javax.mail.Message;
	import javax.mail.Session;
	import javax.mail.internet.InternetAddress;
	import javax.mail.internet.MimeMessage;
	import java.util.Properties;
	import java.util.logging.Level;
	import java.util.logging.Logger;
	*/
	
	requires transitive com.s8.api;
	requires transitive com.s8.core.io.xml;
	requires transitive com.s8.core.arch.silicon;
	
	
	requires java.datatransfer;
	requires java.logging;
	requires java.desktop;
	requires java.security.sasl;
	
	
	
}