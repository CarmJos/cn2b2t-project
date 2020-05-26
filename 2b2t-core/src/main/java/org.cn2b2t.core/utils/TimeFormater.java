package org.cn2b2t.core.utils;

public class TimeFormater {

	public static String timeLeft(long t) {
		long left = t - System.currentTimeMillis();
		if (left < 0) {
			return "已过期";
		} else if (left < 3600000) {
			return "小于1时";
		}
		StringBuilder sb = new StringBuilder();
		if (left > 31536000000L) {
			sb.append(left / 31536000000L).append("年");
			left %= 31536000000L;
		}
		if (left > 2592000000L) {
			sb.append(left / 2592000000L).append("月");
			left %= 2592000000L;
		}
		if (left > 86400000L) {
			sb.append(left / 86400000L).append("天");
			left %= 86400000L;
		}
		if (left > 3600000) {
			sb.append(left / 3600000).append("时");
		}
		return sb.toString();
	}

	public static String getTime(long t) {
		StringBuilder sb = new StringBuilder();
		t /= 1000;
		if (t > 3600) {
			sb.append(t / 3600).append("h");
		}
		if (t > 60) {
			sb.append(t % 3600 / 60).append("min");
		}
		sb.append(t % 60).append("s");
		return sb.toString();
	}

}
