package quizzically.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import quizzically.config.MyConfigVars;
import quizzically.config.MyDBInfo;
import quizzically.lib.MySql;
import quizzically.lib.SqlResult;

/**
 * Models and manages user messages, including saving, retrieving, and updating message
 * information.
 */
public class Message {

	private String id;
	private String msg;
	private User from;
	private User to;
	private String type;		// 1: REQUEST, 2: CHALLENGE, 4: NOTE
	private boolean isRead;
	private Date date;
	
	private MySql sql;
	private static final String DATE_FORMAT = "dd-MMM-yy hh:mm a";
	
	public Message(String msg, String type, User from, User to) {
		sql = MySql.getInstance();
		
		this.msg = msg;
		this.from = from;
		this.to = to;
		this.type = type;
		
		this.isRead = false;
		this.date = new Date();
	}
	
	public Message(String id, String msg, String type, boolean isRead, User from, User to, String date) {
		sql = MySql.getInstance();
		
		this.id = id;
		this.msg = msg;
		this.from = from;
		this.to = to;
		this.type = type;
		this.isRead = isRead;
		
		try {
			this.date = new SimpleDateFormat(DATE_FORMAT).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Inserts a message into a database table, returning true if the insertion is
	 * successful. It constructs an array of column names and values to be inserted, calls
	 * the `insert` method of a `sql` object, and checks the returned ID for zero.
	 *
	 * @returns a boolean value indicating whether the message was saved successfully.
	 */
	public boolean save() {
		String[] cols = {"text", "from_id", "to_id", "type", "is_read", "created_at"};
		String isRead = this.isRead ? "1" : "0";
		String[] vals = {this.msg, Integer.toString(this.from.getId()), 
			Integer.toString(this.to.getId()), this.type, isRead, this.getDate()};
		
		int id = sql.insert(MyDBInfo.MESSAGES_TABLE, cols, vals);
		return id != 0;
	}
	
	/**
	 * Checks if a user has unread messages by iterating through their messages and
	 * returning true if any are unread, otherwise returning false. It requires a username
	 * to access the corresponding user's messages.
	 *
	 * @param username username of the user whose messages are retrieved to check for
	 * unread messages.
	 *
	 * @returns a boolean indicating the presence of unread messages for the specified user.
	 */
	public static boolean hasUnread(String username) {
		ArrayList<Message> msgs = getMessages(new User(username));
		
		for (Message msg : msgs) {
			if (!msg.isRead()) return true;
		}
		return false;
	}
	
	/**
	 * Retrieves a list of messages sent to a specified user, sorted by creation date in
	 * descending order, from a database table. It creates a `Message` object for each
	 * retrieved message and returns the list of messages.
	 *
	 * @param user recipient of messages, used to filter messages from the database based
	 * on the `to_id` column.
	 *
	 * Extracted:
	 * - `user.getId()`: retrieves the user's ID.
	 * - `user.getId()` is used to filter messages in the database.
	 * - `user` is expected to have an `id` property.
	 *
	 * @returns a list of messages sent to the specified user, ordered by creation date.
	 *
	 * The returned output is a list of `Message` objects. Each `Message` object is a
	 * collection of attributes including message ID, text, type, read status, sender,
	 * recipient, and creation date.
	 */
	public static ArrayList<Message> getMessages(User user) {
		ArrayList<Message> msgs = new ArrayList<Message>();
		SqlResult rows = MySql.getInstance().get(MyDBInfo.MESSAGES_TABLE, "to_id = '" + user.getId() + "' ORDER BY created_at DESC");
		
		for (HashMap<String, String> row : rows) {
			User from = User.getUserById(row.get("from_id"));
			User to = User.getUserById(row.get("to_id"));
			boolean isRead = row.get("is_read").equals("1") ? true : false;
			String date = row.get("created_at");
			msgs.add(new Message(row.get("id"), row.get("text"), row.get("type"), isRead, from, to, date));
		}
		return msgs;
	}
	
	/**
	 * Retrieves a message from a database based on its ID, retrieves associated user
	 * information, and returns a Message object containing the message details and user
	 * information. It retrieves data from the database and populates a Message object.
	 * It then returns the populated Message object.
	 *
	 * @param id identifier for a message to be retrieved from the database.
	 *
	 * @returns an instance of the Message class, containing message details and associated
	 * user information.
	 *
	 * Returned output is an instance of the `Message` class, with the following properties:
	 * - `id`: a string representing the message ID.
	 * - `text`: a string representing the message text.
	 * - `type`: a string representing the message type.
	 * - `isRead`: a boolean indicating whether the message is read.
	 * - `from`: an instance of the `User` class representing the sender.
	 * - `to`: an instance of the `User` class representing the recipient.
	 * - `date`: a string representing the creation date of the message.
	 */
	public static Message getMessageById(String id) {
		SqlResult rows = MySql.getInstance().get(MyDBInfo.MESSAGES_TABLE, "id = " + id);
		HashMap<String, String> msg = rows.get(0);
		
		User from = User.getUserById(msg.get("from_id"));
		User to = User.getUserById(msg.get("to_id"));
		boolean isRead = msg.get("is_read").equals("1") ? true : false;
		String date = msg.get("created_at");
		
		return new Message(msg.get("id"), msg.get("text"), msg.get("type"), isRead, from, to, date);
	}
	
	/**
	 * Marks a message as read by setting `isRead` to true and updates the database with
	 * the new status based on the message's unique `id`.
	 */
	public void markRead() {
		this.isRead = true;
		sql.update(MyDBInfo.MESSAGES_TABLE, "is_read=1", "id=" + this.id);
	}
	
	/**
	 * Returns a string value representing an identifier, presumably assigned to an object.
	 * The value is directly retrieved from an object's `id` field.
	 *
	 * @returns the value of the `id` variable.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the value of the `type` variable as a string. The function does not modify
	 * the `type` variable and is read-only. It is a getter method.
	 *
	 * @returns the value of the `type` variable, which is a string.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Returns the value of the `msg` variable as a string.
	 *
	 * @returns the value of the `msg` variable.
	 */
	public String getMsg() {
		return msg;
	}
	
	/**
	 * Truncates a message to 30 characters if its length exceeds 30, appending "..." to
	 * indicate truncation, otherwise returns the message in its entirety.
	 *
	 * @returns Either the entire message or a truncated version with an ellipsis at the
	 * end.
	 */
	public String getPreviewMsg() {
		if (this.msg.length() > 30) return this.msg.substring(0, 30) + "...";
		return this.msg;
	}
	
	/**
	 * Returns the `from` object, which is presumably a User instance. The function does
	 * not modify the object but simply retrieves and returns its current state. It is
	 * likely a getter method in the context of a larger application.
	 *
	 * @returns an instance of the User class stored in the 'from' variable.
	 */
	public User getFromUser() {
		return from;
	}
	
	/**
	 * Returns the `to` object, which is presumably a User instance. This suggests a
	 * getter method for accessing the recipient of a message or action. The method name
	 * and return type imply a simple accessor function.
	 *
	 * @returns an instance of the `User` class, specifically the `to` object.
	 */
	public User getToUser() {
		return to;
	}
	
	/**
	 * Returns the value of the `isRead` variable.
	 *
	 * @returns a boolean value indicating whether the document has been read.
	 */
	public boolean isRead() {
		return isRead;
	}
	
	/**
	 * Formats a date object into a string using the specified DATE_FORMAT. The resulting
	 * string is returned as the function's output. The function uses a SimpleDateFormat
	 * instance to perform the date formatting.
	 *
	 * @returns a string representing the date in the format specified by the `DATE_FORMAT`
	 * constant.
	 */
	public String getDate() {
		return new SimpleDateFormat(DATE_FORMAT).format(this.date);
	}
}
