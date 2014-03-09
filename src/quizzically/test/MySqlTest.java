package quizzically.test;
import quizzically.lib.*;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class MySqlTest {
	
	private MySql sql;
	
	@Before
	public void testConnection() throws ClassNotFoundException, SQLException {
		sql = MySql.getInstance();
	}
	
	@Test
	public void testGetAll() throws SQLException {
		SqlResult users = sql.get("users");
		for (HashMap<String, String> user : users) {
			System.out.println(user.get("name") + " " + user.get("email"));
		}
		assertTrue(users.size() > 0);
	}
	
	
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
	
	@Test
	public void testInsert() throws SQLException {
		String[] cols = {"name", "is_admin"};
		String[] vals = {"Matt Vitelli", "0"};
		int id = sql.insert("users", cols, vals);
		assertTrue(id != 0);
	}
	
	@Test
	public void testUpdate() throws SQLException {
		int status = sql.update("users", "email = 'mvitelli@stanford.edu'", "name = 'Matt Vitelli'");
		assertTrue(status > 0);
	}
	
	@Test
	public void testDelete() throws SQLException {
		int num = sql.delete("users", "name = 'Matt Vitelli'");
		assertTrue(num > 0);
	}
}
