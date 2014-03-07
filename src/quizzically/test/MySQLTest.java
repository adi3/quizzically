package quizzically.test;
import quizzically.lib.*;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class MySQLTest {
	
	private MySQL sql;
	
	@Before
	public void testConnection() throws ClassNotFoundException, SQLException {
		sql = MySQL.getInstance();
	}
	
	@Test
	public void testGetAll() throws SQLException {
		ResultSet users = sql.get("users");
		while(users.next()) {
			System.out.println(users.getString(1) + " " + users.getString(2));
		}
		users.last();
		assertTrue(users.getRow() > 0);
	}
	
	
	@Test
	public void testGetSome() throws SQLException {
		String[] cols = {"email"};
		ResultSet user = sql.get(cols, "users", "name = 'Dominic Becker'");
		while(user.next()) {
			System.out.println(user.getString(1));
		}
		user.last();
		assertTrue(user.getRow() > 0);
		
		user = sql.get(cols, "users", "name LIKE '%Nic%'");
		while(user.next()) {
			System.out.println(user.getString(1));
		}
		user.last();
		assertTrue(user.getRow() > 0);
	}
	
	@Test
	public void testInsert() throws SQLException {
		String[] cols = {"name", "is_admin"};
		String[] vals = {"Matt Vitelli", "0"};
		ResultSet genKeys = sql.insert("users", cols, vals);
		assertTrue(genKeys.first());
	}
	
	@Test
	public void testUpdate() throws SQLException {
		int status = sql.update("users", "email = 'mvitelli@stanford.edu'", "name = 'Matt Vitelli'");
		assertTrue(status == 1);
	}
	
	@Test
	public void testDelete() throws SQLException {
		int num = sql.delete("users", "name = 'Matt Vitelli'");
		assertTrue(num > 0);
	}
}
