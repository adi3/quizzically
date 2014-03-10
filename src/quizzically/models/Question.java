package quizzically.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import quizzically.config.MyDBInfo;
import quizzically.lib.MySql;
import quizzically.lib.SqlResult;

import java.util.*;

import quizzically.exceptions.*;

public abstract class Question {
	private static final String[] QUESTIONS_COLUMNS = {"text", "type"};
	
	public static final int TYPE_TEXT = 0;
	public static final int TYPE_FILL_IN = 1;
	public static final int TYPE_MULTIPLE_CHOICE = 2;
	public static final int TYPE_PICTURE = 3;
	
	private int id;
	private String text;
	int type;
	private SortedMap<Integer, Answer> orderedAnswers;
	
	// Note: a Question object does not have an a priori position as it is not
	// associated with a specific Quiz. The QUIZ_QUESTIONS_TABLE associates quizes
	// to the questions they contain and the position of each question.
	// The static method retrieveByQuizID is aware of those positions and returns
	// the list of Questions in the correct order.

	/* XXX
	 * do not call except in subclasses
	 */
	protected Question(int id, String text, SortedMap<Integer, Answer> orderedAnswers) {
		this.orderedAnswers = orderedAnswers;
		this.id = id; 
		this.text = text;
	}

	protected Question(String text) {
		this.text = text;
	}

	/**
	 * Get the id
	 */
	public int id() {
		return id;
	}

	/**
	 * Get the raw question text
	 * @return the question text
	 */
	public String text() {
		return text;
	}

	/**
	 * The question type
	 */
	public int type() {
		return type;
	}

	/**
	 * Get the possible answers to this question
	 * ordered by their position
	 * @return the possible answers (options) for the question
	 */
	public List<Answer> answers() {
		List<Answer> answers = new ArrayList<Answer>();
		for(Answer a: orderedAnswers.values()){
			answers.add(a);
		}
		return answers;
	}
	
	/**
	 * Set of Answer positions that are occupied
	 * in the Question.
	 * @return
	 */
	public Set<Integer> occupiedPositions() {
		return orderedAnswers.keySet();
	}
	
	
	// TODO: create methods that modify a Quiz e.g. add an answer
	
	/**
	 * Creates a Question object with the given values and inserts it into the db.
	 * Performs the insertion first, retrieves the generated ID and then creates the object.
	 * Note that a Question object does not have an a priori position as it is not
	 * associated with a specific Quiz. To add the newly created Question quest to a Quiz quiz
	 * at position pos call quiz.addQuestion(question, pos)
	 * @param text
	 * @param type
	 * @return
	 */
	public static Question create(String text, int type){
		MySql sql = MySql.getInstance();
		String[] values = {text, Integer.toString(type)};
		int questionID = sql.insert(MyDBInfo.QUESTIONS_TABLE, QUESTIONS_COLUMNS, values);
		SortedMap<Integer, Answer> orderedAnswers = new TreeMap<Integer, Answer>();
		switch (type) {
			case TYPE_TEXT:
				return new TextQuestion(questionID, text, orderedAnswers);
			case TYPE_FILL_IN:
				return new FillInQuestion(questionID, text, orderedAnswers);
			case TYPE_MULTIPLE_CHOICE:
				return new MultipleChoiceQuestion(questionID, text, orderedAnswers);
			case TYPE_PICTURE:
				return new PictureQuestion(questionID, text, orderedAnswers);
			default:
				throw new RuntimeException("Requested question type does not exist.");
		}
	}
	
	/**
	 * Adds the answer to the Answer List
	 * maintained by Question.
	 * XXX only to be called by Answer.create()
	 * Read Answer.create() comments for explanation
	 * of this protocol.
	 * @param answer
	 */
	 void addAnswer(Answer answer, int position){
		// relation inserted in DB by Answer.create()
		// since Answer belongs to unique Question.
		orderedAnswers.put(position, answer);
	}
	
	
	/**
	 * Retrieve Question object from db.
	 * Throws exception on error and returns null 
	 * if no Question with the desired ID was found.
	 * @param questionID
	 * @return
	 */
	public static Question retrieveByID(int questionID) {
		MySql sql = MySql.getInstance();
		SqlResult res = sql.get(MyDBInfo.QUESTIONS_TABLE, "`id` = " + questionID);

		if (res.size() == 0) {
			return null;
		}

		HashMap<String, String> row = res.get(0);
		try {
			String text = row.get("text");
			int type = Integer.parseInt(row.get("type"));
			SortedMap<Integer, Answer> orderedAnswers = Answer.retrieveByQuestionID(questionID);
			switch (type) {
				case TYPE_TEXT:
					return new TextQuestion(questionID, text, orderedAnswers);
				case TYPE_FILL_IN:
					return new FillInQuestion(questionID, text, orderedAnswers);
				case TYPE_MULTIPLE_CHOICE:
					return new MultipleChoiceQuestion(questionID, text, orderedAnswers);
				case TYPE_PICTURE:
					return new PictureQuestion(questionID, text, orderedAnswers);
				default:
					// TODO: I think do nothing i.e. return null since type is invalid
			}
		} catch (NumberFormatException e) { /*ignore*/ }

		return null;
	}

	/**
	 * Retrieves all questions belonging to the quiz with id quizID.
	 * Returns a SortedMap keyd on the position of Questions in the Quiz.
	 * @param quizID
	 * @return
	 */
	public static SortedMap<Integer, Question> retrieveByQuizID(int quizID) {
		MySql sql = MySql.getInstance();
		SqlResult res = sql.get(MyDBInfo.QUIZ_QUESTIONS_TABLE, "`quiz_id` = " + quizID);
		// use SortedMap to order the questions
		SortedMap<Integer, Question> orderedQuestions = new TreeMap<Integer, Question>();
		for (HashMap<String, String> row : res) {
			try { 
				int questionID = Integer.parseInt(row.get("question_id"));
				int position = Integer.parseInt(row.get("position"));
				Question q = Question.retrieveByID(questionID);
				orderedQuestions.put(position, q);
			} catch (NumberFormatException e) { /* ignore */ }
		}
		return orderedQuestions;
	}
	
	
	
	/**
	 * True if obj is a Question with the same id.
	 */
	@Override
	public boolean equals(Object obj){
		if(obj == this) return true;
		if(! (obj instanceof Question)) return false;
		return id == ((Question)obj).id;
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
