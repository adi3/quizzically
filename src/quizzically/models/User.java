package quizzically.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import quizzically.config.MyConfigVars;
import quizzically.config.MyDBInfo;
import quizzically.lib.MySql;
import quizzically.lib.SqlResult;

/**
 * Provides a representation of a user within the application, encapsulating their
 * attributes and behaviors. It facilitates user management, including retrieval,
 * searching, and friendship operations.
 */
public class User {

	private int id;
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
		this.id = Integer.parseInt(user.get(0).get("id"));
		this.name = user.get(0).get("name");
		this.email = user.get(0).get("email");
		this.loc = user.get(0).get("location");
		this.img = user.get(0).get("img");
		this.isAdmin = user.get(0).get("is_admin").equals("1") ? true : false;
	}
	

	/**
	 * Returns a User object based on the provided id, by calling the static method
	 * `getUserById` on the `User` class, passing the id as a string.
	 *
	 * @param id unique identifier for a user, used to retrieve the corresponding user object.
	 *
	 * @returns an instance of the `User` class, retrieved by `id`.
	 */
	public static User retrieve(int id) {
		return User.getUserById(Integer.toString(id));
	}

	/**
	 * Retrieves a User object from the database based on the provided ID. It uses the
	 * MySql instance to query the USERS_TABLE and returns a User object with the username
	 * corresponding to the specified ID. The function returns null if no matching ID is
	 * found.
	 *
	 * @param id identifier of a user to be retrieved from the database.
	 *
	 * @returns an instance of the `User` class with the specified username.
	 */
	public static User getUserById(String id) {
		SqlResult row = MySql.getInstance().get(MyDBInfo.USERS_TABLE, "id = " + id);
		return new User(row.get(0).get("username"));
	}
	
	/**
	 * Queries a database for users matching a given parameter in their name or username,
	 * then filters the results to exclude a specified username.
	 *
	 * @param param search criteria for users, allowing the function to filter the results
	 * based on the specified string.
	 *
	 * @param username username to be excluded from the search results.
	 *
	 * @returns a list of User objects matching the search parameter and excluding the
	 * specified username.
	 *
	 * The output is an ArrayList of User objects. Each User object contains a single
	 * attribute, the username.
	 */
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
	
	/**
	 * Returns a list of User objects representing friends of the current user, retrieved
	 * from the database based on the current user's username.
	 *
	 * @returns a list of usernames corresponding to the friends of the current user.
	 */
	public ArrayList<User> getFriends() {
		SqlResult row = sql.get(MyDBInfo.USERS_TABLE, "username = '" + this.getUsername() + "'");
		SqlResult friends = sql.getFriends(row.get(0).get("id"));
		
		ArrayList<User> results = new ArrayList<User>();
		for (int i = 0; i < friends.size(); i++) {
			results.add(new User(friends.get(i).get("username")));
		}
		return results;
	}
	
	
	/**
	 * Checks if a friendship request is pending between the current user and a specified
	 * user with the given username. It queries a database to verify the friendship status.
	 *
	 * @param username username of a potential friend to be searched in the database.
	 *
	 * @returns a boolean indicating whether a friend request between two users is pending.
	 */
	public boolean isPendingFriend(String username) {
		User friend = new User(username);
		SqlResult row = sql.get(MyDBInfo.FRIENDS_TABLE, "(id_1=" + this.getId() + " AND id_2=" + friend.getId() + ") "
							+ "OR (id_2=" + this.getId() + " AND id_1=" + friend.getId() + ")");
		
		if (row.isEmpty()) return false; 
		String confirmed = row.get(0).get("is_confirmed");
		return confirmed.equals("0") ? true : false;
	}
	
	/**
	 * Adds a new friend to the database and sends a request to the friend if the operation
	 * is successful.
	 * It takes a `User` object as input and returns a boolean indicating the success of
	 * the operation.
	 *
	 * @param friend user to whom a new friendship request is being sent.
	 *
	 * @returns a boolean value indicating whether the friend was successfully added to
	 * the database.
	 */
	public boolean addFriend(User friend) {
		String[] cols = {"id_1", "id_2", "is_confirmed"};
		String[] vals = {Integer.toString(getId()), Integer.toString(friend.getId()), "0"};
		int id = sql.insert(MyDBInfo.FRIENDS_TABLE, cols, vals);
		if(id != 0) new Message(MyConfigVars.REQUEST_MSG, "REQUEST", this, friend).save();
		return id != 0;
	}
	
	
	/**
	 * Confirms a friendship request by updating the database and sending a confirmation
	 * message to the requesting user. It takes a `User` object as input, updates the
	 * `is_confirmed` status in the database, and returns true if the update is successful.
	 *
	 * @param friend user whose friend request is being accepted.
	 *
	 * @returns a boolean value indicating whether the friend request was accepted or not.
	 */
	public boolean acceptRequest(User friend) {
		int status = sql.update(MyDBInfo.FRIENDS_TABLE, "is_confirmed=1", 
							"id_1=" + friend.getId() + " AND id_2=" + this.id);

		String msg = MyConfigVars.ACCEPT_MSG.replace("{Name}", this.name);
		if (status == 1) new Message(msg, "REQUEST", this, friend).save();
		return status == 1;
	}
	
	/**
	 * Deletes a friend from the database by checking if the friend exists in the friendships
	 * table based on the current user's id and the friend's id. It returns true if the
	 * deletion is successful.
	 *
	 * @param friend friend to be deleted from the user's friends list.
	 *
	 * @returns a boolean indicating whether a friend was successfully deleted from the
	 * database.
	 */
	public boolean deleteFriend(User friend) {
		int status = sql.delete(MyDBInfo.FRIENDS_TABLE, "(id_1=" + this.id + " AND id_2=" + friend.getId() + ") "
							+ "OR (id_2=" + this.id + " AND id_1=" + friend.getId() + ")");
		return status == 1;
	}
	
	
	/**
	 * Determines whether a specified user is in the list of friends. It checks if the
	 * provided user is contained within the collection of friends. The function returns
	 * a boolean value indicating the presence or absence of the user in the friends list.
	 *
	 * @param friend user to be verified as a friend.
	 *
	 * @returns a boolean value indicating whether the specified user is in the user's
	 * friends list.
	 */
	public boolean isFriend(User friend) {
		return this.getFriends().contains(friend);
	}
	
	/**
	 * Returns the value of the `name` variable as a string.
	 *
	 * @returns the value of the `name` variable.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns a string representing the email address.
	 * The email address is stored in the `email` variable.
	 * The function provides read-only access to the email field.
	 *
	 * @returns the value of the `email` variable as a string.
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Retrieves and returns the value of the `username` variable. It is a read-only
	 * accessor method, not modifying the `username` value. It provides a way to access
	 * the username outside the class.
	 *
	 * @returns a string representing the username.
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Returns the value of the `loc` variable as a string.
	 * The function provides a getter method for the `loc` field, allowing it to be
	 * accessed externally.
	 * It is likely used for encapsulation purposes in object-oriented programming.
	 *
	 * @returns a string value representing the location, which is stored in the `loc` variable.
	 */
	public String getLoc() {
		return loc;
	}
	
	/**
	 * Returns the value of the `img` variable, likely a string representing an image.
	 *
	 * @returns the value of the `img` variable.
	 */
	public String getImg() {
		return img;
	}
	
	/**
	 * Returns the value of the `id` variable.
	 * The `id` variable is presumably an integer field in the class.
	 *
	 * @returns the value of the instance variable `id`.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns a string representing a URL to a user's profile.
	 * The URL is constructed with the `getId` method, which presumably retrieves the
	 * user's ID.
	 * The resulting string is in the format "Profile?id=<user_id>".
	 *
	 * @returns a string in the format "Profile?id=<id_value>", where <id_value> is the
	 * value of the getId() method.
	 */
	public String profileLink() {
		return "Profile?id=" + getId();
	}

	/**
	 * Retrieves an array of `Quiz` objects associated with a specific owner, identified
	 * by the `getId()` method. The retrieval is performed by the `Quiz.retrieveByOwnerId`
	 * static method. The retrieved quizzes are returned as an array.
	 *
	 * @returns an array of `Quiz` objects retrieved by the `Quiz.retrieveByOwnerId` method.
	 */
	public Quiz[] quizzes() {
		return Quiz.retrieveByOwnerId(getId());
	}
	
	/**
	 * Returns a hash code value for the object based on its `username` field. The hash
	 * code is generated by calling the `hashCode` method on the `username` object. This
	 * allows the object to be used in hash-based data structures.
	 *
	 * @returns the hash code of the `username` string.
	 */
	@Override
	public int hashCode() {
		return this.username.hashCode();
    }
	
	/**
	 * Compares the current object with the given object for equality, returning true if
	 * they have the same username, false otherwise.
	 *
	 * @param o object being compared to the current object for equality.
	 *
	 * @returns a boolean value indicating whether the objects are equal based on their
	 * usernames.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		return this.username.equals(((User) o).getUsername());
	}
}
