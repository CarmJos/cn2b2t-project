package org.cn2b2t.common.runnables;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.cn2b2t.common.Main;

import java.util.ArrayList;
import java.util.List;

public class BoardCastRunnable {

    private List<String> messages;
    private int index = 0;

    public BoardCastRunnable() {
        this.messages = new ArrayList<>();
        messages.add(Main.color("&8[&a&l?&8] &7您可以输入 &6/help &7获得帮助消息。"));
        messages.add(Main.color("&8[&a&l?&8] &7遇到无脑刷屏？不妨试试 &6/ignore &7屏蔽该玩家吧！"));
        messages.add(Main.color("&8[&a&l?&8] &7想要为维持服务器贡献一份力？输入 &6/donate &7查看详情！"));
        messages.add(Main.color("&8[&a&l?&8] &7你也想要&e浅黄色&7的名字？输入 &6/donate &7查看如何获得！"));
        messages.add(Main.color("&8[&a&l?&8] &7本服需要玩家自行续费与维护，每月集齐50元即可续命一个月，输入 &6/donate &7查看详情！"));
        messages.add(Main.color("&8[&c&l!&8] &7服务器难免出现Bug，若您发现，请将相关详情发送至邮件 &6carm@carmwork.com &7，感谢您的帮助！"));
        messages.add(Main.color("&8[&c&l!&8] &7服务器会自动检测并清理大面积无意义高配红石(如红石灯矩阵)，请勿恶意卡服。"));
        messages.add(Main.color("&8[&c&l!&8] &7本服无OP、管理。&6遇事冷静，要学会自行处理。&7"));
        messages.add(Main.color("&8[&c&l!&8] &7本服禁用黑客端的崩服(Crash)功能，如有发现，&c立刻封禁，恕不解封！&7"));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() > 1) {
                    String message = messages.get(index);
                    if (index >= messages.size()) {
                        index = 0;
                    } else {
                        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(" \n" + message + " \n "));
                        index = index + 1;
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 600L, 6000L);
    }
}
