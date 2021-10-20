package org.cn2b2t.core.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.md_5.bungee.api.ChatColor.*;

public class BBCodeChatParser {

    private static final Pattern pattern;
    private static final Pattern strip_bbcode_pattern;

    static {
        pattern = Pattern.compile("(?is)(?=\\n)|(?:[&§](?<color>[0-9A-FK-OR]))|(?:\\[(?<tag>/?(?:b|i|u|s|nocolor|nobbcode)|(?:url|command|hover|suggest|color)=(?<value>(?:(?:[^]\\[]*)\\[(?:[^]\\[]*)\\])*(?:[^]\\[]*))|/(?:url|command|hover|suggest|color))\\])|(?:\\[(?<implicitTag>url|command|suggest)\\](?=(?<implicitValue>.*?)\\[/\\k<implicitTag>\\]))");
        strip_bbcode_pattern = Pattern.compile("(?is)(?:\\[(?<tag>/?(?:b|i|u|s|nocolor|nobbcode)|(?:url|command|hover|suggest|color)=(?<value>(?:(?:[^]\\[]*)\\[(?:[^]\\[]*)\\])*(?:[^]\\[]*))|/(?:url|command|hover|suggest|color))\\])|(?:\\[(?<implicitTag>url|command|suggest)\\](?=(?<implicitValue>.*?)\\[/\\k<implicitTag>\\]))");
    }

