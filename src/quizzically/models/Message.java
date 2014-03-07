package quizzically.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import quizzically.config.MyConfigVars;
import quizzically.config.MyDBInfo;
import quizzically.lib.MySQL;

public class Message {

	private String id;
	private String msg;
	private User from;
	private User to;
	private String type;		// 1: REQUEST, 2: CHALLENGE, 4: NOTE
	private boolean isRead;
	private Date date;
	
	private MySQL sql;
	private static final String DATE_FORMAT = "dd-MMM-yy hh:mm a";
	
	public Message(String msg, String type, User from, User to) {
		sql = MySQL.getInstance();
		
		this.msg = msg;
		this.from = from;
		this.to = to;
		this.type = type;
		
		this.isRead = false;
		this.date = new Date();
	}
	
	public Message(String id, String msg, String type, boolean isRead, User from, User to, String date) {
		sql = MySQL.getInstance();
		
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
	
	public boolean save() {
		String[] cols = {"text", "from_id", "to_id", "type", "is_read", "created_at"};
		String isRead = this.isRead ? "1" : "0";
		String[] vals = {this.msg, this.from.getId(), this.to.getId(), this.type, isRead, this.getDate()};
		
		ResultSet genKeys = sql.insert(MyDBInfo.MESSAGES_TABLE, cols, vals);
		if(genKeys == null) return false;
		try{
			return genKeys.first(); // true when insertion succeeded
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static ArrayList<Message> getMessages(User user) {
		ArrayList<Message> msgs = new ArrayList<Message>();
		ResultSet set = MySQL.getInstance().get(MyDBInfo.MESSAGES_TABLE, "to_id = '" + user.getId() + "' ORDER BY created_at DESC");
		
		try {
			while(set.next()) {
				User from = User.getUserById(set.getString("from_id"));
				User to = User.getUserById(set.getString("to_id"));
				boolean isRead = set.getString("is_read").equals("1") ? true : false;
				String date = set.getString("created_at");
				msgs.add(new Message(set.getString("id"), set.getString("text"), set.getString("type"), isRead, from, to, date));
			}
			return msgs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	public static Message getMessageById(String id) {
		ResultSet set = MySQL.getInstance().get(MyDBInfo.MESSAGES_TABLE, "id = " + id);
		try {
			set.first();
			User from = User.getUserById(set.getString("from_id"));
			User to = User.getUserById(set.getString("to_id"));
			boolean isRead = set.getString("is_read").equals("1") ? true : false;
			String date = set.getString("created_at");
			return new Message(set.getString("id"), set.getString("text"), set.getString("type"), isRead, from, to, date);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void markRead() {
		this.isRead = true;
		sql.update(MyDBInfo.MESSAGES_TABLE, "is_read=1", "id=" + this.id);
	}
	
	public String getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public String getPreviewMsg() {
		if (this.msg.length() > 25) return this.msg.substring(0, 25) + "...";
		return this.msg;
	}
	
	public User getFromUser() {
		return from;
	}
	
	public User getToUser() {
		return to;
	}
	
	public boolean isRead() {
		return isRead;
	}
	
	public String getDate() {
		return new SimpleDateFormat(DATE_FORMAT).format(this.date);
	}
}
