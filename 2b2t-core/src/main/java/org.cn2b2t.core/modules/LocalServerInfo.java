package org.cn2b2t.core.modules;

import org.bukkit.Bukkit;
import org.cn2b2t.core.managers.utils.DataManager;
import org.cn2b2t.core.managers.utils.ServersManager;

public class LocalServerInfo extends DataServerInfo {
    public LocalServerInfo(String serverID, String hostName, int port, String serverGroup) {
        super(serverID, hostName, port, serverGroup, Bukkit.getOnlinePlayers().size());


    }

    @Override
    public int getPlayers() {
        return Bukkit.getOnlinePlayers().size();
    }

    public void register() {
        writeInfo();
        if (!this.getServerGroup().equalsIgnoreCase("SYSTEM")) {
            sendRegisterRequest();
        }

    }

    public void unregister() {
        deleteInfo();
        if (!this.getServerGroup().equalsIgnoreCase("SYSTEM")) {
            sendUnregisterRequest();
        }
    }

    public void writeInfo() {
        DataManager.getConnection().update(ServersManager.databaseTable,
                new String[]{"servergroup", "hostname", "port", "players"},
                new Object[]{LocalServerInfo.this.getServerGroup(), LocalServerInfo.this.getHostName(), LocalServerInfo.this.getPort(), getPlayers()},
                new String[]{"serverid"}, new Object[]{LocalServerInfo.this.getServerID()});
    }

    public void deleteInfo() {
        DataManager.getConnection().delete(ServersManager.databaseTable, "serverid", LocalServerInfo.this.getServerID());
    }


    public void sendRegisterRequest() {
        DataManager.getTempConnection().insert("messager_" + ServersManager.messageChannel,
                new String[]{"start", "lasttime", "sign", "message"},
                new Object[]{System.currentTimeMillis(), 1500, ServersManager.messageChannel, ServersManager.Operation.ADD.name() + ":" + LocalServerInfo.this.getServerID()});
    }

    public void sendUnregisterRequest() {
        DataManager.getTempConnection().insert("messager_" + ServersManager.messageChannel,
                new String[]{"start", "lasttime", "sign", "message"},
                new Object[]{System.currentTimeMillis(), 1500, ServersManager.messageChannel, ServersManager.Operation.REMOVE.name() + ":" + LocalServerInfo.this.getServerID()});
    }


}
