package quizzically.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import quizzically.lib.MySQL;

public class Account {
	
	private MySQL sql;
	private static final String TABLE = "users";
	
	public Account() {
		sql = new MySQL();
	}
	
	public boolean accountExists(String username) {
		ResultSet set = sql.get("users", "username = '" + username + "'");
		
		try {
			set.last();
			return set.getRow() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean checkCredentials(String username, String password) {
		if (!this.accountExists(username)) return false;
		String[] cols = {"password, salt"};
		ResultSet user = sql.get(cols, TABLE, "username = '" + username + "'");
		try {
			user.first();
			String salted_pass = getSaltedPass(password, user.getString(2));
			return salted_pass.equals(user.getString(1));			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public ArrayList<String> createAccount(String name, String email, String username, String password) {
		ArrayList<String> errors = validateInput(name, email, username, password);
		if (errors.size() != 0) return errors;
		
		String salt = Integer.toString(getSalt());
		String salted_pass = getSaltedPass(password, salt);
		if (salted_pass == null) errors.add("Problem saving registration. Please try a different password.");
		if (errors.size() != 0) return errors;
		
		String[] vals = {name, email, "0", username, salted_pass, salt};
		int status = sql.insert(TABLE, vals);
		if (status == 0) errors.add("Trouble saving registration. Please try again.");
		return errors;
	}
	
	private ArrayList<String> validateInput(String name, String email, String username, String password) {
		ArrayList<String> errors = new ArrayList<String>();
		if (name.isEmpty()) errors.add("You must provide a name for registration.");
		if (email.isEmpty()) errors.add("You must provide an email for registration.");
		if (this.accountExists(username)) errors.add("The username already exists. Please choose a different one.");
		if (!this.isStrongPass(password)) errors.add("Please ensure your password meets our strength requirements.");
		return errors;
	}
	
	// valid password has one digit, one letter and one special character.
	// no whitespaces allowed. length must be at least 6
	public boolean isStrongPass(String password) {
		return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$");
	}
	
	private int getSalt() {
		return new Random().nextInt();
	}
	
	private String getSaltedPass(String password, String salt) {
		String salted = password + salt;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(salted.getBytes());
			return hexToString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
}
