package quizzically.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

import quizzically.lib.MySQL;

public abstract class Question {
	private int id;
	private String text;
	int type;
	private ArrayList<Answer> answers;

	private static final String TABLE = "questions"; // TODO put value in MyDBInfo

	protected static int TYPE_TEXT = 0;
	protected static int TYPE_FILL_IN = 1;
	protected static int TYPE_MULTIPLE_CHOICE = 2;
	protected static int TYPE_PICTURE = 3;

	public Question(String text, int type) {
		this.text = text;
		this.type = type;
	}

	/* XXX
	 * do not call except in subclasses
	 */
	protected Question(String text) {
		this.text = text;
	}

	public static Question retrieve(int id) {
		// TODO fetch from db
	}

	/**
	 * Return the question or null if failed
	 */
	public static Question fromResultSet(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String text = rs.getString("text");
		int type = rs.getInt("type");

		switch (type) {
			case TYPE_TEXT:
				return new TextQuestion(text);
			case TYPE_FILL_IN:
				return new FillInQuestion(text);
			case TYPE_MULTIPLE_CHOICE:
				return new MultipleChoiceQuestion(text);
			case TYPE_PICTURE:
				return new PictureQuestion(text);
			default:
				// TODO
		}

		// TODO set id!

		return null;
	}

	public static ArrayList<Question> retrieveByQuizID(int id) {
		MySQL sql = MySQL.getInstance();
		ResultSet rs = sql.get(TABLE, "\"id\" = " + id);
		ArrayList<Question> results = new ArrayList<Question>();

		try {
			while (rs.next()) {
				Question q = Question.fromResultSet(rs);
				results.add(q);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO is it ok to silently ignore this?
		}

		return results;
	}

	/**
	 * Get the raw question text
	 * @return the question text
	 */
	public String text() {
		return text;
	}

	/**
	 * Get the possible answers to this question
	 * @return the possible answers (options) for the question
	 */
	public ArrayList<Answer> answers() {
		return answers;
	}

	/**
	 * Grade the given user answer(s)
	 * @param answers the answers provided by the user
	 * @return the grade on the question
	 */
	public abstract int grade(Set<Response> responses);

	/**
	 * Get the highest possible grade
	 * @return the highest possible grade value 
	 * for the question
	 */
	public abstract int possiblePoints();
}
