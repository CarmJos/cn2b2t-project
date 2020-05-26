package org.cn2b2t.proxy.utils;

import java.util.UUID;

public class UUIDUtils {

	public static UUID toUUID(String s){
		if(s.length() == 36){
			return UUID.fromString(s);
		} else {
			StringBuilder sb = new StringBuilder(36);
			sb.append(s, 0, 8)
					.append('-')
					.append(s, 8, 12)
					.append('-')
					.append(s, 12, 16)
					.append('-')
					.append(s, 16, 20)
					.append('-')
					.append(s.substring(20));
			return UUID.fromString(sb.toString());
		}
	}

}
