package org.cn2b2t.core.managers.utils.database;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class Database {
	protected final String prefix;
	protected Logger log;
	protected DBMS driver;
	protected Connection connection;
	protected Map<PreparedStatement, StatementEnum> preparedStatements = new HashMap<PreparedStatement, StatementEnum>();
	protected int lastUpdate;

	public Database(final Logger log, final String prefix, final DBMS dbms) throws DatabaseException {
//        this.preparedStatements = new HashMap<PreparedStatement, StatementEnum>();
		if (log == null) {
			throw new DatabaseException("Logger cannot be null.");
		}
		if (prefix == null || prefix.length() == 0) {
			throw new DatabaseException("Plugin prefix cannot be null or empty.");
		}
		this.log = log;
		this.prefix = prefix;
		this.driver = dbms;
	}

	protected final String prefix(final String message) {
		return this.prefix + this.driver + message;
	}

	@Deprecated
	public final void writeInfo(final String toWrite) {
		this.info(toWrite);
	}

	@Deprecated
	public final void writeError(final String toWrite, final boolean severe) {
		if (severe) {
			this.error(toWrite);
		} else {
			this.warning(toWrite);
		}
	}

	public final void info(final String info) {
		if (info != null && !info.isEmpty()) {
			this.log.info(this.prefix(info));
		}
	}

	public final void warning(final String warning) {
		if (warning != null && !warning.isEmpty()) {
			this.log.warning(this.prefix(warning));
		}
	}

	public final void error(final String error) {
		if (error != null && !error.isEmpty()) {
			this.log.severe(this.prefix(error));
		}
	}

	protected abstract boolean initialize();

	public final DBMS getDriver() {
		return this.getDBMS();
	}

	public final DBMS getDBMS() {
		return this.driver;
	}

	public abstract boolean open();

	public final boolean close() {
		if (this.connection != null) {
			try {
				this.connection.close();
				return true;
			} catch (SQLException e) {
				this.writeError("Could not close connection, SQLException: " + e.getMessage(), true);
				return false;
			}
		}
		this.writeError("Could not close connection, it is null.", true);
		return false;
	}

	@Deprecated
	public final boolean isConnected() {
		return this.isOpen();
	}

	public final Connection getConnection() {
		return this.connection;
	}

	public final boolean isOpen() {
		return this.isOpen(1);
	}

	public final boolean isOpen(final int seconds) {
		if (this.connection != null) {
			try {
				if (this.connection.isValid(seconds)) {
					return true;
				}
			} catch (SQLException ex) {
			}
		}
		return false;
	}

	@Deprecated
	public final boolean checkConnection() {
		return this.isOpen();
	}

	public final int getLastUpdateCount() {
		return this.lastUpdate;
	}

	protected abstract void queryValidation(final StatementEnum p0) throws SQLException;

	public final ResultSet query(final String query) throws SQLException {
		this.queryValidation(this.getStatement(query));
		final Statement statement = this.getConnection().createStatement();
		if (statement.execute(query)) {
			return statement.getResultSet();
		}
		final int uc = statement.getUpdateCount();
		this.lastUpdate = uc;
		return this.getConnection().createStatement().executeQuery("SELECT " + uc);
	}

	protected final ResultSet query(final PreparedStatement ps, final StatementEnum statement) throws SQLException {
		this.queryValidation(statement);
		if (ps.execute()) {
			return ps.getResultSet();
		}
		final int uc = ps.getUpdateCount();
		this.lastUpdate = uc;
		return this.connection.createStatement().executeQuery("SELECT " + uc);
	}

	public final ResultSet query(final PreparedStatement ps) throws SQLException {
		ResultSet output = this.query(ps, this.preparedStatements.get(ps));
		this.preparedStatements.remove(ps);
		return output;
	}

	public final PreparedStatement prepare(final String query) throws SQLException {
		final StatementEnum s = this.getStatement(query);
		final PreparedStatement ps = this.connection.prepareStatement(query);
		this.preparedStatements.put(ps, s);
		return ps;
	}

	public ArrayList<Long> insert(final String query) throws SQLException {
		final ArrayList<Long> keys = new ArrayList<Long>();
		final PreparedStatement ps = this.connection.prepareStatement(query, 1);
		this.lastUpdate = ps.executeUpdate();
		final ResultSet key = ps.getGeneratedKeys();
		if (key.next()) {
			keys.add(key.getLong(1));
		}
		return keys;
	}

	public ArrayList<Long> insert(final PreparedStatement ps) throws SQLException {
		this.lastUpdate = ps.executeUpdate();
		this.preparedStatements.remove(ps);
		final ArrayList<Long> keys = new ArrayList<Long>();
		final ResultSet key = ps.getGeneratedKeys();
		if (key.next()) {
			keys.add(key.getLong(1));
		}
		return keys;
	}

	public final ResultSet query(final Builder builder) throws SQLException {
		return this.query(builder.toString());
	}

	public abstract StatementEnum getStatement(final String p0) throws SQLException;

	@Deprecated
	public boolean createTable() {
		return false;
	}

	@Deprecated
	public boolean checkTable(final String table) {
		return this.isTable(table);
	}

	@Deprecated
	public boolean wipeTable(final String table) {
		return this.truncate(table);
	}

	public abstract boolean isTable(final String p0);

	public abstract boolean truncate(final String p0);
}
