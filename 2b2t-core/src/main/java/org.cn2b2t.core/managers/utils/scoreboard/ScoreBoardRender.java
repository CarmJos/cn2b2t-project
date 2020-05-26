package org.cn2b2t.core.managers.utils.scoreboard;

import org.cn2b2t.core.Main;
import org.cn2b2t.core.modules.users.User;
import org.cn2b2t.core.managers.utils.UserManager;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 该类为内部使用，可能会在将来版本中去除
 *
 * @author LSeng
 */
public final class ScoreBoardRender {

	boolean display = true;

	private CeramicScoreboard rended;
	private static final List<ChatColor> colors = Arrays.asList(ChatColor.values()); //所有颜色
	private final Player player;
	private final Scoreboard sb;
	private final Objective objective;
	private final List<BoardLine> boardLines = new ArrayList<>();// "行"
	private int maxLine;//用于标注最大行数
	private Listener listener;

	public ScoreBoardRender(Player p, String title, Scoreboard sb) {

		this.player = p;
		if (sb == null) {
//            if(Bukkit.getScoreboardManager().getMainScoreboard() == null){
			sb = Bukkit.getScoreboardManager().getNewScoreboard();
			this.sb = sb;
//            }else{
//                this.sb = Bukkit.getScoreboardManager().getMainScoreboard();
//            }
		} else {
			this.sb = sb;
		}
//        if(getBoard().getObjective(DisplaySlot.SIDEBAR)!=null){
//            objective = getBoard().getObjective(DisplaySlot.SIDEBAR);
//        }else{
		objective = getBoard().registerNewObjective(p.getUniqueId().toString().substring(0, 16), "dummy");
//        }
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		try {
//            getBoard().getTeams().clear();
		} catch (Exception ex) {
		}

		setTitle(title.replace("&", "§"));
		for (int i = 0; i < colors.size(); i++) { //循环所有的颜色
			final ChatColor color = colors.get(i);
			final Team team = getBoard().registerNewTeam("boardLine" + i); //为每个颜色注册一个队伍
			team.addEntry(color.toString()); //为队伍设置一个"行"
			boardLines.add(new BoardLine(color, team)); //将"行"添加至列表
		}
		p.setScoreboard(this.sb);

		this.listener = new Listener() {

			@EventHandler
			public void onQuit(PlayerQuitEvent event) {
				if (event.getPlayer() == player) {
					HandlerList.unregisterAll(this);
				}
			}

			@EventHandler
			public void onTitleUpdata(TitleUpdateEvent event) {
				if (!display) {
					return;
				}
				if (ScoreBoardRender.this.rended == null) {
					return;
				}
				if (event.getKarScoreboard() == ScoreBoardRender.this.rended) {
					setTitle(event.getNew());
				}
			}

			@EventHandler
			public void onLineUpdata(LineUpdateEvent event) {
				if (!display) {
					return;
				}
				if (ScoreBoardRender.this.rended == null) {
					return;
				}
				if (event.getKarScoreboard() == ScoreBoardRender.this.rended) {
					if (event.getNew().equals("{clear}")) {
						removeLine(event.getLine());
					} else {
						setLine(event.getLine(), event.getNew());
					}
				}
			}

			@EventHandler
			public void onPriorityUpdata(PriorityChangeEvent event) {
				if (ScoreBoardRender.this.rended == null) {
					return;
				}
				User u = UserManager.getUser(p);
				if (event.getKarScoreboard() == ScoreBoardRender.this.rended && event.getNew() == SBPriority.CANCEL && u.getScoreboards().size() == 1) {
					p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
					ScoreBoardRender.this.rended = null;
					u.getScoreboards().remove(event.getKarScoreboard());
					return;
				}
				if (event.getKarScoreboard() == ScoreBoardRender.this.rended) {
					CeramicScoreboard max = event.getKarScoreboard();
					for (CeramicScoreboard sb : u.getScoreboards()) {
						if (sb.getPriority().size() > max.getPriority().size()) {
							max = sb;
						}
					}
					setRended(event.getKarScoreboard());
					if (display) {
						update();
					}
				} else if (event.getNew().size() > ScoreBoardRender.this.rended.getPriority().size() && event.getNew() != SBPriority.CANCEL) {
					setRended(event.getKarScoreboard());
					if (display) {
						update();
					}
				}
			}

			@EventHandler
			public void onScoreboardEnable(ScoreboardEnableEvent event) {
				if (!event.user.getPlayer().equals(p)) {
					return;
				}
				ScoreBoardRender.this.display = event.b;
				if (ScoreBoardRender.this.display) {
					ScoreBoardRender.this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
					update();
				} else {
					p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
				}
			}

			@EventHandler
			public void onScoreboardAdd(ScoreboardAddEvent event) {
				if (!event.user.getPlayer().equals(p)) {
					return;
				}
				if (ScoreBoardRender.this.rended == null) {
					setRended(event.getKarScoreboard());
					return;
				}
				if (event.getKarScoreboard().getPriority().size() >= ScoreBoardRender.this.rended.getPriority().size()) {
					setRended(event.getKarScoreboard());
				}
			}
		};
		Bukkit.getPluginManager().registerEvents(this.listener, Main.getInstance());

	}

//    public ScoreBoardRender(Player p,Scoreboard sb){
//        this(p,"&f&lKa&7&lr",sb);
//    }
	public ScoreBoardRender(Player p, String title) {
		this(p, title, Bukkit.getScoreboardManager().getMainScoreboard() == p.getScoreboard() ? null : p.getScoreboard());
	}

