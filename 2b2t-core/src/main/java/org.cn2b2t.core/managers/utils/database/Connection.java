package org.cn2b2t.core.managers.utils.database;

import org.cn2b2t.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class Connection {

	public boolean debug_outputSQL = true;
	private Database sql;
	private boolean databaseIsOpen;
	private DatabaseConfig databaseConfig;

	private Connection() {
	}

	public Connection(String SQL_HOST, int SQL_PORT, String SQL_DATA, String SQL_USER, String SQL_PASS) {
		this.databaseConfig = new DatabaseConfig(SQL_HOST, SQL_PORT, SQL_DATA, SQL_USER, SQL_PASS);
	}

	public boolean connect() {
		if (sql != null) {
			return false;
		}
		sql = new MySQL(Bukkit.getLogger(), "Kettle", getDatabaseConfig().SQL_HOST, getDatabaseConfig().SQL_PORT, getDatabaseConfig().SQL_DATA, getDatabaseConfig().SQL_USER, getDatabaseConfig().SQL_PASS);
		SQLcheck();
		return databaseIsOpen();
	}

	public void SQLqueryWithNoResult(String query) {
		SQLqueryWithNoResult(query, true);
	}

	public void SQLqueryWithNoResult(String query, boolean output) {
		SQLcheck();
		try {
			getSQL().query(query);
		} catch (SQLException ex) {
			Bukkit.getLogger().severe(query);
			if (output) {
				info(ex.getLocalizedMessage());
			}
		}
	}

	public ResultSet SQLqueryInTable(String Table) {
		SQLcheck();
		String sqlm = "";
		try {
			sqlm = "SELECT * FROM " + Table;
			ResultSet query = getSQL().getConnection().prepareStatement(sqlm, 1004, 1008).executeQuery();
			return query;
		} catch (SQLException ex) {
			Bukkit.getLogger().warning(ex.getLocalizedMessage());
			Bukkit.getLogger().log(Level.WARNING, "code: {0}", sqlm);
		}
		return null;
	}

	public ResultSet SQLquery(String message) {
		SQLcheck();
		try {
			ResultSet query = getSQL().getConnection().prepareStatement(message, 1004, 1008).executeQuery();
			return query;
		} catch (SQLException ex) {
			Bukkit.getLogger().warning(ex.getLocalizedMessage());
			Bukkit.getLogger().log(Level.WARNING, "code: {0}", message);
		}
		return null;
	}

	public ResultSet SQLquery(String Table, String QueryName, Object QueryValue) {
		SQLcheck();
		String sqlm = "";
		try {
			sqlm = "SELECT * FROM " + Table + " WHERE `" + QueryName + "` = '" + QueryValue + "'";
			ResultSet query = getSQL().getConnection().prepareStatement(sqlm, 1004, 1008).executeQuery();
			return query;
		} catch (SQLException ex) {
			Bukkit.getLogger().warning(ex.getLocalizedMessage());
			Bukkit.getLogger().log(Level.WARNING, "code: {0}", sqlm);
		}
		return null;
	}

	public ResultSet SQLquery(String Table, String condition) {
		SQLcheck();
		String sqlm = "";
		try {
			sqlm = "SELECT * FROM " + Table + " WHERE " + condition;
			ResultSet query = getSQL().getConnection().prepareStatement(sqlm, 1004, 1008).executeQuery();
			return query;
		} catch (SQLException ex) {
			Bukkit.getLogger().warning(ex.getLocalizedMessage());
			Bukkit.getLogger().log(Level.WARNING, "code: {0}", sqlm);
		}
		return null;
	}

	public Database getSQL() {
		return sql;
	}

	public DatabaseConfig getDatabaseConfig() {
		return databaseConfig;
	}

	public boolean databaseIsOpen() {
		return databaseIsOpen;
	}

	public void SQLcheck() {
		if (!sql.isOpen()) {
			sql.open();
		}
		databaseIsOpen = sql.isOpen();
	}

	public void update(String Table, String columnName, Object value, String QueryName, Object QueryValue) {

		ResultSet query = SQLquery(Table, QueryName, QueryValue);
		String sqlQuery = "";
		try {
			if (query != null) {
				if (query.next()) {
					sqlQuery = "UPDATE IGNORE " + Table + " SET " + columnName + " = '" + value + "' WHERE " + QueryName + " = '" + QueryValue + "'";
				} else {
					sqlQuery = "INSERT IGNORE INTO `" + Table + "`(`" + QueryName + "`, `" + columnName + "`) VALUES ('" + QueryValue + "', '" + value + "')";
				}
				if (debug_outputSQL) {
					Bukkit.getLogger().log(Level.INFO, "code: {0}", sqlQuery);
				}
				query.close();
				SQLqueryWithNoResult(sqlQuery);
			} else {
				SQLcheck();
			}
		} catch (SQLException e) {
			info(e.getLocalizedMessage());
			Bukkit.getLogger().log(Level.WARNING, "code: {0}", sqlQuery);
		}
	}


	public void update(String Table, String columnName, Object value, String QueryName, Object QueryValue, String QueryName2, Object QueryValue2) {
		ResultSet query = SQLquery(Table, "`" + QueryName + "` = '" + QueryValue + "' AND `" + QueryName2 + "` = '" + QueryValue2 + "'");
		String sqlQuery = "";
		try {
			if (query != null) {

				if (query.next()) {
					sqlQuery = "UPDATE IGNORE " + Table + " SET " + columnName + " = '" + value + "' WHERE " + QueryName + " = '" + QueryValue + "'";
				} else {
					sqlQuery = "INSERT IGNORE INTO `" + Table + "`(`" + QueryName + "`, `" + columnName + "`) VALUES ('" + QueryValue + "', '" + value + "')";
				}
				if (debug_outputSQL) {
					Bukkit.getLogger().log(Level.INFO, "code: {0}", sqlQuery);
				}
				query.close();
				SQLqueryWithNoResult(sqlQuery);
			} else {
				SQLcheck();
			}
		} catch (SQLException e) {
			info(e.getLocalizedMessage());
			Bukkit.getLogger().log(Level.WARNING, "code: {0}", sqlQuery);
		}
	}

	public ResultSet SQLquery(String Table, String[] queryNames, Object[] queryValues) {
		SQLcheck();
		StringBuilder sqlm = new StringBuilder();
		if (queryNames.length != queryValues.length) {
			throw new RuntimeException("请求的条件名与条件值数量不相符");
		}
		sqlm.append("SELECT * FROM ").append(Table).append(" WHERE ");
		for (int i = 0; i < queryNames.length; i++) {
			sqlm.append("`").append(queryNames[i]).append("` = '").append(queryValues[i]).append("'");
			if (i != queryNames.length - 1) {
				sqlm.append(" AND ");
			}
		}
		if (debug_outputSQL) {
			Bukkit.getLogger().log(Level.INFO, "code: {0}", sqlm.toString());
		}
		try {
			ResultSet query = getSQL().getConnection().prepareStatement(sqlm.toString(), 1004, 1008).executeQuery();
			return query;
		} catch (SQLException ex) {
			Bukkit.getLogger().warning(ex.getLocalizedMessage());
			Bukkit.getLogger().log(Level.WARNING, "code: {0}", sqlm);
		}
		return null;
	}

	public void update(String table, String[] columnNames, Object[] values, String[] queryNames, Object[] queryValues) {
		ResultSet query = SQLquery(table, queryNames, queryValues);
		StringBuilder sqlQuery = new StringBuilder();
		if (columnNames.length != values.length || queryNames.length != queryValues.length) {
			throw new RuntimeException("提交的字段名与字段值数量不相符");
		}
		try {
			if (query != null) {

				if (query.next()) {
					sqlQuery.append("UPDATE IGNORE ").append(table).append(" SET ");
					for (int i = 0; i < columnNames.length; i++) {
						sqlQuery.append(columnNames[i]).append(" = '").append(values[i]).append("'");
						if (i != columnNames.length - 1) {
							sqlQuery.append(", ");
						}
					}
					sqlQuery.append(" WHERE ");
					for (int i = 0; i < queryNames.length; i++) {
						sqlQuery.append("`").append(queryNames[i]).append("` = '").append(queryValues[i]).append("'");
						if (i != queryNames.length - 1) {
							sqlQuery.append(" AND ");
						}
					}
				} else {
					sqlQuery.append("INSERT IGNORE INTO `").append(table).append("`(");
					for (int i = 0; i < columnNames.length; i++) {
						sqlQuery.append("`").append(columnNames[i]).append("`");
						if (i != columnNames.length - 1) {
							sqlQuery.append(", ");
						} else {
							if (queryNames.length > 0) {
								sqlQuery.append(", ");
							}
						}
					}
					for (int i = 0; i < queryNames.length; i++) {
						sqlQuery.append("`").append(queryNames[i]).append("`");
						if (i != queryNames.length - 1) {
							sqlQuery.append(", ");
						}
					}
					sqlQuery.append(") VALUES (");
					for (int i = 0; i < values.length; i++) {
						sqlQuery.append("'").append(values[i]).append("'");
						if (i != values.length - 1) {
							sqlQuery.append(", ");
						} else {
							if (queryValues.length > 0) {
								sqlQuery.append(", ");
							}
						}
					}
					for (int i = 0; i < queryValues.length; i++) {
						sqlQuery.append("'").append(queryValues[i]).append("'");
						if (i != queryValues.length - 1) {
							sqlQuery.append(", ");
						}
					}
					sqlQuery.append(")");
				}
				if (debug_outputSQL) {
					Bukkit.getLogger().log(Level.INFO, "code: {0}", sqlQuery);
				}
				query.close();
				SQLqueryWithNoResult(sqlQuery.toString());
			} else {
				SQLcheck();
			}
		} catch (SQLException e) {
			info(e.getLocalizedMessage());
			Bukkit.getLogger().log(Level.SEVERE, "code: {0}", sqlQuery);
		}
	}

	public void insert(String table, String[] columnNames, Object[] values) {
		StringBuilder sqlQuery = new StringBuilder();
		if (columnNames.length != values.length) {
			throw new RuntimeException("提交的字段名与字段值数量不相符");
		}

		sqlQuery.append("INSERT IGNORE INTO `").append(table).append("`(");
		for (int i = 0; i < columnNames.length; i++) {
			sqlQuery.append("`").append(columnNames[i]).append("`");
			if (i != columnNames.length - 1) {
				sqlQuery.append(", ");
			}
		}
		sqlQuery.append(") VALUES (");
		for (int i = 0; i < values.length; i++) {
			sqlQuery.append("'").append(values[i]).append("'");
			if (i != values.length - 1) {
				sqlQuery.append(", ");
			}
		}
		sqlQuery.append(")");

		if (debug_outputSQL) {
			Bukkit.getLogger().log(Level.INFO, "code: {0}", sqlQuery);
		}

		SQLqueryWithNoResult(sqlQuery.toString());
	}

	public void delete(String Table, String queryName, Object queryValue) {
		SQLcheck();
		StringBuilder sqlm = new StringBuilder();
		sqlm.append("DELETE FROM ").append(Table).append(" WHERE `").append(queryName).append("` = '").append(queryValue).append("'");

		if (debug_outputSQL) {
			Bukkit.getLogger().log(Level.INFO, "code: {0}", sqlm);
		}

		SQLqueryWithNoResult(sqlm.toString());
	}

	public void delete(String Table, String[] queryNames, Object[] queryValues) {
		SQLcheck();
		StringBuilder sqlm = new StringBuilder();
		if (queryNames.length != queryValues.length) {
			throw new RuntimeException("请求的条件名与条件值数量不相符");
		}
		sqlm.append("DELETE FROM ").append(Table).append(" WHERE ");
		for (int i = 0; i < queryNames.length; i++) {
			sqlm.append("`").append(queryNames[i]).append("` = '").append(queryValues[i]).append("'");
			if (i != queryNames.length - 1) {
				sqlm.append(" AND ");
			}
		}

		if (debug_outputSQL) {
			Bukkit.getLogger().log(Level.INFO, "code: {0}", sqlm);
		}

		SQLqueryWithNoResult(sqlm.toString());
	}

	public void updateAsyn(String Table, String columnName, Object value, String QueryName, Object QueryValue) {
		new BukkitRunnable() {
			@Override
			public void run() {
				update(Table, columnName, value, QueryName, QueryValue);
				cancel();
			}
		}.runTaskAsynchronously(Main.getInstance());
	}

	public void updateAsyn(String table, String[] columnNames, Object[] values, String[] queryNames, Object[] queryValues) {
		new BukkitRunnable() {
			@Override
			public void run() {
				update(table, columnNames, values, queryNames, queryValues);
				cancel();
			}
		}.runTaskAsynchronously(Main.getInstance());
	}

	public void insertAsyn(String table, String[] columnNames, Object[] values) {
		new BukkitRunnable() {
			@Override
			public void run() {
				insert(table, columnNames, values);
				cancel();
			}
		}.runTaskAsynchronously(Main.getInstance());
	}

	public void info(String msg) {
		info(Bukkit.getConsoleSender(), msg);
	}

	public void info(CommandSender sender, String msg) {
		sender.sendMessage(msg.replace("&", "§"));
	}

}
