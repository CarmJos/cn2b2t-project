package org.cn2b2t.common.managers;


import org.bukkit.entity.Player;
import org.cn2b2t.common.enums.LogType;

public class LoggerManager {


    public static void writeIn(final String name, final LogType t, final String value) {

        // 写入格式实例 2009-01-21 18:49:12 CUMR CHAT 哈哈哈哈哈哈


    }

    public static void log(final LogType lt, final Player p, final String s) {
        final String name = p.getName();
        writeIn(name, lt, s);
    }

    public static void logConsole(final LogType lt, final String s) {
        final String name = "CONSOLE";
        writeIn(name, lt, s);
    }
}
