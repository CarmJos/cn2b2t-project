package org.cn2b2t.proxy.utils.data;

import java.util.HashMap;
import java.util.Map;

public enum DBMS {
    Other("[Other] "),
    Firebird("[Firebird] "),
    FrontBase("[FrontBase] "),
    DB2("[DB2] "),
    H2("[H2] "),
    Informix("[Informix] "),
    Ingres("[Ingres] "),
    MaxDB("[MaxDB] "),
    MicrosoftSQL("[MicrosoftSQL] "),
    Mongo("[Mongo] "),
    mSQL("[mSQL] "),
    MySQL("[MySQL] "),
    Oracle("[Oracle] "),
    PostgreSQL("[PostgreSQL] "),
    SQLite("[SQLite] ");

    private String prefix;
    private static Map<String, DBMS> prefixes;

    DBMS(final String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return this.prefix;
    }

    public static DBMS getDBMS(final String prefix) {
        return DBMS.prefixes.get(prefix);
    }

    static {
        DBMS.prefixes = new HashMap<String, DBMS>();
        for (final DBMS dbms : DBMS.prefixes.values()) {
            DBMS.prefixes.put(dbms.toString(), dbms);
        }
    }
}
