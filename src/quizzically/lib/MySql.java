package quizzically.lib;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import quizzically.config.MyDBInfo;
import quizzically.models.Model;
import quizzically.models.Hydrator;

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
	
	/**
	 * Get the ResultSet from the sql query
	 * @param sql The SQL query
	 * @return ResultSet, never null
	 */
	private ResultSet executeQuery(String sql) {
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			return executeQuery(stmt);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("DB Error: Execute query failed");
		}
	}
	
	/**
	 * Get the ResultSet from the sql query
	 * @param stmt the statement to execute
	 * @return ResultSet, never null
	 */
	private ResultSet executeQuery(PreparedStatement stmt) {
		try {
			return stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("DB Error: Execute query failed");
		}
	}
	
	private int executeUpdate(PreparedStatement stmt) {
		try {
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("DB Error: Execute update failed");
		}
	}

	private int executeUpdate(String sql) {
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			return executeUpdate(stmt);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("DB Error: Execute query failed");
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
	
	/**
	 * number of tuples in result
	 * @param table
	 * @param where
	 * @return
	 */
	public int count(String table, String where){
		return get(table, where).size();
	}
	
	/**
	 * maximum value of column in result
	 * @param table
	 * @param where
	 * @param column
	 * @return
	 */
	public int max(String table, String where, String column){
		ResultSet rs = this.executeQuery("SELECT MAX(`" + column + "`) AS `MAX` FROM " + table + " WHERE " + where);
		SqlResult result = new SqlResult(rs);
		HashMap<String, String> row = result.get(0); // returns single row
		String max = row.get("MAX");
		if(max == null || max.equals("null")){
			return -1;
		}
		return Integer.parseInt(max);
	}
	
	
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
	
	public Model get(String sql, Hydrator hydrator) {
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			return hydrator.fromResultSet(this.executeQuery(stmt));
		} catch (SQLException e) {
			throw new RuntimeException("Problem retrieving from DB");
		}
	}
	
	public Model[] getMany(PreparedStatement stmt, Hydrator hydrator) {
		return hydrator.fromResultSet(this.executeQuery(stmt), true);
	}
	
	public Model get(PreparedStatement stmt, Hydrator hydrator) {
		return hydrator.fromResultSet(this.executeQuery(stmt));
	}
	
	public Model get(String table, int id, Hydrator hydrator) {
		try {
			String sql = "SELECT * FROM " + table + 
				" WHERE `id` = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, id);
			return get(stmt, hydrator);
		} catch (SQLException e) {
			throw new RuntimeException("Problem retrieving from DB");
		}
	}
	
	public SqlResult getFriends(String id) {
		String sql = "SELECT u2.username from users u1, users u2, friends f "
						+ "WHERE ((f.id_1 = u1.id AND f.id_2 = u2.id) "
						+ "OR (f.id_1 = u2.id AND f.id_2 = u1.id)) "
						+ "AND u1.id = " + id + " AND f.is_confirmed = 1";
		return new SqlResult(this.executeQuery(sql));
	}

	public int insert(String table, Model m, Hydrator hydrator) {
		String strCols = "";Arrays.asList(m.cols()).toString();
		String valMarkers = "";
		String sql;
		PreparedStatement stmt;
		int cur;

		for (String col : m.cols()) {
			strCols += "`" + col + "`,";
		}
		strCols = strCols.substring(0, strCols.length() - 1);

		for (int i = 0; i < m.cols().length; i++) {
			valMarkers += "?,";
		}
		valMarkers = valMarkers.substring(0, valMarkers.length() - 1);

		sql = "INSERT INTO " + 
			MyDBInfo.MYSQL_DATABASE_NAME + "." + table + 
			" (" + strCols + ") " + "VALUES (" + valMarkers + ")";

		try {
			stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			cur = 1;
			hydrator.prepareStatement(stmt, m, cur);

			stmt.executeUpdate();
			ResultSet keys = stmt.getGeneratedKeys();
			keys.first();
			return keys.getInt(1); // return id
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("DB insert failed");
		}
	}
	
	public void update(String table, Model m, Hydrator hydrator) {
		String[] cols = m.cols();
		String set = "";
		String sql;
		PreparedStatement stmt;
		int cur;

		for (String col : cols) {
			set += "`" + col + "` = ?,";
		}
		set = set.substring(0, set.length() - 1);

		sql = "UPDATE " + 
			MyDBInfo.MYSQL_DATABASE_NAME + "." + table + 
			" SET " + set + " WHERE `id` = ?";

		try {
			stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			cur = 1;
			hydrator.prepareStatement(stmt, m, cur);

			stmt.setInt(cur + cols.length, m.id());

			stmt.executeUpdate(); // fire and forget
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("DB update failed");
		}
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

	public int delete(QueryBuilder qb) {
		String sql = qb.sql();
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			qb.prepareStatement(stmt);
			return executeUpdate(stmt);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("DB delete failed");
		}
	}

	public Model[] getMany(QueryBuilder qb, Hydrator hydrator) {
		String sql = qb.sql();
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			qb.prepareStatement(stmt);
			return getMany(stmt, hydrator);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("DB query failed");
		}
		
	}
	
}
