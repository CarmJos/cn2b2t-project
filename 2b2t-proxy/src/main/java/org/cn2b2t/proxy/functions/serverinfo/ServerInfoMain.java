package org.cn2b2t.proxy.functions.serverinfo;

import org.cn2b2t.proxy.Main;
import org.cn2b2t.proxy.commands.ProxyInfos;
import org.cn2b2t.proxy.listeners.FixingMode;
import org.cn2b2t.proxy.listeners.ProxyPingListener;

public class ServerInfoMain {

	public static void init() {
		ServerInfoConfig.load();

		Main.regCommmand(new ProxyInfos("proxyinfos"));
		Main.regListener(new ProxyPingListener());
		if (ServerInfoConfig.fixing) Main.regListener(new FixingMode());


	}


}
