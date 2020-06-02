package org.cn2b2t.core.modules;

public class DataServerInfo {

    private String serverID;
    private String hostName;
    private int port;
    private String serverGroup;

    private int players;

    public DataServerInfo(String serverID, String hostName, int port, String serverGroup, int players) {
        this.serverGroup = serverGroup;
        this.serverID = serverID;
        this.hostName = hostName;
        this.port = port;

        this.players = players;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public String getServerGroup() {
        return serverGroup;
    }

    public void setServerGroup(String serverGroup) {
        this.serverGroup = serverGroup;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

}
