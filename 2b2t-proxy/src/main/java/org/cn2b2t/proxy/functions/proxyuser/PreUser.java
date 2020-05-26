package org.cn2b2t.proxy.functions.proxyuser;

import org.cn2b2t.proxy.functions.Account;
import org.cn2b2t.proxy.managers.DataManager;
import org.cn2b2t.proxy.utils.UUIDUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PreUser {

	private String name;
	private boolean exist;
	private UUID uuid;
	private UUID mojangUUID;
	long registerTime;

	PreUser(String name){
		this.name = name;
		UUID[] uuids = Account.getUUIDandMojangUniqueID(name);
		if(uuids == null){
			this.exist = false;
		} else {
			this.exist = true;
			this.uuid = uuids[0];
			this.mojangUUID = uuids[1];
		}
		registerTime = System.currentTimeMillis();
	}

	public PreUser(UUID mojangUUID){
		this.mojangUUID = mojangUUID;
		if(mojangUUID == null){
			exist = false;
			uuid = null;
			return;
		}
		ResultSet result = DataManager.getWebsiteConnection().SQLquery("select * from nl2_users where mojanguuid = '"+mojangUUID.toString().replace("-","")+"'");
		try {
			if(result != null){
				if(result.next()){
					exist = true;
					String name = result.getString("username");
					UUID uuid = UUIDUtils.toUUID(result.getString("uuid"));
					this.name = name;
					this.uuid = uuid;
				}
				result.close();
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		registerTime = System.currentTimeMillis();
	}

	public String getName() {
		return name;
	}

	public boolean exist(){
		return exist;
	}

	public UUID getUUID(){
		return uuid;
	}

	public UUID getMojangUUID(){
		return mojangUUID;
	}

	public boolean isOnlineMode(){
		return mojangUUID != null;
	}

	public boolean isPremium(){
		String mes = "select * from `uservalues` where `inkid` = '" + Account.getInkID(name) + "' AND `key` = 'verPremium'";
		ResultSet query = DataManager.getTempConnection().SQLquery(mes);
		try {
			if (query != null) {
				String value = null;
				if (query.next()) {
					value = query.getString("value");
				}
				query.close();
				return value != null && value.equalsIgnoreCase("true");
			}
		} catch (SQLException e) {
			DataManager.getTempConnection().info(e.getLocalizedMessage());
			System.out.println("code: "+mes);
		}
		return false;
	}

	public void readyPremium(){
		DataManager.getTempConnection().update("uservalues",
				new String[]{"value"}, new Object[]{"ready"},
				new String[]{"key", "inkid"}, new Object[]{"verPremium", Account.getInkID(name)});
	}

}
