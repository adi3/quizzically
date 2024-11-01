package quizzically.models;

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
/**
 * Provides methods for managing user accounts, including checking for existing
 * accounts, validating credentials, creating new accounts, updating account information,
 * and updating passwords.
 */
public class Account {
	
	private MySql sql;
	
	public Account() {
		sql = MySql.getInstance();
	}
	
	/**
	 * Checks if a user account exists in the database by searching for a matching username.
	 * It queries the "users" table in the MySQL database and returns true if a matching
	 * username is found, false otherwise.
	 *
	 * @param username username to be searched for in the database.
	 *
	 * @returns a boolean value indicating whether an account with the given username exists.
	 */
	public static boolean accountExists(String username) {
		SqlResult rows = MySql.getInstance().get("users", "username = '" + username + "'");
		return rows.size() > 0;
	}
	
	/**
	 * Verifies user credentials by checking if the provided username and password match
	 * those stored in the database. It first checks if the account exists, then retrieves
	 * the hashed password and salt from the database, and finally compares the hashed
	 * password with the provided password after applying the salt.
	 *
	 * @param username username to be checked for existence in the database and is used
	 * to retrieve the associated password and salt.
	 *
	 * @param password password to be checked for matching with the stored password in
	 * the database.
	 *
	 * @returns a boolean value indicating whether the provided credentials are valid.
	 */
	public boolean checkCredentials(String username, String password) {
		if (!this.accountExists(username)) return false;
		String[] cols = {"password, salt"};
		SqlResult user = sql.get(cols, MyDBInfo.USERS_TABLE, "username = '" + username + "'");
		
		String salted_pass = getSaltedPass(password, user.get(0).get("salt"));
		return salted_pass.equals(user.get(0).get("password"));
	}
	
	/**
	 * Validates user input, checks for existing usernames, ensures password strength,
	 * and matches password confirmations before creating a new account with a unique
	 * salted password, storing user data in a database table.
	 *
	 * @param name user's full name, which is capitalized and stored in the database when
	 * a new account is created.
	 *
	 * @param email email address of the user being created.
	 *
	 * @param username desired username for the new account, which is checked for uniqueness
	 * and used in database insertion.
	 *
	 * @param password new password for the account being created, which is then hashed
	 * and stored in the database.
	 *
	 * @param passConf confirmation of the user's password entry.
	 *
	 * @param isAdmin administrative access level of the newly created account, with a
	 * value of `true` indicating an administrator and `false` indicating a standard user.
	 *
	 * @returns a list of error messages or empty if account creation is successful.
	 *
	 * The output is an `ArrayList` of `String` objects. It contains error messages if
	 * any validation or database operations fail. Otherwise, it is empty.
	 */
	public ArrayList<String> createAccount(String name, String email, String username, String password, String passConf, boolean isAdmin) {
		ArrayList<String> errors = validateInput(name, email, username);
		if (this.accountExists(username)) errors.add("The username already exists. Please choose a different one.");
		if (!password.equals(passConf)) errors.add("Please ensure both password entries match.");
		if (!this.isStrongPass(password)) errors.add("Please ensure your password meets our strength requirements.");
		if (errors.size() != 0) return errors;
		
		String salt = Integer.toString(getSalt());
		String salted_pass = getSaltedPass(password, salt);
		if (salted_pass == null) errors.add("Problem saving registration. Please try a different password.");
		if (errors.size() != 0) return errors;
		
		String admin = isAdmin ? "1" : "0";
		String img = "m" + Integer.toString(new Random().nextInt(24)) + ".png";
		String[] cols = {"name", "email", "img", "location", "is_admin", "username", "password", "salt"};
		String[] vals = {capitalize(name), email, img, "", admin, username, salted_pass, salt};
		
		int id = sql.insert(MyDBInfo.USERS_TABLE, cols, vals);
		if (id == 0) errors.add("Trouble saving registration. Please try again.");
		return errors;
	}
	
	/**
	 * Updates an existing user account in the database with the provided name, email,
	 * username, and location. Validation is performed on the input data, and any errors
	 * are returned. If the update fails, an error message is added to the result list.
	 *
	 * @param usernameOld old username of the account being updated.
	 *
	 * @param name new name to be updated in the user's account.
	 *
	 * @param email new email address to be updated in the user's account.
	 *
	 * @param username new username for the account.
	 *
	 * @param loc new location of the user being updated, which is used in the SQL update
	 * query.
	 *
	 * @returns an ArrayList of error messages or an empty list if the update is successful.
	 *
	 * The returned output is an `ArrayList` of `String` objects, representing a collection
	 * of error messages. The list is empty if the update operation is successful, otherwise
	 * it contains one or more error messages, including a generic message indicating
	 * trouble updating the profile.
	 */
	public ArrayList<String> updateAccount(String usernameOld, String name, String email, String username, String loc) {
		ArrayList<String> errors = validateInput(name, email, username);
		if (errors.size() != 0) return errors;
		
		int status = sql.update("users", "name = '" + name + "', "
										+ "email = '" + email + "', "
										+ "username = '" + username + "', "
										+ "location = '" + loc + "'",
										"username = '" + usernameOld + "'");
		if (status == 0) errors.add("Trouble updating profile. Please try again.");
		return errors;
	}
	
