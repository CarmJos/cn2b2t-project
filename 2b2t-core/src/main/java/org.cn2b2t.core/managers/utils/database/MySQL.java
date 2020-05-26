package org.cn2b2t.core.managers.utils.database;


import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class MySQL extends HostnameDatabase {

	public MySQL(final Logger log, final String prefix, final String hostname, final String port, final String database, final String username, final String password) {
		super(log, prefix, DBMS.MySQL, hostname, Integer.parseInt(port), database, username, password);
	}

	public MySQL(final Logger log, final String prefix, final String hostname, final int port, final String database, final String username, final String password) {
		super(log, prefix, DBMS.MySQL, hostname, port, database, username, password);
	}

	public MySQL(final Logger log, final String prefix, final String database, final String username, final String password) {
		super(log, prefix, DBMS.MySQL, "localhost", 3306, database, username, password);
	}

	public MySQL(final Logger log, final String prefix, final String database, final String username) {
		super(log, prefix, DBMS.MySQL, "localhost", 3306, database, username, "");
	}

	public MySQL(final Logger log, final String prefix, final String database) {
		super(log, prefix, DBMS.MySQL, "localhost", 3306, database, "", "");
	}

	@Override
	protected boolean initialize() {
		try {
			Class.forName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
			return true;
		} catch (ClassNotFoundException e) {
			this.warning("MySQL DataSource class missing: " + e.getMessage() + ".");
			return false;
		}
	}

	@Override
	public boolean open() {
		try {
			final String url = "jdbc:mysql://" + this.getHostname() + ":" + this.getPort() + "/" + this.getDatabase() + "?autoReconnect=true";
			if (this.initialize()) {
				this.connection = DriverManager.getConnection(url, this.getUsername(), this.getPassword());
				return true;
			}
			return false;
		} catch (SQLException e) {
			this.error("Could not establish a MySQL connection, SQLException: " + e.getMessage());
			return false;
		}
	}

	@Override
	protected void queryValidation(final StatementEnum statement) throws SQLException {
		switch ((Statements) statement) {
			case USE: {
				this.warning("Please create a new connection to use a different database.");
				throw new SQLException("Please create a new connection to use a different database.");
			}
			case PREPARE:
			case EXECUTE:
			case DEALLOCATE: {
				this.warning("Please use the prepare() method to prepare a SoulHistory.");
				throw new SQLException("Please use the prepare() method to prepare a SoulHistory.");
			}
			default: {
			}
		}
	}

	@Override
	public Statements getStatement(final String query) throws SQLException {
		final String[] statement = query.trim().split(" ", 2);
		try {
			final Statements converted = Statements.valueOf(statement[0].toUpperCase());
			return converted;
		} catch (IllegalArgumentException e) {
			throw new SQLException("Unknown statement: \"" + statement[0] + "\".");
		}
	}

	@Deprecated
	public boolean createTable(final String query) {
		Statement statement = null;
		if (query == null || query.equals("")) {
			this.writeError("Could not create table: SoulHistory is empty or null.", true);
			return false;
		}
		try {
			statement = this.connection.createStatement();
			statement.execute(query);
			statement.close();
		} catch (SQLException e) {
			this.writeError("Could not create table, SQLException: " + e.getMessage(), true);
			return false;
		}
		return true;
	}

	@Override
	public boolean isTable(final String table) {
		Statement statement;
		try {
			statement = this.connection.createStatement();
		} catch (SQLException e) {
			this.error("Could not create a statement in checkTable(), SQLException: " + e.getMessage());
			return false;
		}
		try {
			statement.executeQuery("SELECT * FROM " + table);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public boolean truncate(final String table) {
		Statement statement = null;
		String query = null;
		try {
			if (!this.isTable(table)) {
				this.error("Table \"" + table + "\" does not exist.");
				return false;
			}
			statement = this.connection.createStatement();
			query = "DELETE FROM " + table + ";";
			statement.executeUpdate(query);
			statement.close();
			return true;
		} catch (SQLException e) {
			this.error("Could not wipe table, SQLException: " + e.getMessage());
			return false;
		}
	}

	public enum Statements implements StatementEnum {
		SELECT("SELECT"),
		INSERT("INSERT"),
		UPDATE("UPDATE"),
		DELETE("DELETE"),
		DO("DO"),
		REPLACE("REPLACE"),
		LOAD("LOAD"),
		HANDLER("HANDLER"),
		CALL("CALL"),
		CREATE("CREATE"),
		ALTER("ALTER"),
		DROP("DROP"),
		TRUNCATE("TRUNCATE"),
		RENAME("RENAME"),
		START("START"),
		COMMIT("COMMIT"),
		SAVEPOINT("SAVEPOINT"),
		ROLLBACK("ROLLBACK"),
		RELEASE("RELEASE"),
		LOCK("LOCK"),
		UNLOCK("UNLOCK"),
		PREPARE("PREPARE"),
		EXECUTE("EXECUTE"),
		DEALLOCATE("DEALLOCATE"),
		SET("SET"),
		SHOW("SHOW"),
		DESCRIBE("DESCRIBE"),
		EXPLAIN("EXPLAIN"),
		HELP("HELP"),
		USE("USE");

		private final String string;

		Statements(final String string) {
			this.string = string;
		}

		@Override
		public String toString() {
			return this.string;
		}
	}
}
