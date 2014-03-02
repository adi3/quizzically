package quizzically.lib;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class MySQL {

	private Statement stmt;
	
	public MySQL() throws ClassNotFoundException, SQLException {
		stmt = new DBConnection().getStatement();
	}
	
	public ResultSet get(String table) throws SQLException {
		return stmt.executeQuery("SELECT * FROM " + table);
	}
	
	public ResultSet get(String[] cols, String table) throws SQLException {
		String strCols = Arrays.asList(cols).toString();
		strCols = strCols.substring(1, strCols.length() - 1);
		return stmt.executeQuery("SELECT " + strCols + " FROM " + table);
	}
	
	public ResultSet get(String[] cols, String table, String where) throws SQLException {
		String strCols = Arrays.asList(cols).toString();
		strCols = strCols.substring(1, strCols.length() - 1);
		return stmt.executeQuery("SELECT " + strCols + " FROM " + table + " WHERE " + where);
	}
	
	public int insert(String table, String[] vals) throws SQLException {
		String cols = "(name, email, is_admin)";
		String sql = "INSERT INTO " + table + " " + cols + " VALUES (";
		
		for (String val : vals) sql += "'" + val + "',";
		sql = sql.replaceAll(",$", ")");
		return stmt.executeUpdate(sql);
	}
	
	public int insert(String table, String[] cols, String[] vals) throws SQLException {
		String strCols = Arrays.asList(cols).toString();
		strCols = strCols.substring(1, strCols.length() - 1);
		String sql = "INSERT INTO " + table + " (" + strCols + ") VALUES (";
		
		for (String val : vals) sql += "'" + val + "',";
		sql = sql.replaceAll(",$", ")");
		return stmt.executeUpdate(sql);
	}
	
	public int update(String table, String set, String where) throws SQLException {
		return stmt.executeUpdate("UPDATE " + table + " SET " + set + " WHERE " + where);
	}
	
	public int delete(String table, String where) throws SQLException {
		return stmt.executeUpdate("DELETE FROM " + table + " WHERE " + where);
	}
}