	/**
	 * Verifies password strength and ensures password confirmation matches. It generates
	 * a salted password and updates the user's password in the database if the password
	 * is valid.
	 *
	 * @param username identifier for the user account being updated, used to locate the
	 * user in the database.
	 *
	 * @param pass new password being entered by the user and is used to check its strength
	 * and to create a salted password for storage.
	 *
	 * @param passConf confirmation of the new password entered by the user.
	 *
	 * @returns a list of error messages as an `ArrayList` of `String` objects.
	 *
	 * It is a list of strings representing error messages.
	 */
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
	
	/**
	 * Checks the input fields of name, email, and username for empty values and invalid
	 * email addresses, storing any errors found in an ArrayList.
	 *
	 * @param name user's name that is being validated for registration.
	 *
	 * @param email user-provided email address to be validated during registration.
	 *
	 * @param username an input value for registration, but it is not used within the function.
	 *
	 * @returns a list of error messages as strings, possibly empty if input is valid.
	 */
	private ArrayList<String> validateInput(String name, String email, String username) {
		ArrayList<String> errors = new ArrayList<String>();
		if (name.isEmpty()) errors.add("You must provide a name for registration.");
		if (email.isEmpty()) errors.add("You must provide an email for registration.");
		if (!this.isValidEmail(email)) errors.add("You must enter a valid email address.");
		return errors;
	}
	
	// valid password has one digit, one letter and one special character.
	// no whitespaces allowed. length must be at least 6
	/**
	 * Checks if a password meets certain security criteria, including a minimum length
	 * of 6 characters, presence of at least one digit, one lowercase letter, and one
	 * special character.
	 *
	 * @param password password string to be validated for strength.
	 *
	 * @returns a boolean value indicating whether the password meets the specified
	 * strength criteria.
	 */
	private boolean isStrongPass(String password) {
		return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&+=])(?=\\S+$).{6,}$");
	}
	
	/**
	 * Checks if the input `email` matches a regular expression pattern for a valid email
	 * address, returning `true` if it does and `false` otherwise. The pattern allows
	 * alphanumeric characters, underscores, and hyphens in the local part, followed by
	 * a domain name with optional subdomains.
	 *
	 * @param email string to be validated as a valid email address.
	 *
	 * @returns a boolean indicating whether the input email matches the specified pattern.
	 */
	public boolean isValidEmail(String email) {
		return email.matches("^[_A-Za-z0-9-\\+]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	}
	
	/**
	 * Generates a random integer.
	 * It uses the `nextInt` method of the `Random` class to produce a pseudo-random number.
	 * The generated number is returned as an integer.
	 *
	 * @returns a random integer within the range of `Integer.MIN_VALUE` to `Integer.MAX_VALUE`.
	 */
	private int getSalt() {
		return new Random().nextInt();
	}
	
	/**
	 * Concatenates a password with a salt, updates it with the MD5 hash algorithm, and
	 * returns the result in hexadecimal format.
	 *
	 * @param password password to be hashed and is concatenated with the `salt` parameter
	 * before being processed by the MessageDigest algorithm.
	 *
	 * @param salt a random value concatenated with the `password` to prevent dictionary
	 * attacks and ensure a unique hash for each password.
	 *
	 * @returns a hexadecimal string representation of the MD5 digest of the salted password.
	 */
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
	
	/**
	 * Converts a given string to title case by capitalizing the first letter and leaving
	 * the rest of the string in lowercase, while preserving the original case of the
	 * first letter if it is already uppercase.
	 *
	 * @param str string to be capitalized, with the first letter converted to uppercase
	 * and the rest in lowercase.
	 *
	 * @returns a string with the first letter capitalized and the rest in lowercase.
	 *
	 * The output is a string.
	 */
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
	/**
	 * Converts a byte array into a hexadecimal string representation. It iterates over
	 * each byte, removes higher bits, appends leading zeros if necessary, and concatenates
	 * the hexadecimal values. The result is returned as a string.
	 *
	 * @param bytes byte array to be converted from hexadecimal to a string representation.
	 *
	 * @returns a hexadecimal string representation of the input byte array.
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
