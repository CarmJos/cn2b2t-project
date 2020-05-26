package org.cn2b2t.core.managers.utils.scoreboard;

import org.cn2b2t.core.utils.ColorParser;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CeramicScoreboard {

    final String[] texts = new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
    SBPriority pri;
    String title;
    private boolean rending = false;

    public CeramicScoreboard() {
        this("&f&lCeramic", SBPriority.NORMAL);
    }

    public CeramicScoreboard(String title, SBPriority pri) {
        this.title = title;
        this.pri = pri;
    }

    public CeramicScoreboard(String title) {
        this(title, SBPriority.NORMAL);
    }

    public CeramicScoreboard(SBPriority pri) {
        this("&f&lKa&7&lr", pri);
    }

    public String getLine(int index) {
        if (index < 0 || index > 14) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        return this.texts[index];
    }

    public boolean isCleared(int index) {
        return this.texts[index] == null || this.texts[index].equals("") || this.texts[index].equals("{clear}");
    }

    public List<String> getTexts() {
        List<String> l = new ArrayList<>();
        for (int i = 14; i > -1; i--) {
            if (!this.texts[i].equals("")) {
                for (int id = 0; id < i + 1; id++) {
                    if (this.texts[i].equals("")) {
                        l.add(Null());
                    } else {
                        l.add(this.texts[id]);
                    }
                }
                break;
            }
        }
        return l;
    }

    public SBPriority getPriority() {
        return this.pri;
    }

    public void setPriority(SBPriority pri) {
        PriorityChangeEvent e = new PriorityChangeEvent(this, this.pri, pri);
        Bukkit.getPluginManager().callEvent(e);
        if (e.isCancelled()) {
            return;
        }
        this.pri = e.getNew();
    }

    public boolean isRending() {
        return this.rending;
    }

    protected void setRending(boolean b) {
        this.rending = b;
    }

    public void setTitle(String text) {
        TitleUpdateEvent e = new TitleUpdateEvent(this, this.title, text);
        Bukkit.getPluginManager().callEvent(e);
        if (e.isCancelled()) {
            return;
        }
        this.title = text;
    }

    public String getTitle() {
        return this.title;
    }

    public void setLine(int index, String text) {
        text = ColorParser.parse(text);

        if (index < 0 || index > 14) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        if (texts[index].equals(text)) {
            return;
        }
        LineUpdateEvent e = new LineUpdateEvent(this, index, texts[index], text);
        Bukkit.getPluginManager().callEvent(e);
        if (e.isCancelled()) {
            return;
        }
        texts[index] = e.getNew();
    }

    public void clearLine(int index) {
        setLine(index, "{clear}");
    }

    public String Null() {
        int c = 0;
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(texts).subList(0, 15));
        while (true) {
            if (c < 11) {
                if (list.contains("&" + c) || list.contains("§" + c)) {
                    c++;
                    continue;
                } else {
                    return "§" + c;
                }
            } else if (list.contains("§a") || list.contains("&a")) {
                if (list.contains("§b") || list.contains("&b")) {
                    if (list.contains("§c") || list.contains("&c")) {
                        if (list.contains("§d") || list.contains("&d")) {
                            if (list.contains("§e") || list.contains("&e")) {
                                if (list.contains("§r") || list.contains("&r")) {
                                    return null;
                                } else {
                                    return "§r";
                                }
                            } else {
                                return "§e";
                            }
                        } else {
                            return "§d";
                        }
                    } else {
                        return "§c";
                    }
                } else {
                    return "§b";
                }
            } else {
                return "§a";
            }
        }
    }

}
