package org.cn2b2t.proxy;

import org.cn2b2t.proxy.utils.FileConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {


	public static List<String> illegalWords;


	public static FileConfig yamen;

	public static String database_SQLhost;
	public static int database_SQLport;
	public static String database_SQLdata;
	public static String database_user;
	public static String database_password;

	public static String reason_default;

	public static Map<String/*Layout*/, String/*Value*/> layouts = new HashMap<>();

	public static List<String> servers;


	public static void load() {

		illegalWords = new FileConfig(Main.getInstance(), "illegalWords.yml").getStringList("words");


		//yamen
		yamen = new FileConfig(Main.getInstance(), "yamen.yml");

		database_SQLhost = yamen.getString("Database.SQLhost");
		database_SQLport = yamen.getInt("Database.SQLport");
		database_SQLdata = yamen.getString("Database.SQLdata");
		database_user = yamen.getString("Database.user");
		database_password = yamen.getString("Database.password");

		reason_default = yamen.getString("DefaultReason").replace("&", "§");

		yamen.getSection("Layouts").getKeys().forEach((s) -> {
			StringBuilder layoutBuilder = new StringBuilder();
			yamen.getStringList("Layouts." + s).forEach((layout) -> {
				layoutBuilder.append(layout);
				layoutBuilder.append("\n");
			});
			layouts.put(s, layoutBuilder.toString().replace("&", "§").replace("%(space)", "\n "));
		});

		servers = yamen.getStringList("Servers");

	}


}
