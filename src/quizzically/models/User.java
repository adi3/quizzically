package quizzically.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import quizzically.config.MyConfigVars;
import quizzically.config.MyDBInfo;
import quizzically.lib.MySql;
import quizzically.lib.SqlResult;

public class User {

	private String id;
	private String name;
	private String email;
	private String loc;
	private String img;
	private String username;
	private boolean isAdmin;
	
	private MySql sql;
	
	public User(String username) {
		this.username = username;
		sql = MySql.getInstance();
		
		SqlResult user = sql.get(MyDBInfo.USERS_TABLE, "username = '" + username + "'");
		this.id = user.get(0).get("id");
		this.name = user.get(0).get("name");
		this.email = user.get(0).get("email");
		this.loc = user.get(0).get("location");
		this.img = user.get(0).get("img");
		this.isAdmin = user.get(0).get("is_admin").equals("1") ? true : false;
	}
	
	public static User getUserById(String id) {
		SqlResult row = MySql.getInstance().get(MyDBInfo.USERS_TABLE, "id = " + id);
		return new User(row.get(0).get("username"));
	}
	
	public static ArrayList<User> search(String param, String username) {
		String[] cols = {"username"};
		SqlResult users = MySql.getInstance().get(cols, MyDBInfo.USERS_TABLE, "name LIKE '" + param + "%' OR username LIKE '" + param + "%'");
		
		ArrayList<User> results = new ArrayList<User>();
		for (int i = 0; i < users.size(); i++) {
			String un = users.get(i).get("username");
			if (username != null && un.equals(username)) continue;
			results.add(new User(un));
		}
		return results;
	}
	
	public ArrayList<User> getFriends() {
		SqlResult row = sql.get(MyDBInfo.USERS_TABLE, "username = '" + this.getUsername() + "'");
		SqlResult friends = sql.getFriends(row.get(0).get("id"));
		
		ArrayList<User> results = new ArrayList<User>();
		for (int i = 0; i < friends.size(); i++) {
			results.add(new User(friends.get(i).get("username")));
		}
		return results;
	}
	
	
	public boolean isPendingFriend(String username) {
		User friend = new User(username);
		SqlResult row = sql.get(MyDBInfo.FRIENDS_TABLE, "(id_1=" + this.getId() + " AND id_2=" + friend.getId() + ") "
							+ "OR (id_2=" + this.getId() + " AND id_1=" + friend.getId() + ")");
		
		if (row.isEmpty()) return false; 
		String confirmed = row.get(0).get("is_confirmed");
		return confirmed.equals("0") ? true : false;
	}
	
	public boolean addFriend(User friend) {
		String[] cols = {"id_1", "id_2", "is_confirmed"};
		String[] vals = {this.id, friend.getId(), "0"};
		int id = sql.insert(MyDBInfo.FRIENDS_TABLE, cols, vals);
		if(id != 0) new Message(MyConfigVars.REQUEST_MSG, "REQUEST", this, friend).save();
		return id != 0;
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
	
	public String getLoc() {
		return loc;
	}
	
	public String getImg() {
		return img;
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
