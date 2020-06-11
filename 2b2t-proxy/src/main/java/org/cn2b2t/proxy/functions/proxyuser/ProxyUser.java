package org.cn2b2t.proxy.functions.proxyuser;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.scheduler.BungeeScheduler;
import org.cn2b2t.proxy.Main;
import org.cn2b2t.proxy.functions.Account;
import org.cn2b2t.proxy.functions.serverinfo.ServerInfoConfig;
import org.cn2b2t.proxy.managers.DataManager;
import org.cn2b2t.proxy.managers.UserValueManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProxyUser {


    private String name;
    private int inkID;
    private UUID mojangUUID;
    private UUID gameUUID;


    private boolean isRegistered;

    private int version;

    private boolean hasTarget;
    private ServerInfo serverTarget;

    private String targetServerName;


    private Map<String, String> values;


    public ProxyUser(String userName, String connectedIP, Integer version, UUID uuid, UUID mojangUUID) {
        this.name = userName;
        this.targetServerName = ServerInfoConfig.getServerInfo(connectedIP).getTargetServer();

        this.version = version;


        isRegistered = Account.isRegistered(userName);
        if (isRegistered) {
            inkID = Account.getInkID(name);
            this.mojangUUID = mojangUUID;
            gameUUID = uuid;
            values = UserValueManager.getDataValues(inkID);
        } else {
            inkID = -1;
            this.mojangUUID = mojangUUID;
            values = new HashMap<>();
        }


        if (!isOnlineMode() && !ServerInfoConfig.getServerInfo(connectedIP).isDirectTeleport()) {
            new BungeeScheduler().runAsync(Main.getInstance(), () -> {
                DataManager.putTarget(userName, targetServerName);
            });
        }

        new BungeeScheduler().runAsync(Main.getInstance(), this::createTempUser);

    }

    public UUID getGameUUID() {
        return gameUUID;
    }

    public int getInkID() {
        return inkID;
    }

    public boolean isRegistered() {
        return isRegistered;
    }


    public Map<String, String> getValues() {
        return values;
    }

    public void setServerTarget(String server) {
        this.serverTarget = BungeeCord.getInstance().getServerInfo(server) != null ?
                BungeeCord.getInstance().getServerInfo(server) : BungeeCord.getInstance().getServerInfo(ServerInfoConfig.defaultInfo.getTargetServer());
        hasTarget = true;
    }

    public ServerInfo getServerTarget() {
        hasTarget = false;
        return serverTarget;
    }

    public boolean isOnlineMode() {
        return mojangUUID != null;
    }

    public boolean isRealOnlineMode() {
        return ProxyServer.getInstance().getPlayer(name).getPendingConnection().isOnlineMode();
    }

    public UUID getMojangUUID() {
        return mojangUUID;
    }

    public String getName() {
        return name;
    }

    public void createTempUser() {
        ResultSet query = DataManager.getTempConnection().SQLquery("tempuser", "name", name);
        try {
            boolean exist = false;
            if (query != null) {
                if (query.next()) {
                    exist = true;
                    new BungeeScheduler().runAsync(Main.getInstance(), () -> DataManager.getTempConnection()
                            .update("tempuser",
                                    new String[]{"name", "isonlinemode", "version"},
                                    new Object[]{getName(), isRealOnlineMode() ? 1 : 0, version},
                                    new String[]{"name"},
                                    new Object[]{name}));
                }
                query.close();
            }
            if (!exist) {
                new BungeeScheduler().runAsync(Main.getInstance(), () -> DataManager.getTempConnection()
                        .insert("tempuser",
                                new String[]{"name", "isonlinemode", "version"},
                                new Object[]{getName(), isRealOnlineMode() ? 1 : 0, version}));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getTargetServerName() {
        return targetServerName;
    }

    public boolean hasTarget() {
        return hasTarget;
    }


}
