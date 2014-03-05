package quizzically.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import quizzically.lib.MySQL;

public class Quiz {
	private int id;
	private String name;
	private int owner_id;
	private User owner;

	private static final String TABLE = "quizzes";

	/**
	 * Get the quiz with the given id or null if it doesn't exist
	 */
	public static Quiz retrieve(int id) {
		MySQL sql = MySQL.getInstance();
		ResultSet rs = sql.get(TABLE, "\"id\" = " + id);

		String name;
		int owner_id;

		try {
			rs.first();
			name = rs.getString("name");
			owner_id = rs.getInt("owner_id");
			return new Quiz(name, owner_id);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Quiz(String name, int owner_id) {
		this.name = name;
		this.owner_id = owner_id;
		// TODO instantiate owner lazily? so lazyyyy ~_~
	}

	public ArrayList<Question> questions() {
		return Question.retrieveByQuizID(this.id); // TODO ensure this only happens once?
	}
}
