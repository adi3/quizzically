package quizzically.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.*;
import quizzically.config.MyDBInfo;

import quizzically.lib.MySQL;

public class Quiz {
	private int id;
	private String name;
	private int owner_id;
	private User owner;
	private Collection<Question> questions;
	
	public Quiz(int id, String name, int owner_id) {
		this.id = id;
		this.name = name;
		this.owner_id = owner_id;
		// TODO instantiate owner lazily? so lazyyyy ~_~
		questions = Question.retrieveByQuizID(id);
	}

	/**
	 * Get the quiz with the given id or null if it doesn't exist
	 */
	public static Quiz retrieve(int id) {
		MySQL sql = MySQL.getInstance();
		ResultSet rs = sql.get(MyDBInfo.QUESTIONS_TABLE, "\"id\" = " + id);
		if(rs == null){
			throw new RuntimeException("Error retrieving quiz.");
		}
		String name;
		int quizID;
		int ownerID;
		try {
			if(rs.first()){ // if id not found return null
				name = rs.getString("name");
				quizID = rs.getInt("id");
				ownerID = rs.getInt("owner_id");
				return new Quiz(quizID, name, ownerID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Collection<Question> questions() {
		return questions;
	}
}
