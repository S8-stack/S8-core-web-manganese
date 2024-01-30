package com.s8.core.web.manganese;

import com.s8.core.web.manganese.generator.CSS_ClassBase;

public class CSS_ParsingTest02 {

	public static void main(String[] args) {
		
		
		CSS_ClassBase base = new CSS_ClassBase();
		
		base.parseFile("/Users/pc/qx/git/S8-core-web-manganese/web-sources/S8-core-web-manganese/mg-mail-style.css");
		base.DEBUG_print();
	}



}