	public ScoreBoardRender(Player p) {
		this(p, "&f&lKa&7&lr");
	}

	private ScoreBoardRender() {
		this(null);
	}

	private Scoreboard getBoard() {
		return sb;
	}

	public Objective getObjective() {
		return objective;
	}

	public String getTitle() {
		return objective.getDisplayName();
	}

	public void setTitle(String title) {
		objective.setDisplayName(title.replace("&", "§"));
	}

	public Player getPlayer() {
		return this.player;
	}

	/**
	 * 不管优先度，直接设置为该计分板(内部使用)
	 *
	 * @param sb
	 */
	public void setRended(CeramicScoreboard sb) {
		if (this.rended != null) {
			this.rended.setRending(false);
		}
		if (sb == null) {
			throw new NullPointerException();
		}
		RendedChangeEvent e = new RendedChangeEvent(UserManager.getUser(player), this.rended, sb);
		Bukkit.getPluginManager().callEvent(e);
		sb.setRending(true);
		this.rended = sb;
		update();
	}

	public CeramicScoreboard getRended() {
		return this.rended;
	}

	public void update() {
		if (this.rended == null) {
			return;
		}
		setTitle(this.rended.getTitle());
		setBody(this.rended.getTexts());
	}

	public String getLine(int line) {
		line = 16 - line;
		final BoardLine boardLine = getBoardLine(line); //得到我们的"行"
		Validate.notNull(boardLine, "Unable to find BoardLine with index of " + line + "."); //确认是否存在
		return boardLine.getTeam().getPrefix() + boardLine.getTeam().getSuffix();
	}

