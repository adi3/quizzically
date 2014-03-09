package quizzically.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import quizzically.config.MyDBInfo;
import quizzically.lib.MySQL;
import java.util.*;

import quizzically.exceptions.*;

public abstract class Question {
	protected static final int TYPE_TEXT = 0;
	protected static final int TYPE_FILL_IN = 1;
	protected static final int TYPE_MULTIPLE_CHOICE = 2;
	protected static final int TYPE_PICTURE = 3;
	
	private int id;
	private String text;
	int type;
	// TODO: ArrayList or Collection?
	private ArrayList<Answer> answers;
	
	// Note: a Question object does not have an a priori position as it is not
	// associated with a specific Quiz. The QUIZ_QUESTIONS_TABLE associates quizes
	// to the questions they contain and the position of each question.
	// The static method retrieveByQuizID is aware of those positions and returns
	// the list of Questions in the correct order.

	// TODO: why public, and why don't you allow id parameter?
	// I think clients should use the static create instead
	public Question(String text, int type) {
		this.text = text;
		this.type = type;
	}

	/* XXX
	 * do not call except in subclasses
	 */
	protected Question(int id, String text) {
		this.id = id; // added
		this.text = text;
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
	
	
	// TODO: create methods that modify a Quiz e.g. add an answer
	
	/**
	 * Creates a Question object with the given values and inserts it into the db.
	 * Performs the insertion first, retrieves the generated ID and then creates the object.
	 * Throws a RuntimeException if there is an error or if the insertion fails.
	 * @param text
	 * @param type
	 * @return
	 */
	public static Question create(String text, int type){
		// TODO: pass info about answers so that Answer.create() can be called
		// and the Answer objects are retrieved
		
		return null;
	}
	
	// TODO: Allow retrieval of owned Answers as well
	/**
	 * Retrieve Question object from db.
	 * Throws exception on error and returns null 
	 * if no Question with the desired ID was found.
	 * @param questionID
	 * @return
	 */
	public static Question retrieveByID(int questionID) {
		MySQL sql = MySQL.getInstance();
		ResultSet rs = sql.get(MyDBInfo.QUESTIONS_TABLE, "\"id\" = " + questionID);
		if(rs == null){
			throw new RuntimeException("Retrieval failed");
		}
		try{
			if(rs.first()){
				String text = rs.getString("text");
				int type = rs.getInt("type");
				switch (type) {
					case TYPE_TEXT:
						return new TextQuestion(questionID, text);
					case TYPE_FILL_IN:
						return new FillInQuestion(questionID, text);
					case TYPE_MULTIPLE_CHOICE:
						return new MultipleChoiceQuestion(questionID, text);
					case TYPE_PICTURE:
						return new PictureQuestion(questionID, text);
					default:
						// TODO: I think do nothing i.e. return null since type is invalid
				}
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	protected Question(String text) {
		this.text = text;
	}

	/**
	 * Retrieves all questions belonging to the quiz with id quizID.
	 * Returns them ordered by their position in the quiz.
	 * @param quizID
	 * @return
	 */
	public static Collection<Question> retrieveByQuizID(int quizID) {
		MySQL sql = MySQL.getInstance();
		ResultSet rs = sql.get(MyDBInfo.QUIZ_QUESTIONS_TABLE, "\"quiz_id\" = " + quizID);
		if(rs == null){
			throw new RuntimeException("Retrieval failed");
		}
		try {
			// use SortedMap instead of Array/ArrayList since we don't know
			// the size in advance (asking rs for the size is equivalent to looping
			// through all of the ResultSet)
			SortedMap<Integer, Question> orderedQuestions = new TreeMap<Integer, Question>();
			while (rs.next()) {
				int questionID = rs.getInt("question_id");
				int position = rs.getInt("position");
				Question q = Question.retrieveByID(questionID);
				orderedQuestions.put(position, q);
			}
			return orderedQuestions.values();
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO is it ok to silently ignore this?
		}

		return new HashSet<Question>(); // TODO is this reasonable style given we want to return an empty Collection?
	}

	/**
	 * Grade the given user answer(s)
	 * @param answers the answers provided by the user
	 * @return the grade on the question
	 */
	public abstract int grade(List<Response> responses)
		throws InvalidResponseException;

	/**
	 * Get the highest possible grade
	 * @return the highest possible grade value 
	 * for the question
	 */
	public abstract int possiblePoints();
}
