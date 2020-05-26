package org.cn2b2t.proxy.functions.permission;

import org.cn2b2t.proxy.Main;
import org.cn2b2t.proxy.utils.FileConfig;
import net.md_5.bungee.config.Configuration;

/**
 * @author cam
 */
public class PermConfig {

	// 只加载"@"和“Bungee"库权限

	public static Configuration permConfig;


	public static void load() {
		permConfig = null;
		PermConfig.permConfig = new FileConfig(Main.getInstance(), "perm.yml").getConfig();


	}

}
