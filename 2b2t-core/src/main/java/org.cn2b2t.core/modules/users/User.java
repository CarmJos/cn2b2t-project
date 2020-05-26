package org.cn2b2t.core.modules.users;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.cn2b2t.core.Main;
import org.cn2b2t.core.events.UserLoadedEvent;
import org.cn2b2t.core.managers.utils.DataManager;
import org.cn2b2t.core.managers.utils.UserManager;
import org.cn2b2t.core.managers.utils.scoreboard.CeramicScoreboard;
import org.cn2b2t.core.managers.utils.scoreboard.ScoreBoardRender;
import org.cn2b2t.core.modules.gui.GUI;
import org.cn2b2t.core.utils.UUIDUtils;
import org.spigotmc.AsyncCatcher;

import java.sql.ResultSet;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User {

    private final Player player;

    private int inkID;
    private UUID mojangUUID;
    private boolean onlineMode;

    private Map<String, Object> handler = new HashMap<>();

    public GUI openedGUI;

    ScoreBoardRender sbRender;
    List<CeramicScoreboard> scoreboards = new ArrayList<>();


    public boolean fullLoaded;

    public User(Player player) {
        this.player = player;

        new BukkitRunnable() {
            @Override
            public void run() {

                UserManager.getHandlers().forEach((key, value) -> registerHandler(key, value));

                UserManager.getHandlerClasses().forEach((key, value) -> {
                    try {
                        registerHandler(value);
                    } catch (Exception ex) {
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });


                try {
                    ResultSet accountResult = DataManager.getConnection().SQLquery("nl2_users",
                            "uuid", player.getUniqueId().toString().replace("-", ""));
                    if (accountResult != null) {
                        if (accountResult.next()) {
                            User.this.inkID = accountResult.getInt("id");
                            String muuid = accountResult.getString("mojanguuid");
                            if (muuid != null && !muuid.isEmpty() && !muuid.equalsIgnoreCase("null")) {
                                User.this.mojangUUID = UUIDUtils.toUUID(muuid);
                            }
                            User.this.onlineMode = mojangUUID != null;
                        } else {
                            accountResult.close();
                            throw new NullPointerException("Cannot find user profile. name=" + player.getName() + " uuid=" + player.getUniqueId());
                        }
                        accountResult.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            getPlayer().kickPlayer("关键信息加载失败,请联系管理员\nKey information loading failed. Please turn to administrator.");
                        }
                    }.runTask(Main.getInstance());
                }


                new BukkitRunnable() {
                    @Override
                    public void run() {
                        fullLoaded = true;
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                sbRender = new ScoreBoardRender(player);
                            }
                        }.runTaskLater(Main.getInstance(), 1L);
                        Bukkit.getPluginManager().callEvent(new UserLoadedEvent(User.this));
                    }
                }.runTask(Main.getInstance());


                cancel();
            }
        }.runTaskAsynchronously(Main.getInstance());
    }


    public boolean isFullLoaded() {
        return fullLoaded;
    }

    public boolean isOnlineMode() {
        return onlineMode;
    }

    public UUID getMojangUUID() {
        return mojangUUID;
    }

    public List<CeramicScoreboard> getScoreboards() {
        return this.scoreboards;
    }

    public Player getPlayer() {
        return player;
    }

    public void unregister() {
        //此操作疑似会重复运行onDisable();
//		for (Map.Entry<String, Object> e : UserManager.handlers.entrySet()) {
//			if(AbstractUserHandler.class.isAssignableFrom(e.getValue().getClass()))
//				((AbstractUserHandler) (e.getValue())).onDisable();
//		}
        this.openedGUI = null;
        this.handler.clear();
        this.handler = null;
        this.scoreboards.clear();
        this.scoreboards = null;
        this.sbRender = null;
        UserManager.unregisterUser(this.getPlayer());
    }

    public <T extends AbstractUserHandler> T getHandler(Class<T> c) {
        if (isUnregistered()) {
            AsyncCatcher.catchOp("handler get when user unloaded");
        }
        int position = c.getName().lastIndexOf('.');
        String name = c.getName().substring(position + 1);
        if (containsHandler(name)) {
            return (T) handler.get(name);
        } else {
            throw new NullPointerException("Handler " + name + " in User " + getPlayer().getName() + " wasn't found.");
        }
    }

    public Object getHandler(String s) {
        if (isUnregistered()) {
            AsyncCatcher.catchOp("handler get when user unloaded");
        }
        if (containsHandler(s)) {
            return handler.get(s);
        } else {
            throw new NullPointerException("Handler " + s + " in User " + getPlayer().getName() + " wasn't found.");
        }
    }

    public int getInkID() {
        return inkID;
    }

    public <T> T getHandler(String s, Class T) {
        if (isUnregistered()) {
            AsyncCatcher.catchOp("handler get when user unloaded");
        }
        if (containsHandler(s)) {
            return (T) handler.get(s);
        } else {
            throw new NullPointerException("Handler " + s + " in User " + getPlayer().getName() + " wasn't found.");
        }
    }

    public boolean containsHandler(String s) {
        return handler.containsKey(s);
    }

    public boolean containsHandler(Class<? extends AbstractUserHandler> c) {
        int position = c.getName().lastIndexOf('.');
        return handler.containsKey(c.getName().substring(position + 1));
    }

    public void registerHandler(Class<? extends AbstractUserHandler> c) {
        if (isUnregistered()) {
            throw new IllegalStateException("register Handler when user unloaded");
        }
        if (containsHandler(c)) {
            throw new RuntimeException("Handler " + c.getName() + " in User " + getPlayer().getName() + " is existed.");
        } else {
            try {
                AbstractUserHandler h = c.newInstance();
                h.user = this;
                h.init();
                int position = c.getName().lastIndexOf('.');
                this.handler.put(c.getName().substring(position + 1), h);
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void registerHandler(String name, Object o) {
        if (isUnregistered()) {
            throw new IllegalStateException("register Handler when user unloaded");
        }
        if (containsHandler(name)) {
            throw new RuntimeException("Handler " + name + " in User " + getPlayer().getName() + " is existed.");
        } else {
            this.handler.put(name, o);
        }
    }

    public void removeHandler(String name) {
        if (!containsHandler(name)) {
            throw new RuntimeException("Handler " + name + " in User " + getPlayer().getName() + " wasn't existed.");
        } else {
            Object h = this.handler.get(name);
            if (h instanceof AbstractUserHandler) {
                AbstractUserHandler absh = (AbstractUserHandler) h;
                absh.onDisable();
            }
            this.handler.remove(name);
        }
    }

    public void removeHandler(Class<? extends AbstractUserHandler> c) {
        if (!containsHandler(c)) {
            throw new RuntimeException("Handler " + c.getName() + " in User " + getPlayer().getName() + " wasn't existed.");
        } else {
            int position = c.getName().lastIndexOf('.');
            String name = c.getName().substring(position + 1);
            Object h = this.handler.get(name);
            if (h instanceof AbstractUserHandler) {
                AbstractUserHandler absh = (AbstractUserHandler) h;
                absh.onDisable();
            }
            this.handler.remove(name);
        }
    }

    public ScoreBoardRender getSbRender() {
        return sbRender;
    }

    public void addScoreboard(CeramicScoreboard sb) {
        this.scoreboards.add(sb);
    }

    public void replaceHandler(String name, Object o) {
        if (!containsHandler(name)) {
            throw new RuntimeException("Handler " + name + " in User " + getPlayer().getName() + " was existed.");
        } else {
            this.handler.replace(name, o);
        }
    }

    public boolean isUnregistered() {
        return !UserManager.getRegisteredUsers().contains(this);
    }


}
