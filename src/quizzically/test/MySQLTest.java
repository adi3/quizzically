package quizzically.test;
import quizzically.lib.*;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

/**
 * It is designed to test the functionality of a MySql database connection and various
 * SQL operations. The class uses JUnit testing framework to validate the results of
 * these operations.
 */
public class MySqlTest {
	
	private MySql sql;
	
	/**
	 * Sets up a MySQL connection using the `MySql` class, which is an instance of a
	 * database connection manager.
	 */
	@Before
	public void testConnection() throws ClassNotFoundException, SQLException {
		sql = MySql.getInstance();
	}
	
	/**
	 * Executes a SQL query to retrieve data from a "users" table, prints the name and
	 * email of each user, and asserts that at least one user exists in the result.
	 */
	@Test
	public void testGetAll() throws SQLException {
		SqlResult users = sql.get("users");
		for (HashMap<String, String> user : users) {
			System.out.println(user.get("name") + " " + user.get("email"));
		}
		assertTrue(users.size() > 0);
	}
	
	
	/**
	 * Tests database query functionality by retrieving email addresses from the "users"
	 * table based on two different conditions: name equals "DominicYO" and name containing
	 * "Nic". It asserts that at least one result is obtained for each condition.
	 */
	@Test
	public void testGetSome() throws SQLException {
		String[] cols = {"email"};
		SqlResult users = sql.get(cols, "users", "name = 'DominicYO'");
		
		for (int i = 0; i < users.size(); i++) 
			System.out.println(users.get(i).get("email"));
		
		assertTrue(users.size() > 0);
		
		users = sql.get(cols, "users", "name LIKE '%Nic%'");
		for (int i = 0; i < users.size(); i++)
			System.out.println(users.get(i).get("email"));
		
		assertTrue(users.size() > 0);
	}
	
	/**
	 * Tests the `insert` function of a SQL database, specifically inserting a new user
	 * into the "users" table with the given columns and values, and verifies that the
	 * insert operation was successful by checking for a non-zero ID.
	 */
	@Test
	public void testInsert() throws SQLException {
		String[] cols = {"name", "is_admin"};
		String[] vals = {"Matt Vitelli", "0"};
		int id = sql.insert("users", cols, vals);
		assertTrue(id != 0);
	}
	
	/**
	 * Tests the functionality of an SQL update operation. It updates records in the
	 * "users" table where the email address matches "mvitelli@stanford.edu" with a new
	 * name "Matt Vitelli". The function then asserts that the update operation was
	 * successful, i.e., more than one record was updated.
	 */
	@Test
	public void testUpdate() throws SQLException {
		int status = sql.update("users", "email = 'mvitelli@stanford.edu'", "name = 'Matt Vitelli'");
		assertTrue(status > 0);
	}
	
	/**
	 * Deletes a row from the "users" table where the "name" is 'Matt Vitelli' and asserts
	 * the number of deleted rows is greater than 0.
	 */
	@Test
	public void testDelete() throws SQLException {
		int num = sql.delete("users", "name = 'Matt Vitelli'");
		assertTrue(num > 0);
	}
}
