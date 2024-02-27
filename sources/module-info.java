/**
 * 
 */
module com.s8.core.web.manganese {
	
	
	exports com.s8.core.web.manganese;
	exports com.s8.core.web.manganese.css;
	exports com.s8.core.web.manganese.html;
	exports com.s8.core.web.manganese.mime;

	exports com.s8.core.web.manganese.sasl;
	exports com.s8.core.web.manganese.smtp;
		
	requires transitive com.s8.api;
	requires transitive com.s8.core.io.xml;
	requires transitive com.s8.core.arch.silicon;

}