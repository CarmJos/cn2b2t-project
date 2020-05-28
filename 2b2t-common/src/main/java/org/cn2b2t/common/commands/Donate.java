package org.cn2b2t.common.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.cn2b2t.common.Main;
import org.cn2b2t.common.functions.ProfileData;
import org.cn2b2t.common.managers.DataManager;
import org.cn2b2t.common.managers.DonateManager;
import org.cn2b2t.core.utils.ColorParser;

import java.util.ArrayList;
import java.util.List;

public class Donate implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("donate") && sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length >= 1) {
                String aim = args[0];
                if (aim.equalsIgnoreCase("me")) {
                    help(p, 1);
                } else if (aim.equalsIgnoreCase("info")) {
                    help(p, 2);
                } else if (aim.equals("1") || aim.equals("5") || aim.equals("10") || aim.equals("20") || aim.equals("50")) {
                    Main.getInstance().getServer().dispatchCommand(Bukkit.getConsoleSender(), "mcpay pbuy " + sender.getName() + " " + DonateManager.getID(aim));
                } else {
                    help(p, 0);
                }
            } else {
                help(p, 0);
            }
        } else if (cmd.getName().equalsIgnoreCase("donate")) {
            if (args.length == 4) {
                // donate apply adehcby 20200306194633048611812 20
                String playername = args[1];
                String order = args[2];
                String price = args[3];
                Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(ColorParser.parse(
                        "&6-&e--&8----------------------------&e--&6-\n" +
                                "&7感谢 &6&l" + playername + "&7为本服助力 &6" + price + "元 &7!\n" +
                                "&7本服仅由玩家赞助维持,输入 &6/donate &7查看更多。\n" +
                                "&6-&e--&8----------------------------&e--&6-"
                )));
                DataManager.saveData(Bukkit.getPlayer(playername), order, (int) Double.parseDouble(price));

            }
            sender.sendMessage(ColorParser.parse(" \n本月赞助 &6" + DonateManager.monthly + "\n" +
                    "总共收到 &6" + DonateManager.total));
        }
        return true;
    }

    private static void help(Player p, int i) {
        switch (i) {
            case 1: {
                ProfileData profileData = ProfileData.get(p);
                p.sendMessage(ColorParser.parse("&6-&e--&8----------------------------&e--&6-\n" +
                        "&7您总共赞助了 &6" + profileData.getTotalDonate() + " &7元;\n" +
                        "&7至今一个月内，您为服务器提供了 &6" + profileData.getMonthlyDonate() + " &7元资金。\n" +
                        "&7总共为服务器续命 &6" + profileData.getTotalDonate() / 50 + " &7天。\n" +
                        (profileData.getTotalDonate() > 0 ? "&7感谢您为本社区所做的一切！\n" : "\n") +
                        "&6-&e--&8----------------------------&e--&6-"));
                break;
            }
            case 2: {
                p.sendMessage(ColorParser.parse("&6-&e--&8----------------------------&e--&6-\n" +
                        "&7本服&c无管理维护，仅由玩家赞助维持&7。\n" +
                        "&7所收赞助钱款自动化处理，全部转账到服务商账户中。\n" +
                        "&7若您还未有相应消费能力，&c谢绝赞助，心意已领&7。\n" +
                        "&7至今一个月内 赞助&6超过5元的&7即可获得 &e浅黄色 &7聊天名，\n" +
                        "&7至今一个月内 赞助&6超过10元&7的可以延迟服务器重启。\n" +
                        "&7总共赞助&6超过50元&7的，即使本服务器已满人，也可进入。\n" +
                        "&6-&e--&8----------------------------&e--&6-"));
                break;
            }
            default: {
                p.sendMessage(ColorParser.parse("&6-&e--&8----------------------------&e--&6-\n" +
                        "&7本服自2019年9月1日起，开始接受玩家资助。\n" +
                        "&7本月获得赞助 &6" + DonateManager.monthly + "元&7，总共获得 &6" + DonateManager.total + "元&7。\n" +
                        "&7查看介绍 &6/donate info\n" +
                        "&7查看我的贡献 &6/donate me\n" +
                        "&7赞助对应金额 &6/donate 1/5/10/20/50\n" +
                        "&c注意：&7目前仅支持单笔1元、5元、 10元、20元、50元，\n" +
                        "     &7请勿输入其他金额。\n" +
                        "&6-&e--&8----------------------------&e--&6-"));
                break;
            }

        }

    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1: {
                List<String> completions = new ArrayList<>();
                String[] strings = new String[]{"info", "me", "1", "5", "10"};
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
