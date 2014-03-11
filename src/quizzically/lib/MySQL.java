package quizzically.lib;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MySql {

	// singleton
	private static MySql instance = null;

	private DBConnection con;
	
	private MySql() {
		try {
			con = new DBConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		con.close();
	}

	/**
	 * Get a MySQL singleton object
	 */
	public static MySql getInstance() {
		if (instance != null) {
			return instance;
		}
		instance = new MySql();
		return instance;
	}
	
	private ResultSet executeQuery(String sql) {
		try {
			Statement stmt = con.getStatement();
			return stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("DB Error: Execute query failed");
		}
	}
	
	private int executeUpdate(String sql) {
		try {
			Statement stmt = con.getStatement();
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("DB Error: Execute update failed");
		}
	}
	
	/**
	 * Similar to executeUpdate, but returns ResultSet of generated
	 * keys instead of number of affected rows. If no insertions were made
	 * the ResultSet will be empty (or null on error)
	 * @param sql
	 * @return ResultSet of generated keys
	 */
	private ResultSet executeInsertion(String sql) {
		try {
			Statement stmt = con.getStatement();
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			return stmt.getGeneratedKeys();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("DB Error: Execute insertion failed");
		}
	}
	
	public SqlResult get(String table) {
		ResultSet rs = this.executeQuery("SELECT * FROM " + table);
		return new SqlResult(rs);
	}
	
	/*
	public ResultSet get(String[] cols, String table) {
		String strCols = Arrays.asList(cols).toString();
		strCols = strCols.substring(1, strCols.length() - 1);
		return this.executeQuery("SELECT " + strCols + " FROM " + table);
	}
	*/
	
	public SqlResult get(String table, String where) {
		ResultSet rs = this.executeQuery("SELECT * FROM " + table + " WHERE " + where);
		return new SqlResult(rs);
	}
	
	public SqlResult get(String[] cols, String table, String where) {
		String strCols = Arrays.asList(cols).toString();
		strCols = strCols.substring(1, strCols.length() - 1);
		String sql = "SELECT " + strCols + " FROM " + table + " WHERE " + where;
		return new SqlResult(this.executeQuery(sql));
	}
	
	public SqlResult getFriends(String id) {
		String sql = "SELECT u2.username from users u1, users u2, friends f "
						+ "WHERE ((f.id_1 = u1.id AND f.id_2 = u2.id) "
						+ "OR (f.id_1 = u2.id AND f.id_2 = u1.id)) "
						+ "AND u1.id = " + id + " AND f.is_confirmed = 1";
		return new SqlResult(this.executeQuery(sql));
	}
	
	public int insert(String table, String[] cols, String[] vals) {
		String strCols = Arrays.asList(cols).toString();
		strCols = strCols.substring(1, strCols.length() - 1);
		String sql = "INSERT INTO " + table + " (" + strCols + ") VALUES (";
		
		for (String val : vals) sql += "'" + val + "',";
		sql = sql.replaceAll(",$", ")");
		ResultSet keys = executeInsertion(sql);
		
		try {
			keys.first();
			return keys.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("DB insert failed");
		}
	}
	
	public int update(String table, String set, String where) {
		return this.executeUpdate("UPDATE " + table + " SET " + set + " WHERE " + where);
	}
	
	public int delete(String table, String where) {
		return this.executeUpdate("DELETE FROM " + table + " WHERE " + where);
	}
}
