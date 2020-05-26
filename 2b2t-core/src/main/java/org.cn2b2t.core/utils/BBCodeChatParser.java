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

public class BBCodeChatParser {

	private static final Pattern pattern;
	private static final Pattern strip_bbcode_pattern;
//	private final Logger logger;

//	public BBCodeChatParser(final Logger logger) {
//		this.logger = logger;
//	}
//
//	public BBCodeChatParser() {
//		this(Logger.getLogger(BBCodeChatParser.class.getName()));
//	}

	static {
		pattern = Pattern.compile("(?is)(?=\\n)|(?:[&§](?<color>[0-9A-FK-OR]))|(?:\\[(?<tag>/?(?:b|i|u|s|nocolor|nobbcode)|(?:url|command|hover|suggest|color)=(?<value>(?:(?:[^]\\[]*)\\[(?:[^]\\[]*)\\])*(?:[^]\\[]*))|/(?:url|command|hover|suggest|color))\\])|(?:\\[(?<implicitTag>url|command|suggest)\\](?=(?<implicitValue>.*?)\\[/\\k<implicitTag>\\]))");
		strip_bbcode_pattern = Pattern.compile("(?is)(?:\\[(?<tag>/?(?:b|i|u|s|nocolor|nobbcode)|(?:url|command|hover|suggest|color)=(?<value>(?:(?:[^]\\[]*)\\[(?:[^]\\[]*)\\])*(?:[^]\\[]*))|/(?:url|command|hover|suggest|color))\\])|(?:\\[(?<implicitTag>url|command|suggest)\\](?=(?<implicitValue>.*?)\\[/\\k<implicitTag>\\]))");
	}

//	public static BaseComponent[] parseChatColor(BaseComponent[] bcs){
//		List<BaseComponent> list = new LinkedList();
//		
//		ChatColor lastColor = ChatColor.GRAY;
//		boolean lastBold = false;
//		boolean lastItalic = false;
//		boolean lastObfuscated = false;
//		boolean lastUnderlined = false;
//		boolean lastStrikethrough = false;
//		for(BaseComponent bc : bcs){
//			if(bc instanceof TextComponent){
//				TextComponent tc = (TextComponent)bc;
//				String raw = tc.getText();
//				System.out.print("raw: "+raw);
//				if(raw.contains("§")){
//					TextComponent tce = new TextComponent();
//					while(raw.contains("§")){
//						if(!raw.startsWith("§")){
//							tce.setText(raw.substring(0,raw.indexOf('§')));
//							raw = raw.substring(raw.indexOf('§'));
//						}
//						if(raw.length()-1>raw.indexOf('§')){
//							char c = Character.toLowerCase(raw.charAt(raw.indexOf('§')+1));
//							if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f')) {
//								if (!tce.getText().isEmpty()) {
//									tce.setColor(lastColor);
//									tce.setBold(lastBold);
//									tce.setItalic(lastItalic);
//									tce.setObfuscated(lastObfuscated);
//									tce.setUnderlined(lastUnderlined);
//									tce.setStrikethrough(lastStrikethrough);
//									if(bc.getHoverEvent()!=null){
//										tce.setHoverEvent(bc.getHoverEvent());
//									}
//									if(bc.getClickEvent()!=null){
//										tce.setClickEvent(bc.getClickEvent());
//									}
//									list.add(tce.duplicate());
//									tce = new TextComponent();
//									System.out.print("动作: 输出1");
//								}
//								lastColor = ChatColor.getByChar(c);
//								lastBold = lastItalic = lastObfuscated = lastUnderlined = lastStrikethrough = false;
//								raw = raw.substring(2, raw.length()-1);
//								System.out.print("动作: 染色");
//							}else if(c == 'm'){
//								lastStrikethrough = true;
//								raw = raw.substring(2, raw.length()-1);
//								System.out.print("动作: 划掉");
//							}else if(c == 'n'){
//								lastUnderlined = true;
//								raw = raw.substring(2, raw.length()-1);
//								System.out.print("动作: 下划线");
//							}else if(c == 'o'){
//								lastItalic = true;
//								raw = raw.substring(2, raw.length()-1);
//								System.out.print("动作: 斜体");
//							}else if(c == 'l'){
//								lastBold = true;
//								raw = raw.substring(2, raw.length()-1);
//								System.out.print("动作: 加粗");
//							}else if(c == 'k'){
//								lastObfuscated = true;
//								raw = raw.substring(2, raw.length()-1);
//								System.out.print("动作: 混乱");
//							}else if(c == 'r'){
//								lastColor = ChatColor.RESET;
//								System.out.print("动作: 重设");
//							}
//						}else{
//							raw = raw.substring(0, raw.length()-1);
//						}
//						if(!tce.getText().isEmpty()){
//							tce.setColor(lastColor);
//							tce.setBold(lastBold);
//							tce.setItalic(lastItalic);
//							tce.setObfuscated(lastObfuscated);
//							tce.setUnderlined(lastUnderlined);
//							tce.setStrikethrough(lastStrikethrough);
//							if(bc.getHoverEvent()!=null){
//								tce.setHoverEvent(bc.getHoverEvent());
//							}
//							if(bc.getClickEvent()!=null){
//								tce.setClickEvent(bc.getClickEvent());
//							}
//							list.add((BaseComponent)tce.duplicate());
//							tce = new TextComponent();
//							System.out.print("动作: 输出2");
//						}
//					}
//					if(!raw.isEmpty()){
//						tce.setText(raw);
//						tce.setColor(lastColor);
//						tce.setBold(lastBold);
//						tce.setItalic(lastItalic);
//						tce.setObfuscated(lastObfuscated);
//						tce.setUnderlined(lastUnderlined);
//						tce.setStrikethrough(lastStrikethrough);
//						if(bc.getHoverEvent()!=null){
//							tce.setHoverEvent(bc.getHoverEvent());
//						}
//						if(bc.getClickEvent()!=null){
//							tce.setClickEvent(bc.getClickEvent());
//						}
//						list.add((BaseComponent)tce.duplicate());
//						System.out.print("动作: 输出3");
//					}
//				}else{
//					bc.setColor(lastColor);
//					bc.setBold(lastBold);
//					bc.setItalic(lastItalic);
//					bc.setObfuscated(lastObfuscated);
//					bc.setUnderlined(lastUnderlined);
//					bc.setStrikethrough(lastStrikethrough);
//					list.add(bc);
//					System.out.print("动作: 输出4");
//				}
//			}else{
//				bc.setColor(lastColor);
//				bc.setBold(lastBold);
//				bc.setItalic(lastItalic);
//				bc.setObfuscated(lastObfuscated);
//				bc.setUnderlined(lastUnderlined);
//				bc.setStrikethrough(lastStrikethrough);
//				list.add(bc);
//				System.out.print("动作: 输出5");
//			}
//		}
//		
//		return list.toArray(new BaseComponent[list.size()]);
//	}

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
					Label_0458:
					{
						switch (color) {
							case MAGIC: {
								current.setObfuscated(true);
								break Label_0458;
							}
							case BOLD: {
								current.setBold(true);
								break Label_0458;
							}
							case STRIKETHROUGH: {
								current.setStrikethrough(true);
								break Label_0458;
							}
							case UNDERLINE: {
								current.setUnderlined(true);
								break Label_0458;
							}
							case ITALIC: {
								current.setItalic(true);
								break Label_0458;
							}
							case RESET: {
								color = ChatColor.WHITE;
								break;
							}
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
					if (color != null && color != ChatColor.BOLD && color != ChatColor.ITALIC && color != ChatColor.MAGIC && color != ChatColor.RESET && color != ChatColor.STRIKETHROUGH && color != ChatColor.UNDERLINE) {
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
		return components.toArray(new BaseComponent[components.size()]);
//		return parseChatColor(components.toArray(new BaseComponent[components.size()]));
	}

	public static String stripBBCode(final String string) {
		return strip_bbcode_pattern.matcher(string).replaceAll("");
	}

}
