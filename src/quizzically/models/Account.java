package quizzically.models;

import static org.junit.Assert.assertTrue;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import quizzically.config.MyDBInfo;
import quizzically.lib.MySql;
import quizzically.lib.SqlResult;

// TODO this looks like it needs to be static
public class Account {
	
	private MySql sql;
	
	public Account() {
		sql = MySql.getInstance();
	}
	
	public static boolean accountExists(String username) {
		SqlResult rows = MySql.getInstance().get("users", "username = '" + username + "'");
		return rows.size() > 0;
	}
	
	public boolean checkCredentials(String username, String password) {
		if (!this.accountExists(username)) return false;
		String[] cols = {"password, salt"};
		SqlResult user = sql.get(cols, MyDBInfo.USERS_TABLE, "username = '" + username + "'");
		
		String salted_pass = getSaltedPass(password, user.get(0).get("salt"));
		return salted_pass.equals(user.get(0).get("password"));
	}
	
	public ArrayList<String> createAccount(String name, String email, String username, String password, boolean isAdmin) {
		ArrayList<String> errors = validateInput(name, email, username);
		if (this.accountExists(username)) errors.add("The username already exists. Please choose a different one.");
		if (!this.isStrongPass(password)) errors.add("Please ensure your password meets our strength requirements.");
		if (errors.size() != 0) return errors;
		
		String salt = Integer.toString(getSalt());
		String salted_pass = getSaltedPass(password, salt);
		if (salted_pass == null) errors.add("Problem saving registration. Please try a different password.");
		if (errors.size() != 0) return errors;
		
		String admin = isAdmin ? "1" : "0";
		String[] cols = {"name", "email", "is_admin", "username", "password", "salt"};
		String[] vals = {capitalize(name), email, admin, username, salted_pass, salt};
		
		int id = sql.insert(MyDBInfo.USERS_TABLE, cols, vals);
		if (id == 0) errors.add("Trouble saving registration. Please try again.");
		return errors;
	}
	
	public ArrayList<String> updateAccount(String usernameOld, String name, String email, String username) {
		ArrayList<String> errors = validateInput(name, email, username);
		if (errors.size() != 0) return errors;
		
		int status = sql.update("users", "name = '" + name + "', "
										+ "email = '" + email + "', "
										+ "username = '" + username + "' ",
										"username = '" + usernameOld + "'");
		if (status == 0) errors.add("Trouble updating profile. Please try again.");
		return errors;
	}
	
	public ArrayList<String> updatePassword(String username, String pass, String passConf) {
		ArrayList<String> errors = new ArrayList<String>();
		if (!pass.equals(passConf)) errors.add("Please ensure both password entries match.");
		if (!this.isStrongPass(pass)) errors.add("Please ensure your password meets our strength requirements.");
		if (errors.size() != 0) return errors;
		
		String salt = Integer.toString(getSalt());
		String salted_pass = getSaltedPass(pass, salt);
		if (salted_pass == null) errors.add("Problem saving registration. Please try a different password.");
		if (errors.size() != 0) return errors;
		
		int status = sql.update("users", "password = '" + salted_pass + "', "
										+ "salt = '" + salt + "' ",
										"username = '" + username + "'");
		if (status == 0) errors.add("Trouble updating profile. Please try again.");
		return errors;
	}
	
	private ArrayList<String> validateInput(String name, String email, String username) {
		ArrayList<String> errors = new ArrayList<String>();
		if (name.isEmpty()) errors.add("You must provide a name for registration.");
		if (email.isEmpty()) errors.add("You must provide an email for registration.");
		if (!this.isValidEmail(email)) errors.add("You must enter a valid email address.");
		return errors;
	}
	
	// valid password has one digit, one letter and one special character.
	// no whitespaces allowed. length must be at least 6
	private boolean isStrongPass(String password) {
		return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&+=])(?=\\S+$).{6,}$");
	}
	
	public boolean isValidEmail(String email) {
		return email.matches("^[_A-Za-z0-9-\\+]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
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
	
	private String capitalize(String str) {
		char[] chars = str.toLowerCase().toCharArray();
		boolean found = false;
		for (int i = 0; i < chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				chars[i] = Character.toUpperCase(chars[i]);
				found = true;
			} else if (Character.isWhitespace(chars[i])) found = false;
		}
		return String.valueOf(chars);
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
