package quizzically.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import quizzically.config.MyConfigVars;
import quizzically.config.MyDBInfo;
import quizzically.lib.MySQL;

public class User {

	private String id;
	private String name;
	private String email;
	private String username;
	private boolean isAdmin;
	
	private MySQL sql;
	
	public User(String username) {
		this.username = username;
		sql = MySQL.getInstance();
		
		ResultSet user = sql.get(MyDBInfo.USERS_TABLE, "username = '" + username + "'");
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
	
	public static User getUserById(String id) {
		ResultSet set = MySQL.getInstance().get(MyDBInfo.USERS_TABLE, "id = " + id);
		try {
			set.first();
			return new User(set.getString("username"));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<User> search(String param, String username) {
		String[] cols = {"username"};
		ResultSet users = sql.get(cols, MyDBInfo.USERS_TABLE, "name LIKE '" + param + "%'");
		
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
		ResultSet set = sql.get(MyDBInfo.USERS_TABLE, "username = '" + this.getUsername() + "'");
		
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
		String[] cols = {"id_1", "id_2", "is_confirmed"};
		String[] vals = {this.id, friend.getId(), "0"};
		ResultSet genKeys = sql.insert(MyDBInfo.FRIENDS_TABLE, cols, vals);
		if(genKeys == null) return false;
		try{
			if (genKeys.first()) new Message(MyConfigVars.REQUEST_MSG, "REQUEST", this, friend).save();
			return genKeys.first(); // true when insertion succeeded
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean acceptRequest(User friend) {
		int status = sql.update(MyDBInfo.FRIENDS_TABLE, "is_confirmed=1", 
							"id_1=" + friend.getId() + " AND id_2=" + this.id);

		String msg = MyConfigVars.ACCEPT_MSG.replace("{Name}", this.name);
		if (status == 1) new Message(msg, "REQUEST", this, friend).save();
		return status == 1;
	}
	
	
	public boolean isFriend(User friend) {
		return this.getFriends().contains(friend);
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
