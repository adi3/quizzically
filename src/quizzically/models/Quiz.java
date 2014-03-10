package quizzically.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import quizzically.config.MyDBInfo;
import quizzically.lib.MySql;
import quizzically.lib.SqlResult;

public class Quiz {
	private static final String[] QUIZ_QUESTIONS_COLUMNS = new String[]{"quiz_id", "question_id", "position"};
	
	private int id;
	private String name;
	private int owner_id;
	private User owner;
	private List<Question> questions;
	
	private Quiz(int id, String name, int owner_id, List<Question> questions) {
		this.id = id;
		this.name = name;
		this.owner_id = owner_id;
		this.questions = questions;
	}

	public static Quiz create(String name, int ownerID){
		MySql sql = MySql.getInstance();
		
		return null;
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
			List<Question> questions = Question.retrieveByQuizID(id);
			return new Quiz(quizID, name, ownerID, questions);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Adds the question to the questions list,
	 * and inserts the quiz-question relation
	 * @param question
	 */
	// TODO if question_id is already owned by quiz ignore
	// Note that when addQuestion is called all questions in the 
	// DB are also in the List of the Quiz object
	public void addQuestion(Question question, int position){
		if(! questions.contains(question)){ // id comparison
			questions.add(question);
			MySql sql = MySql.getInstance();
			String[] values = {Integer.toString(id), Integer.toString(question.id()), Integer.toString(position)};
			sql.insert(MyDBInfo.QUIZ_QUESTIONS_TABLE, QUIZ_QUESTIONS_COLUMNS, values);
		}
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
