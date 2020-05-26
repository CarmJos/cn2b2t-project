package org.cn2b2t.proxy.functions.serverinfo;

import org.cn2b2t.proxy.Main;
import org.cn2b2t.proxy.functions.PlayerCounter;
import org.cn2b2t.proxy.utils.MathMethods;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerInfos {
	private String motd1;
	private String motd2;
	private ServerPing.PlayerInfo[] playerinfo;
	private String version;
	private boolean useFakePlayer;
	private List<String> playersFrom;
	private int maxPlayers;
	private Favicon favicon;

	private String modType;


	private String targetServer;
	private boolean directTeleport;
	private boolean onlyOnlineMode;

	public ServerInfos(String motd1, String motd2,
					   List<String> playerinfo,
					   String version,
					   boolean useFakePlayer,
					   int maxPlayers,
					   String faviconpath,
					   String targetServer,
					   boolean directTeleport,
					   boolean onlyOnlineMode) {

		this.motd1 = motd1.replace("&", "§");
		this.motd2 = motd2.replace("&", "§");
		this.playerinfo = playerinfo.stream().map(s -> new ServerPing.PlayerInfo(s.replace("&", "§"), UUID.randomUUID())).toArray(ServerPing.PlayerInfo[]::new);
		this.version = version.replace("&", "§");
		this.useFakePlayer = useFakePlayer;
		this.maxPlayers = maxPlayers;
		this.playersFrom = new ArrayList<>();
		BufferedImage img;
		try {
			img = ImageIO.read(new File(Main.getInstance().getDataFolder().getParentFile().getAbsoluteFile().getParentFile().getAbsolutePath() + faviconpath));
			favicon = Favicon.create(img);
		} catch (IOException e) {
			System.out.print(faviconpath + "不存在。");
			favicon = null;
		}
		this.targetServer = targetServer;
		this.directTeleport = directTeleport;
		this.onlyOnlineMode = onlyOnlineMode;
		this.modType = "VANILLA";
	}

	public ServerInfos(String motd1, String motd2,
					   List<String> playerinfo,
					   String version,
					   boolean useFakePlayer,
					   List<String> playersFrom,
					   int maxPlayers,
					   String faviconpath,
					   String targetServer,
					   boolean directTeleport,
					   boolean onlyOnlineMode,
					   String modType) {
		this.motd1 = motd1.replace("&", "§");
		this.motd2 = motd2.replace("&", "§");
		this.playerinfo = playerinfo.stream().map(s -> new ServerPing.PlayerInfo(s.replace("&", "§"), UUID.randomUUID())).toArray(ServerPing.PlayerInfo[]::new);
		this.version = version.replace("&", "§");
		this.useFakePlayer = useFakePlayer;
		this.playersFrom = playersFrom;
		this.maxPlayers = maxPlayers;
		BufferedImage img;
		try {
			img = ImageIO.read(new File(Main.getInstance().getDataFolder().getParentFile().getAbsoluteFile().getParentFile().getAbsolutePath() + faviconpath));
			favicon = Favicon.create(img);
		} catch (IOException e) {
			System.out.print(faviconpath + "不存在。");
			favicon = null;
		}
		this.targetServer = targetServer;
		this.directTeleport = directTeleport;
		this.onlyOnlineMode = onlyOnlineMode;
		this.modType = modType;
	}

	public List<String> getPlayersFrom() {
		return playersFrom;
	}

	public String getModType() {

		return modType;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public String getMotd() {
		return motd1 + "\n" + motd2;
	}

	public boolean isUseFakePlayer() {
		return useFakePlayer;
	}

	public ServerPing.PlayerInfo[] getPlayerinfo() {
		return playerinfo;
	}

	public String getVersion() {
		return version;
	}

	public Favicon getFavicon() {
		return favicon;
	}

	public String getTargetServer() {
		return targetServer;
	}

	public boolean isDirectTeleport() {
		return directTeleport;
	}

	public boolean isOnlyOnlineMode() {
		return onlyOnlineMode;
	}

	public int getPlayerNumber() {
		int playerNumber = playersFrom.size() > 0 ?
				getPlayersFrom().stream().mapToInt(servername -> BungeeCord.getInstance().getServerInfo(servername).getPlayers().size()).sum()
				: BungeeCord.getInstance().getPlayers().size();
		return isUseFakePlayer() ? MathMethods.getFakePlayer(playerNumber) : playerNumber;
	}
}
