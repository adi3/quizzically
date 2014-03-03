package quizzically.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import quizzically.lib.MySQL;

public class User {

	private int id;
	private String name;
	private String email;
	private String username;
	private boolean isAdmin;
	
	private MySQL sql;
	
	
	public User() {
		sql = new MySQL();
	}
	
	
}