	public void setLine(int line, String value) {
		line = 16 - line;
		value = value.replace("&&", "§§").replace("&", "§").replace("§§", "&");
		final BoardLine boardLine = getBoardLine(line); //得到我们的"行"
		Validate.notNull(boardLine, "Unable to find BoardLine with index of " + line + "."); //确认是否存在
		objective.getScore(boardLine.getColor().toString()).setScore(line); //设置"行"
//		clear(line);
		//分割字符串为前16个和后16个及中间16个
		String prefix;
		String info = "";
		String suffix = "";
//		if (value.length() > 16) {
//			suffix = value.substring(16);
//			//处理前后的颜色
//			String sufpre = ChatColor.getLastColors(prefix);
//			if (value.charAt(15) == '§') {
//				sufpre = "§";
//			} else if (!suffix.isEmpty() && suffix.charAt(0) == '§') {
//				sufpre = "";
//			}
//			String ssp = sufpre + suffix;
//			suffix = ssp.substring(0, ssp.length());
//		}
//		if (value.length() > 16) {
//			prefix = value.substring(0, 16);
//			suffix = ChatColor.getLastColors(prefix) + value.substring(16, Math.min(32, value.length()));
//		} else {
//			prefix = value;
//		}

//		if(value.length() > 16){
//			prefix = value.substring(0, 16);
//			String prefixLastColor = ChatColor.getLastColors(prefix);
//			if(prefix.charAt(15) == '§'){
//				prefix = prefix.substring(0, 15);
//			}
//			value = value.substring(16);
//			if(!prefixLastColor.equalsIgnoreCase("§f")){
//				value = prefixLastColor + value;
//			}
//			if(value.length() > 16){
//				info = value.substring(0, 16);
//				String infoLastColor = ChatColor.getLastColors(info);
//				if(info.charAt(15) == '§'){
//					info = info.substring(0, 15);
//				}
//				value = value.substring(16);
//				if(!infoLastColor.equalsIgnoreCase("§f")){
//					value = infoLastColor + value;
//				}
//				suffix = value.substring(0,Math.min(16,value.length()));
//			} else {
//				info = (prefixLastColor.equalsIgnoreCase("§f") ? "" : prefixLastColor) + value;
//			}
//		} else {
//			prefix = value;
//		}

		if (value.length() > 16) {
			prefix = value.substring(0,16);
			String prefixLastColor = ChatColor.getLastColors(prefix);
			if(prefixLastColor.isEmpty()) prefixLastColor = "§f";
			if(prefix.charAt(15) == '§'){
				prefix = prefix.substring(0, 15);
				value = "§"+value.substring(16);
			} else {
				value = prefixLastColor + value.substring(16);
			}
			suffix = value.substring(0,Math.min(16,value.length()));

//			if(prefix.charAt(15) == '§'){
//				prefix = prefix.substring(0,15);
//				suffix = ChatColor.getLastColors(prefix) + "§" + value.substring(16);
//			}else{
//				suffix = ChatColor.getLastColors(prefix) + value.substring(16);
//			}
//			suffix = suffix.substring(0,Math.min(16,value.length()));
		}else{
			prefix = value;
		}

		boardLine.getTeam().setPrefix(prefix); //设置前16个字符
//		boardLine.getTeam().setDisplayName(info);
		boardLine.getTeam().setSuffix(suffix); //设置后16个字符
//		for(String entry : boardLine.getTeam().getEntries()){
//			boardLine.getTeam().removeEntry(entry);
//		}
//		boardLine.getTeam().removeEntry(boardLine.getTeam().getEntries().iterator().next());
//		boardLine.getTeam().addEntry(info);
//		objective.getScore(info).setScore(line);
		maxLine = line + 1;
	}

	//all 5  [0 1 2 3 4] maxLine = 5  all 3 [0 1 2] maxLine=4
	public void clear(int size) {
		if (maxLine > size) {
			for (int i = size; i < maxLine; i++) {
				removeLine(i);
			}
			maxLine = size;
		}
	}

	public void removeLine(int line) {
		line = 16 - line;
		final BoardLine boardLine = getBoardLine(line);
		Validate.notNull(boardLine, "Unable to find BoardLine with index of " + line + "."); //确认是否存在
		getBoard().resetScores(boardLine.getColor().toString()); //删除这个"行"
	}

	private BoardLine getBoardLine(int line) {
		return boardLines.get(line);
	}

	public void setBody(List<String> newContents) {
		for (int i = 0; i < newContents.size(); i++) {
			setLine(newContents.size() - i, newContents.get(i));
		}
		clear(newContents.size());
	}

	class BoardLine {

		private final ChatColor color;
		private final Team team;

		public BoardLine(ChatColor color, Team team) {
			this.color = color;
			this.team = team;
		}

		public ChatColor getColor() {
			return color;
		}

		public Team getTeam() {
			return team;
		}

	}
}
