package quizzically.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import quizzically.config.MyDBInfo;
import quizzically.lib.MySql;
import quizzically.lib.SqlResult;

public class Quiz {
	private static final String[] QUIZZES_COLUMNS = new String[]{"name", "owner_id"};
	private static final String[] QUIZ_QUESTIONS_COLUMNS = new String[]{"quiz_id", "question_id", "position"};
	
	private int id;
	private String name;
	private int owner_id;
	private User owner;
	private SortedMap<Integer, Question> orderedQuestions;
	
	private Quiz(int id, String name, int owner_id, SortedMap<Integer, Question> orderedQuestions) {
		this.id = id;
		this.name = name;
		this.owner_id = owner_id;
		this.orderedQuestions = orderedQuestions;
	}

	public static Quiz create(String name, int ownerID){
		MySql sql = MySql.getInstance();
		String[] values = {name, Integer.toString(ownerID)};
		int insertionID = sql.insert(MyDBInfo.QUIZZES_TABLE, QUIZZES_COLUMNS, values);
		return new Quiz(insertionID, name, ownerID, new TreeMap<Integer, Question>());
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
			SortedMap<Integer, Question> orderedQuestions = Question.retrieveByQuizID(id);
			return new Quiz(quizID, name, ownerID, orderedQuestions);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Adds the question to the questions list, and inserts the quiz-question relation
	 * into the QUIZ_QUESTIONS_TABLE. question can have either been just created or
	 * it can belong to other Quizzes.
	 * If question is already contained in the quiz or if position is taken
	 * the method does nothing.
	 * @param question
	 */
	public void addQuestion(Question question, int position){
		if(orderedQuestions.containsKey(position)){
			// TODO: Replace existing Question at position with provided question
			return;
		}
		// Note that when addQuestion is called all questions in the 
		// DB are also in the SortedMap of the Quiz object
		if(! orderedQuestions.containsValue(question)){ // id comparison
			orderedQuestions.put(position, question);
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

	/**
	 * Return owned Questions sorted by position
	 * @return
	 */
	public List<Question> questions() {
		List<Question> questions = new ArrayList<Question>();
		for(Question q: orderedQuestions.values()){
			questions.add(q);
		}
		return questions;
	}
}
