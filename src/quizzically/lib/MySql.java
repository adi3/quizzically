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

/**
 * Provides a singleton interface to interact with a MySQL database, offering methods
 * for executing queries, inserting, updating, and deleting data. It utilizes a
 * DBConnection object to establish and manage the database connection.
 */
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
	
	/**
	 * Closes a database connection.
	 */
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
	
	/**
	 * Executes a database update operation using a given PreparedStatement and returns
	 * the number of rows affected. It catches and logs any SQLExceptions, then rethrows
	 * a RuntimeException with a custom error message.
	 *
	 * @param stmt prepared SQL statement to which the `executeUpdate` method is applied.
	 *
	 * @returns the number of rows affected by the SQL update statement.
	 */
	private int executeUpdate(PreparedStatement stmt) {
		try {
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("DB Error: Execute update failed");
		}
	}

	/**
	 * Executes a SQL query on a database connection, returning the number of updated
	 * rows. It takes a SQL statement as input and catches any SQL exceptions, logging
	 * the error and throwing a RuntimeException.
	 *
	 * @param sql SQL query to be executed on the database.
	 *
	 * @returns an integer representing the number of rows affected by the SQL query.
	 */
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
	
	/**
	 * Executes a SQL query to select all columns from a specified table in a database.
	 * It returns a `SqlResult` object containing the query results. The table name is
	 * passed as a parameter to the function.
	 *
	 * @param table name of the database table to be queried.
	 *
	 * @returns an instance of `SqlResult` containing the result set from the specified
	 * database table.
	 */
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
	
	
	/**
	 * Executes a SQL query to retrieve data from a specified table based on a given
	 * condition. It takes a table name and a WHERE clause as input, executes the query,
	 * and returns the result as an instance of `SqlResult`.
	 *
	 * @param table table name in the SQL query.
	 *
	 * @param where conditions of the SQL query filter, specifying which rows to include
	 * in the result set.
	 *
	 * @returns a `SqlResult` object containing a `ResultSet` from the specified query.
	 */
	public SqlResult get(String table, String where) {
		ResultSet rs = this.executeQuery("SELECT * FROM " + table + " WHERE " + where);
		return new SqlResult(rs);
	}
	
	/**
	 * Executes a custom SQL query, retrieves the result set, and returns a `SqlResult`
	 * object containing the query's data. It takes a SQL query string as input and
	 * utilizes an underlying `executeQuery` method to perform the database operation.
	 *
	 * @param query custom SQL query to be executed.
	 *
	 * @returns an instance of SqlResult containing the result of the custom SQL query execution.
	 */
	public SqlResult getCustomQuery(String query){
		ResultSet rs = this.executeQuery(query);
		return new SqlResult(rs);
	}
	
	/**
	 * Constructs a SQL query string based on the provided column names, table name, and
	 * where clause conditions, then executes the query and returns the result as a
	 * SqlResult object.
	 *
	 * @param cols columns to be selected from the database table.
	 *
	 * @param table name of the database table from which the query will be executed.
	 *
	 * @param where condition for filtering records in the SQL query.
	 *
	 * @returns an instance of `SqlResult` containing query results.
	 */
	public SqlResult get(String[] cols, String table, String where) {
		String strCols = Arrays.asList(cols).toString();
		strCols = strCols.substring(1, strCols.length() - 1);
		String sql = "SELECT " + strCols + " FROM " + table + " WHERE " + where;
		return new SqlResult(this.executeQuery(sql));
	}
	
	/**
	 * Executes a SQL query using a prepared statement, retrieves the results, and hydrates
	 * them using a specified Hydrator instance, returning the hydrated model. It catches
	 * any SQL exceptions and wraps them in a RuntimeException.
	 *
	 * @param sql SQL query that is executed to retrieve data from the database.
	 *
	 * @param hydrator an object responsible for mapping database result set rows to a
	 * domain object model.
	 *
	 * @returns a hydrated model object, resulting from the execution of the given SQL query.
	 */
	public Model get(String sql, Hydrator hydrator) {
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			return hydrator.fromResultSet(this.executeQuery(stmt));
		} catch (SQLException e) {
			throw new RuntimeException("Problem retrieving from DB");
		}
	}
	
	/**
	 * Executes a query using a PreparedStatement, retrieves the results, and hydrates
	 * them into Model objects using the provided Hydrator. The hydrated models are then
	 * returned as an array. The query is executed with the option to fetch all results
	 * at once.
	 *
	 * @param stmt prepared SQL statement that is used to execute a query.
	 *
	 * @param hydrator object responsible for mapping the result set data into Model objects.
	 *
	 * @returns an array of Model objects hydrated from a database query result set.
	 */
	public Model[] getMany(PreparedStatement stmt, Hydrator hydrator) {
		return hydrator.fromResultSet(this.executeQuery(stmt), true);
	}
	
	/**
	 * Executes a query with a given `PreparedStatement` and uses the result to hydrate
	 * a `Model` object through a `Hydrator`.
	 *
	 * @param stmt prepared SQL statement to be executed.
	 *
	 * @param hydrator an object responsible for mapping data from a result set to an object.
	 *
	 * @returns an instance of the Model class, hydrated from a database query.
	 */
	public Model get(PreparedStatement stmt, Hydrator hydrator) {
		return hydrator.fromResultSet(this.executeQuery(stmt));
	}
	
	/**
	 * Retrieves a model object from the database based on the specified table name and
	 * ID. It uses a prepared statement to prevent SQL injection and attempts to hydrate
	 * the object using a provided hydrator.
	 *
	 * @param table name of the database table from which data is to be retrieved.
	 *
	 * @param id unique identifier used to filter the database records in the SQL query.
	 *
	 * @param hydrator object used to convert database results into a Java object model.
	 *
	 * Destructure is not applicable here as `hydrator` is a single object. It is an
	 * instance of `Hydrator` class, likely containing methods for mapping database query
	 * results to objects.
	 *
	 * @returns an instance of the `Model` class populated with database data.
	 *
	 * The Model object returned by the function is hydrated by the provided Hydrator.
	 */
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
	
	/**
	 * Fetches a list of usernames of confirmed friends for a given user, identified by
	 * their user ID, from a database via a SQL query.
	 *
	 * @param id identifier of the user whose friends are being retrieved.
	 * It is used to filter the results to include only friends of the specified user.
	 *
	 * @returns a list of usernames of confirmed friends of the user with the specified
	 * id.
	 */
	public SqlResult getFriends(String id) {
		String sql = "SELECT u2.username from users u1, users u2, friends f "
						+ "WHERE ((f.id_1 = u1.id AND f.id_2 = u2.id) "
						+ "OR (f.id_1 = u2.id AND f.id_2 = u1.id)) "
						+ "AND u1.id = " + id + " AND f.is_confirmed = 1";
		return new SqlResult(this.executeQuery(sql));
	}

	/**
	 * Inserts a new record into a specified database table, utilizing a Model object for
	 * column names and a Hydrator object for preparing parameterized SQL statements. It
	 * returns the generated ID of the newly inserted record.
	 *
	 * @param table name of the table in the database into which a new record is to be inserted.
	 *
	 * @param m Model object, which contains the column names of the table to be inserted
	 * into and possibly other metadata.
	 *
	 * Contain cols.
	 *
	 * @param hydrator an object that prepares the database statement with values from
	 * the `Model` object `m`, allowing it to set the correct parameters for the SQL query.
	 *
	 * Hydrate. The `hydrator` is likely a class that implements a hydration mechanism
	 * for mapping data between Java objects and SQL queries. It has a method `prepareStatement`
	 * that takes a `PreparedStatement`, a `Model`, and a parameter index as input.
	 *
	 * @returns the ID of the newly inserted record.
	 */
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
	
	/**
	 * Updates a row in a database table based on the provided `Model` object and its
	 * associated `Hydrator`. It constructs an SQL query to update the specified table
	 * with the model's values and executes the query using a prepared statement.
	 *
	 * @param table name of the database table to be updated.
	 *
	 * @param m model object, providing access to its properties such as column names and
	 * the ID.
	 *
	 * Deconstruct `m` into its main components: its columns, its ID, and its hydrator.
	 *
	 * @param hydrator an object responsible for setting the prepared statement's parameters
	 * based on the provided model.
	 *
	 * Have its main properties.
	 */
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

	/**
	 * Executes a SQL INSERT statement to insert data into a specified table, returns the
	 * auto-generated primary key of the inserted row, and throws a RuntimeException if
	 * the insertion fails.
	 *
	 * @param table name of the database table into which data will be inserted.
	 *
	 * @param cols column names in the database table to be inserted into.
	 *
	 * Are destructured into an array of strings.
	 *
	 * @param vals values to be inserted into the specified table and columns.
	 *
	 * Deconstruct `vals` as an array of strings.
	 *
	 * @returns an integer representing the primary key of the inserted row.
	 *
	 * The output is an integer representing the primary key of the newly inserted row.
	 */
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
	
	/**
	 * Executes an SQL UPDATE query on a specified table, applying changes to the table
	 * based on a specified WHERE condition, and returns an integer result. The `set`
	 * parameter defines the column(s) to modify and their new values. The `where` parameter
	 * defines the condition for the update operation.
	 *
	 * @param table name of the database table to be updated.
	 *
	 * @param set column(s) and new value(s) to be updated in the specified table.
	 *
	 * @param where condition for which rows in the specified `table` are updated.
	 *
	 * @returns the number of rows updated in the database.
	 */
	public int update(String table, String set, String where) {
		return this.executeUpdate("UPDATE " + table + " SET " + set + " WHERE " + where);
	}
	
	/**
	 * Executes a SQL DELETE query on a specified database table based on a provided WHERE
	 * condition, returning the number of rows affected. The table and WHERE clause are
	 * concatenated into the SQL query string.
	 *
	 * @param table name of the database table from which to delete data.
	 *
	 * @param where conditions for the rows to be deleted in the specified table.
	 *
	 * @returns the number of rows deleted from the database table.
	 */
	public int delete(String table, String where) {
		return this.executeUpdate("DELETE FROM " + table + " WHERE " + where);
	}

	/**
	 * Executes a SQL query to delete data from a database based on a QueryBuilder object,
	 * prepares the statement, and returns an integer result.
	 *
	 * @param qb QueryBuilder object that encapsulates the SQL query to be executed for
	 * deletion.
	 *
	 * Extract its main properties.
	 *
	 * @returns an integer representing the number of rows deleted.
	 *
	 * The `delete` function returns an integer value representing the number of rows
	 * affected by the deletion operation.
	 */
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

	/**
	 * Executes a database query, prepares a statement, and returns an array of model
	 * objects based on the query results. It uses a QueryBuilder to construct the query
	 * and a Hydrator to hydrate the results. The function catches and logs SQL exceptions.
	 *
	 * @param qb QueryBuilder object that generates the SQL query to be executed.
	 *
	 * Destructured, the `qb` object has the following properties:
	 * - `sql`: a string representing the SQL query.
	 * - `prepareStatement`: a method that prepares the SQL query.
	 *
	 * @param hydrator object responsible for mapping database query results to the
	 * corresponding Model objects.
	 *
	 * Hydrate.
	 * It has methods to map data from a data source to an object.
	 * It typically contains metadata about the object's structure.
	 *
	 * @returns an array of `Model` objects populated with query results.
	 *
	 * The returned output is an array of Model objects.
	 */
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
