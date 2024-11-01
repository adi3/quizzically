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
 * It is a JUnit test class designed to validate the functionality of a MySql database
 * connection and CRUD (Create, Read, Update, Delete) operations.
 */
public class MySqlTest {
	
	private MySql sql;
	
	/**
	 * Establishes a connection to a MySQL database using the `MySql` singleton instance
	 * and stores it in the `sql` variable.
	 */
	@Before
	public void testConnection() throws ClassNotFoundException, SQLException {
		sql = MySql.getInstance();
	}
	
	/**
	 * Tests the retrieval of all users from a database. It executes a query for the
	 * "users" table, prints the name and email of each user, and verifies that at least
	 * one user exists.
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
	 * Tests the retrieval of database records based on two different queries. It checks
	 * if records exist for users with names 'DominicYO' and names containing 'Nic'. The
	 * function uses SQL queries to fetch email addresses from the 'users' table.
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
	 * Inserts data into a "users" table with specified columns and values, and then
	 * asserts that the resulting ID is not zero. The function relies on a separate SQL
	 * insertion method, which is not shown here.
	 */
	@Test
	public void testInsert() throws SQLException {
		String[] cols = {"name", "is_admin"};
		String[] vals = {"Matt Vitelli", "0"};
		int id = sql.insert("users", cols, vals);
		assertTrue(id != 0);
	}
	
	/**
	 * Updates a row in the "users" table by setting the "email" to "mvitelli@stanford.edu"
	 * and the "name" to "Matt Vitelli". It then verifies that the update was successful
	 * by checking if the update status is greater than 0.
	 */
	@Test
	public void testUpdate() throws SQLException {
		int status = sql.update("users", "email = 'mvitelli@stanford.edu'", "name = 'Matt Vitelli'");
		assertTrue(status > 0);
	}
	
	/**
	 * Tests the deletion of a record from the "users" table based on a specified condition.
	 * It executes a SQL delete statement and verifies that at least one record was
	 * deleted. The result is asserted to be greater than 0.
	 */
	@Test
	public void testDelete() throws SQLException {
		int num = sql.delete("users", "name = 'Matt Vitelli'");
		assertTrue(num > 0);
	}
}
