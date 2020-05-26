package org.cn2b2t.core.utils;

public class ColorParser {

	public static String parse(final String text) {
		return text.replaceAll("&", "§").replace("§§", "&");
	}

}
