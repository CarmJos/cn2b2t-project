package org.cn2b2t.proxy.utils;

public class MathMethods {

	public static int getFakePlayer(int online) {
		if (online <= 2) return (int) (online * 3.5);
		if (online <= 16) return (int) (online * 2.5);
		if (online <= 64) return (int) (online * 1.5);
		return online;
	}


}
