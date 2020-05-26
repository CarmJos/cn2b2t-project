package org.cn2b2t.proxy.functions.permission;

import java.util.List;

public class DataPermissionGroup {

	public final int id;
	public final List<Integer> extend;
	public final List<DataPermission> perms;

	public DataPermissionGroup(int id, List<Integer> extend, List<DataPermission> perms) {
		this.id = id;
		this.extend = extend;
		this.perms = perms;
	}
}
