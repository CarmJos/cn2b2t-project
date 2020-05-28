package org.cn2b2t.common.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.cn2b2t.common.functions.ProfileData;
import org.cn2b2t.core.utils.ColorParser;

import java.util.ArrayList;
import java.util.List;

public class Settings implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("settings") && sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length >= 2 && (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false"))) {
                String aim = args[0];
                boolean value = args[1].equalsIgnoreCase("true");

                if (aim.equalsIgnoreCase("showScoreboard") || aim.equalsIgnoreCase("ssb")) {
                    ProfileData.get(p).setShowScoreboard(value);
                } else if (aim.equalsIgnoreCase("showDeathMessages") || aim.equalsIgnoreCase("sdm")) {
                    ProfileData.get(p).setShowDeathMessages(value);
                } else if (aim.equalsIgnoreCase("showJoinAndLeaveAlerts") || aim.equalsIgnoreCase("sjaqa")) {
                    ProfileData.get(p).setShowJoinAndLeaveAlerts(value);
                } else {
                    help(p);
                }
            } else {
                help(p);
            }
        }
        return true;
    }

    private static void help(Player p) {
        p.sendMessage(ColorParser.parse("&6-&e--&8----------------------------&e--&6-\n" +
                "&7开关积分榜\n" +
                "     &6/settings showScoreboard true/false\n" +
                "&7开关死亡消息\n" +
                "     &6/settings showDeathMessages true/false\n" +
                "&7开关玩家加入、退出提示信息\n" +
                "     &6/settings showJoinAndLeaveAlerts true/false\n" +
                "&6-&e--&8----------------------------&e--&6-"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1: {
                List<String> completions = new ArrayList<>();
                String[] strings = new String[]{"showScoreboard", "showDeathMessages", "showJoinAndLeaveAlerts"};
                for (String s : strings) {
                    if (StringUtil.startsWithIgnoreCase(s, args[0].toLowerCase())) {
                        completions.add(s);
                    }
                }
                return completions;
            }
            case 2: {
                List<String> completions = new ArrayList<>();
                String[] strings = new String[]{"true", "false"};
                for (String s : strings) {
                    if (StringUtil.startsWithIgnoreCase(s, args[0].toLowerCase())) {
                        completions.add(s);
                    }
                }
                return completions;
            }
            default:
                return ImmutableList.of();
        }
    }
}
