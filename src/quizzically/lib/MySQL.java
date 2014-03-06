package quizzically.lib;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class MySQL {

	// singleton
	private static MySQL instance = null;

	private DBConnection con;
	
	private MySQL() {
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
	public static MySQL getInstance() {
		if (instance != null) {
			return instance;
		}
		instance = new MySQL();
		return instance;
	}
	
	private ResultSet executeQuery(String sql) {
		try {
			Statement stmt = con.getStatement();
			return stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private int executeUpdate(String sql) {
		try {
			Statement stmt = con.getStatement();
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public ResultSet get(String table) {
		return this.executeQuery("SELECT * FROM " + table);
	}
	
	/*
	public ResultSet get(String[] cols, String table) {
		String strCols = Arrays.asList(cols).toString();
		strCols = strCols.substring(1, strCols.length() - 1);
		return this.executeQuery("SELECT " + strCols + " FROM " + table);
	}
	*/
	
	public ResultSet get(String table, String where) {
		return this.executeQuery("SELECT * FROM " + table + " WHERE " + where);
	}
	
	public ResultSet get(String[] cols, String table, String where) {
		String strCols = Arrays.asList(cols).toString();
		strCols = strCols.substring(1, strCols.length() - 1);
		return this.executeQuery("SELECT " + strCols + " FROM " + table + " WHERE " + where);
	}
	
	public ResultSet getFriends(String id) {
		String sql = "SELECT u2.username from users u1, users u2, friends f "
						+ "WHERE ((f.id_1 = u1.id AND f.id_2 = u2.id) "
						+ "OR (f.id_1 = u2.id AND f.id_2 = u1.id)) "
						+ "AND u1.id = " + id + " AND f.is_confirmed = 1";
		return this.executeQuery(sql);
	}

	/*
	public int insert(String table, String[] vals) {
		String cols = "(name, email, is_admin, username, password, salt)";	// TODO : model should return these
		String sql = "INSERT INTO " + table + " " + cols + " VALUES (";
		
		for (String val : vals) sql += "'" + val + "',";
		sql = sql.replaceAll(",$", ")");
		return this.executeUpdate(sql);
	}
	*/
	
	public int insert(String table, String[] cols, String[] vals) {
		String strCols = Arrays.asList(cols).toString();
		strCols = strCols.substring(1, strCols.length() - 1);
		String sql = "INSERT INTO " + table + " (" + strCols + ") VALUES (";
		
		for (String val : vals) sql += "'" + val + "',";
		sql = sql.replaceAll(",$", ")");
		return this.executeUpdate(sql);
	}
	
	public int update(String table, String set, String where) {
		return this.executeUpdate("UPDATE " + table + " SET " + set + " WHERE " + where);
	}
	
	public int delete(String table, String where) {
		return this.executeUpdate("DELETE FROM " + table + " WHERE " + where);
	}
}
