/**
 * 
 */
/**
 * 
 */


module com.s8.core.web.manganese {
	

	
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
	requires javax.activation;
	
	
	
}