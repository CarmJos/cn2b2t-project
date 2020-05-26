package org.cn2b2t.proxy.functions.permission;

import org.cn2b2t.proxy.Main;
import org.cn2b2t.proxy.commands.ProxyPermdog;
import org.cn2b2t.proxy.listeners.OveridePermissionsListener;

public class PermissionMain {

	public static void init() {
		PermConfig.load();
		PermissionDataManager.init();

		Main.regListener(new OveridePermissionsListener());
		Main.regCommmand(new ProxyPermdog("proxypermdog"));
	}

	public static void disable() {
	}

}
