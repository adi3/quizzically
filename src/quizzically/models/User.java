package quizzically.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import quizzically.lib.MySQL;

public class User {

	private int id;
	private String name;
	private String email;
	private String username;
	private boolean isAdmin;
	
	private MySQL sql;
	private static final String TABLE = "users";
	
	public User(String username) {
		this.username = username;
		sql = new MySQL();
		
		ResultSet user = sql.get(TABLE, "username = '" + username + "'");
		try {
			user.first();
			this.name = user.getString("name");
			this.email = user.getString("email");
			this.isAdmin = user.getString("is_admin").equals("1") ? true : false; 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, String> search(String param, String username) {
		HashMap<String, String> results = new HashMap<String, String>();
		
		String[] cols = {"name", "username"};
		ResultSet users = sql.get(cols, TABLE, "name LIKE '" + param + "%'");
		
		try {
			while(users.next()) {
				if (users.getString("username").equals(username)) continue;
				results.put(users.getString("username"), users.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getUsername() {
		return username;
	}
}
