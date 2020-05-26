package cn.moci.ig.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cn2b2t.core.managers.utils.ServersManager;
import org.cn2b2t.core.utils.ColorParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 最终解释权归 墨瓷工作室 。
 *
 * @author Cam, LSeng.
 */
public class IG implements CommandExecutor {

    public static List<String> queue = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            return true;
        } // /IG condition KUMR killaura 1000 5 -1 Hitbox(Killaura) kick %(Player) #ac Task #00001 for IG generated an exception；
        if (args.length < 1) {
            return true;
        }
        switch (args[0]) {
            case "notify": {
                // /IG notify {player} HackType
                if (args.length < 2) {
                    return true;
                }
                String username = args[1];
                String reason = Arrays.stream(args, 2, args.length - 1).collect(Collectors.joining());
                return true;
            }
            case "punish": {
                // /IG punish {player} HackType
                String username = args[1];
                if (Bukkit.getPlayer(username) != null) {
                    Player p = Bukkit.getPlayer(username);
                    if (!p.hasPermission("ig.bypass")) {
                        ServersManager.goAFK(p);
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            onlinePlayer.sendMessage(ColorParser.parse("&7玩家 &f" + p.getName() + " &7因&c疑似作弊&7被移出本局游戏。"));
                            onlinePlayer.sendMessage(ColorParser.parse("&7保持良好游戏环境需要各位的努力，感谢各位的支持。"));
                        }
                    }
                }

                return true;
            }
            default: {
                return true;
            }

        }
    }

}