    public static BaseComponent[] parse(final String text) {
        final Matcher matcher = BBCodeChatParser.pattern.matcher(text);
        TextComponent current = new TextComponent();
        final List<BaseComponent> components = new LinkedList<>();
        int forceBold = 0;
        int forceItalic = 0;
        int forceUnderlined = 0;
        int forceStrikethrough = 0;
        int nocolorLevel = 0;
        int nobbcodeLevel = 0;
        final Deque<ChatColor> colorDeque = new LinkedList<>();
        final Deque<ClickEvent> clickEventDeque = new LinkedList<>();
        final Deque<HoverEvent> hoverEventDeque = new LinkedList<>();
        while (matcher.find()) {
            boolean parsed = false;
            final StringBuffer stringBuffer = new StringBuffer();
            matcher.appendReplacement(stringBuffer, "");
            final TextComponent component = new TextComponent(current);
            current.setText(stringBuffer.toString());
            components.add(current);
            current = component;
            final String group_color = matcher.group("color");
            final String group_tag = matcher.group("tag");
            String group_value = matcher.group("value");
            final String group_implicitTag = matcher.group("implicitTag");
            final String group_implicitValue = matcher.group("implicitValue");
            if (group_color != null && nocolorLevel <= 0) {
                ChatColor color = ChatColor.getByChar(group_color.charAt(0));
                if (color != null) {
                    if (MAGIC.equals(color)) {
                        current.setObfuscated(true);
                    } else if (BOLD.equals(color)) {
                        current.setBold(true);
                    } else if (STRIKETHROUGH.equals(color)) {
                        current.setStrikethrough(true);
                    } else if (UNDERLINE.equals(color)) {
                        current.setUnderlined(true);
                    } else if (ITALIC.equals(color)) {
                        current.setItalic(true);
                    } else if (RESET.equals(color)) {
                        color = WHITE;
                    }
                    current = new TextComponent();
                    current.setColor(color);
                    current.setBold(forceBold > 0);
                    current.setItalic(forceItalic > 0);
                    current.setUnderlined(forceUnderlined > 0);
                    current.setStrikethrough(forceStrikethrough > 0);
                    if (!colorDeque.isEmpty()) {
                        current.setColor(colorDeque.peek());
                    }
                    if (!clickEventDeque.isEmpty()) {
                        current.setClickEvent(clickEventDeque.peek());
                    }
                    if (!hoverEventDeque.isEmpty()) {
                        current.setHoverEvent(hoverEventDeque.peek());
                    }
                }
                parsed = true;
            }
            if (group_tag != null && nobbcodeLevel <= 0) {
                if (group_tag.matches("(?is)^b$")) {
                    current.setBold(++forceBold > 0);
                    parsed = true;
                } else if (group_tag.matches("(?is)^/b$")) {
                    current.setBold(--forceBold > 0);
                    parsed = true;
                }
                if (group_tag.matches("(?is)^i$")) {
                    current.setItalic(++forceItalic > 0);
                    parsed = true;
                } else if (group_tag.matches("(?is)^/i$")) {
                    current.setItalic(--forceItalic > 0);
                    parsed = true;
                }
                if (group_tag.matches("(?is)^u$")) {
                    current.setUnderlined(++forceUnderlined > 0);
                    parsed = true;
                } else if (group_tag.matches("(?is)^/u$")) {
                    current.setUnderlined(--forceUnderlined > 0);
                    parsed = true;
                }
                if (group_tag.matches("(?is)^s$")) {
                    current.setStrikethrough(++forceStrikethrough > 0);
                    parsed = true;
                } else if (group_tag.matches("(?is)^/s$")) {
                    current.setStrikethrough(--forceStrikethrough > 0);
                    parsed = true;
                }
                if (group_tag.matches("(?is)^color=.*$")) {
                    ChatColor color = null;
                    for (final ChatColor color2 : ChatColor.values()) {
                        if (color2.getName().equalsIgnoreCase(group_value)) {
                            color = color2;
                        }
                    }
                    colorDeque.push(current.getColor());
                    if (color != null && color != ChatColor.BOLD && color != ChatColor.ITALIC && color != MAGIC && color != ChatColor.RESET && color != ChatColor.STRIKETHROUGH && color != ChatColor.UNDERLINE) {
                        colorDeque.push(color);
                        current.setColor(color);
                    } else {
                        Bukkit.getLogger().log(Level.WARNING, "Invalid color tag: [{0}] UNKNOWN COLOR ''{1}''", new Object[]{group_tag, group_value});
                        colorDeque.push(ChatColor.WHITE);
                        current.setColor(ChatColor.WHITE);
                    }
                    parsed = true;
                } else if (group_tag.matches("(?is)^/color$")) {
                    if (!colorDeque.isEmpty()) {
                        colorDeque.pop();
                        current.setColor(colorDeque.pop());
                    }
                    parsed = true;
                }
                if (group_tag.matches("(?is)^url=.*$")) {
                    String url = group_value;
                    url = url.replaceAll("(?is)\\[/?nobbcode\\]", "");
                    if (!url.startsWith("http")) {
                        url = "http://" + url;
                    }
                    final ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, url);
                    clickEventDeque.push(clickEvent);
                    current.setClickEvent(clickEvent);
                    parsed = true;
                }
                if (group_tag.matches("(?is)^/(?:url|command|suggest)$")) {
                    if (!clickEventDeque.isEmpty()) {
                        clickEventDeque.pop();
                    }
                    if (clickEventDeque.isEmpty()) {
                        current.setClickEvent(null);
                    } else {
                        current.setClickEvent(clickEventDeque.peek());
                    }
                    parsed = true;
                }
                if (group_tag.matches("(?is)^command=.*")) {
                    group_value = group_value.replaceAll("(?is)\\[/?nobbcode\\]", "");
                    final ClickEvent clickEvent2 = new ClickEvent(ClickEvent.Action.RUN_COMMAND, group_value);
                    clickEventDeque.push(clickEvent2);
                    current.setClickEvent(clickEvent2);
                    parsed = true;
                }
                if (group_tag.matches("(?is)^suggest=.*")) {
                    group_value = group_value.replaceAll("(?is)\\[/?nobbcode\\]", "");
                    final ClickEvent clickEvent2 = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, group_value);
                    clickEventDeque.push(clickEvent2);
                    current.setClickEvent(clickEvent2);
                    parsed = true;
                }
                if (group_tag.matches("(?is)^hover=.*$")) {
                    BaseComponent[] components2 = parse(group_value);
                    if (!hoverEventDeque.isEmpty()) {
                        final BaseComponent[] components3 = hoverEventDeque.getLast().getValue();
                        final BaseComponent[] components4 = new BaseComponent[components2.length + components3.length + 1];
                        int i = 0;
                        for (final BaseComponent baseComponent : components3) {
                            components4[i++] = baseComponent;
                        }
                        components4[i++] = new TextComponent("\n");
                        for (final BaseComponent baseComponent : components2) {
                            components4[i++] = baseComponent;
                        }
                        components2 = components4;
                    }
                    final HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, components2);
                    hoverEventDeque.push(hoverEvent);
                    current.setHoverEvent(hoverEvent);
                    parsed = true;
                } else if (group_tag.matches("(?is)^/hover$")) {
                    if (!hoverEventDeque.isEmpty()) {
                        hoverEventDeque.pop();
                    }
                    if (hoverEventDeque.isEmpty()) {
                        current.setHoverEvent(null);
                    } else {
                        current.setHoverEvent(hoverEventDeque.peek());
                    }
                    parsed = true;
                }
            }
            if (group_implicitTag != null && nobbcodeLevel <= 0) {
                if (group_implicitTag.matches("(?is)^url$")) {
                    String url = group_implicitValue;
                    if (!url.startsWith("http")) {
                        url = "http://" + url;
                    }
                    final ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, url);
                    clickEventDeque.push(clickEvent);
                    current.setClickEvent(clickEvent);
                    parsed = true;
                }
                if (group_implicitTag.matches("(?is)^command$")) {
                    final ClickEvent clickEvent2 = new ClickEvent(ClickEvent.Action.RUN_COMMAND, group_implicitValue);
                    clickEventDeque.push(clickEvent2);
                    current.setClickEvent(clickEvent2);
                    parsed = true;
                }
                if (group_implicitTag.matches("(?is)^suggest$")) {
                    final ClickEvent clickEvent2 = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, group_implicitValue);
                    clickEventDeque.push(clickEvent2);
                    current.setClickEvent(clickEvent2);
                    parsed = true;
                }
            }
            if (group_tag != null) {
                if (group_tag.matches("(?is)^nocolor$")) {
                    ++nocolorLevel;
                    parsed = true;
                }
                if (group_tag.matches("(?is)^/nocolor$")) {
                    --nocolorLevel;
                    parsed = true;
                }
                if (group_tag.matches("(?is)^nobbcode$")) {
                    ++nobbcodeLevel;
                    parsed = true;
                }
                if (group_tag.matches("(?is)^/nobbcode$")) {
                    --nobbcodeLevel;
                    parsed = true;
                }
            }
            if (!parsed) {
                final TextComponent component2 = new TextComponent(current);
                current.setText(matcher.group(0));
                components.add(current);
                current = component2;
            }
        }
        final StringBuffer stringBuffer2 = new StringBuffer();
        matcher.appendTail(stringBuffer2);
        current.setText(stringBuffer2.toString());
        components.add(current);
        return components.toArray(new BaseComponent[0]);
    }

    public static String stripBBCode(final String string) {
        return strip_bbcode_pattern.matcher(string).replaceAll("");
    }

}
