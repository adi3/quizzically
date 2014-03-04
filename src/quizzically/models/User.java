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

	private String id;
	private String name;
	private String email;
	private String username;
	private boolean isAdmin;
	
	private MySQL sql;
	private static final String USERS_TABLE = "users";
	private static final String FRIENDS_TABLE = "friends";
	
	public User(String username) {
		this.username = username;
		sql = new MySQL();
		
		ResultSet user = sql.get(USERS_TABLE, "username = '" + username + "'");
		try {
			user.first();
			this.id = user.getString("id");
			this.name = user.getString("name");
			this.email = user.getString("email");
			this.isAdmin = user.getString("is_admin").equals("1") ? true : false; 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<User> search(String param, String username) {
		String[] cols = {"username"};
		ResultSet users = sql.get(cols, USERS_TABLE, "name LIKE '" + param + "%'");
		
		try {
			ArrayList<User> results = new ArrayList<User>();
			while(users.next()) {
				if (users.getString("username").equals(username)) continue;
				results.add(new User(users.getString("username")));
			}
			return results;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<User> getFriends() {
		ResultSet set = sql.get(USERS_TABLE, "username = '" + this.getUsername() + "'");
		
		try {
			set.first();
			ResultSet friends = sql.getFriends(set.getString("id"));
			ArrayList<User> results = new ArrayList<User>();
			
			while(friends.next()) results.add(new User(friends.getString("username")));
			return results;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean addFriend(User friend) {
		String[] cols = {"id_1", "id_2"};
		String[] vals = {this.id, friend.getId()};
		int status = sql.insert(FRIENDS_TABLE, cols, vals);
		return status == 1;
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
	
	public String getId() {
		return id;
	}
	
	@Override
	public int hashCode() {
		return this.username.hashCode();
    }
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		return this.username.equals(((User) o).getUsername());
	}
}
