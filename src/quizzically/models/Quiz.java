package quizzically.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.*;
import quizzically.config.MyDBInfo;

import quizzically.lib.MySql;
import quizzically.lib.SqlResult;

public class Quiz {
	private int id;
	private String name;
	private int owner_id;
	private User owner;
	private List<Question> questions;
	
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
		MySql sql = MySql.getInstance();
		SqlResult res = sql.get(MyDBInfo.QUIZZES_TABLE, "`id` = " + id);
		String name;
		int quizID;
		int ownerID;

		if (res.size() == 0) {
			return null;
		}

		HashMap<String, String> row = res.get(0);
		try { 
			name = row.get("name");
			quizID = Integer.parseInt(row.get("id"));
			ownerID = Integer.parseInt(row.get("creator_id"));
			return new Quiz(quizID, name, ownerID);
		} catch (NumberFormatException e) { /* ignore */ }

		return null;
	}

	public int id() {
		return id;
	}

	public String name() {
		return name;
	}

	public List<Question> questions() {
		return questions;
	}
}
