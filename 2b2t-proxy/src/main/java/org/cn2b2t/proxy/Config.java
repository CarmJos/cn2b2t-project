package org.cn2b2t.proxy;

import org.cn2b2t.proxy.utils.FileConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {


	public static List<String> illegalWords;


	public static void load() {

		illegalWords = new FileConfig(Main.getInstance(), "illegalWords.yml").getStringList("words");

	}


}
