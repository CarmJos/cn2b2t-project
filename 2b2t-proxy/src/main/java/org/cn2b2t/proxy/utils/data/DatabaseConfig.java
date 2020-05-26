package org.cn2b2t.proxy.utils.data;

public class DatabaseConfig {
    public final String SQL_HOST;
    public final int SQL_PORT;
    public final String SQL_DATA;
    public final String SQL_USER;
    public final String SQL_PASS;

    public DatabaseConfig(String SQL_HOST, int SQL_PORT, String SQL_DATA, String SQL_USER, String SQL_PASS) {
        this.SQL_HOST = SQL_HOST;
        this.SQL_PORT = SQL_PORT;
        this.SQL_DATA = SQL_DATA;
        this.SQL_USER = SQL_USER;
        this.SQL_PASS = SQL_PASS;
    }
}
