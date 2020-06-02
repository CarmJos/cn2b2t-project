package org.cn2b2t.core.managers.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.cn2b2t.core.Main;
import org.cn2b2t.core.listener.ServersUpdateListener;
import org.cn2b2t.core.modules.DataServerInfo;
import org.cn2b2t.core.modules.LocalServerInfo;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServersManager {

    public static List<Player> requests = new ArrayList<>();

    public static String databaseTable = "game_servers";
    public static String messageChannel = "serversmanager";

    private static LocalServerInfo localRegisteredServer;

    private static Map<String/*serverID*/, DataServerInfo> dataServers = new HashMap<>();

    private static BukkitRunnable updateRunnable;

    public static void init() {
        Main.regListener(new ServersUpdateListener());



        updateRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() > 0) updateDataServers();
            }
        };

        getUpdateRunnable().runTaskTimer(Main.getInstance(), 20L, 100L);
    }

    public static void registerServer(String serverID, String hostName, int port, String serverGroup) {
        localRegisteredServer = new LocalServerInfo(serverID, hostName, port, serverGroup);
        localRegisteredServer.register();
    }

    public static void unregisterServer() {
        if (getLocalRegisteredServer() != null) {
            getLocalRegisteredServer().unregister();
            localRegisteredServer = null;
        }
    }

    public static void updateDataServers() {
        Map<String, DataServerInfo> tempDataServers = new HashMap<>();
        ResultSet rs = DataManager.getConnection().SQLqueryInTable(ServersManager.databaseTable);
        try {
            if (rs != null) {
                while (rs.next()) {
                    String serverID = rs.getString("serverid");
                    String hostName = rs.getString("hostname");
                    String serverGroup = rs.getString("servergroup");
                    int players = rs.getInt("players");
                    int port = rs.getInt("port");
                    tempDataServers.put(serverID, new DataServerInfo(serverID, hostName, port, serverGroup, players));
                }
                rs.close();
            }
        } catch (SQLException e) {
            DataManager.getConnection().info(e.getLocalizedMessage());
            Bukkit.getLogger().log(Level.WARNING, "code: {0}", rs);
        }
        dataServers = tempDataServers;
    }

    public static Map<String, DataServerInfo> getDataServers() {
        return dataServers;
    }

    public static Integer getPlayers(String serverName) {
        return getPlayers(serverName, false);
    }

    public static Integer getPlayers(String server, boolean isGroup) {
        if (server.equalsIgnoreCase("#ALL") || server.equalsIgnoreCase("#AII")) {
            return getDataServers().keySet().stream()
                    .mapToInt(s -> getDataServers().getOrDefault(s, new DataServerInfo(null, null, -1, null, 0)).getPlayers())
                    .sum();
        } else {
            if (isGroup) {
                return getDataServers().keySet().stream()
                        .filter(s -> getDataServers().get(s).getServerGroup().equalsIgnoreCase(server))
                        .mapToInt(s -> getDataServers().getOrDefault(s, new DataServerInfo(null, null, -1, null, 0)).getPlayers())
                        .sum();
            } else {
                return getDataServers().getOrDefault(server, new DataServerInfo(null, null, -1, null, 0)).getPlayers();
            }
        }

    }

    public static BukkitRunnable getUpdateRunnable() {
        return updateRunnable;
    }

    public static void teleport(Player p, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("ConnectOther");
            out.writeUTF(p.getName());
//            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        p.sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
    }

    public static void teleport(Player p, String server, int t) {
        requests.add(p);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (requests.contains(p) && p.isOnline()) {
                    teleport(p, server);
                    requests.remove(p);
                }
                cancel();
            }
        }.runTaskLater(Main.getInstance(), 20L * t);
    }

    public static void backToLobby(Player p) {
        teleport(p, "SYSTEM.MAINLOBBY");
    }


    public static void goAFK(Player p) {

        DataServerInfo targetServer = getServerInfo("LIMBO");
        if (targetServer != null) {
            teleport(p, targetServer.getServerID());
        } else {
            p.sendMessage("§7暂无可用挂机服务器。");
        }


    }


    public static DataServerInfo getServerInfo(String name) {
        if (getDataServers().containsKey(name)) {
            return getDataServers().get(name);
        } else {
            for (DataServerInfo info : getDataServers().values()) {
                if (info.getServerGroup().equalsIgnoreCase(name)) {
                    return info;
                }
            }
            return null;
        }
    }


    public static LocalServerInfo getLocalRegisteredServer() {
        return localRegisteredServer;
    }

    public static void switchServer(Player p, String targetServerIp) {
        p.sendPluginMessage(Main.getInstance(), "BeeSwitch", targetServerIp.getBytes(StandardCharsets.UTF_8));
    }

    public enum Operation {
        ADD,
        REMOVE;

        public static Operation tryRead(String name) {
            for (Operation value : Operation.values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return value;
                }
            }
            return null;
        }
    }


}
