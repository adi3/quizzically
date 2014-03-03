package quizzically.lib;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class MySQL {

	private Statement stmt;
	
	public MySQL() {
		try {
			stmt = new DBConnection().getStatement();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	private ResultSet executeQuery(String sql) {
		try {
			return stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private int executeUpdate(String sql) {
		try {
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
	
	public int insert(String table, String[] vals) {
		String cols = "(name, email, is_admin, username, password, salt)";	// TODO : model should return these
		String sql = "INSERT INTO " + table + " " + cols + " VALUES (";
		
		for (String val : vals) sql += "'" + val + "',";
		sql = sql.replaceAll(",$", ")");
		return this.executeUpdate(sql);
	}
	
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
