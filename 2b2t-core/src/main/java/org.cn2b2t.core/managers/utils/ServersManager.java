package org.cn2b2t.core.managers.utils;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.cn2b2t.core.Main;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServersManager {

    public static List<Player> requests = new ArrayList<>();

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
        teleport(p, "LIMBO");

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
