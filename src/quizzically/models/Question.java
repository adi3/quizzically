package quizzically.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import quizzically.config.MyDBInfo;
import quizzically.lib.MySql;
import quizzically.lib.SqlResult;

import java.util.*;

import quizzically.exceptions.*;

public abstract class Question extends Model {
	private static final String TABLE = MyDBInfo.QUESTIONS_TABLE;
	private static final String[] QUESTIONS_COLUMNS = {"text", "type"};

	public static final int TYPE_TEXT = 0;
	public static final int TYPE_FILL_IN = 1;
	public static final int TYPE_MULTIPLE_CHOICE = 2;
	public static final int TYPE_PICTURE = 3;
	
	public static final int[] TYPES = {
		TYPE_TEXT, 
		TYPE_FILL_IN,
		TYPE_MULTIPLE_CHOICE,
		TYPE_PICTURE
	};

	public static final String[] TYPE_STRINGS = {
		"Question-Response",
		"Fill-in-the-Blank",
		"Multiple Choice",
		"Picture-Response"
	};

	public static String typeString(int type) {
		return TYPE_STRINGS[type];
	}
	
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
		super(id, TABLE, new QuestionHydrator());
		this.orderedAnswers = orderedAnswers;
		this.text = text;
	}


	/**
	 * Get the raw question text
	 * @return the question text
	 */
	public String text() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/**
	 * The question type
	 */
	public int type() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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
		SortedMap<Integer, Answer> orderedAnswers = new TreeMap<Integer, Answer>();
		Question question = Question.instance(-1, text, type, 
				orderedAnswers);
		question.save(true);
		return question;
	}

	protected static Question instance(int id, String text, 
			int type, SortedMap<Integer, Answer> orderedAnswers) {
		switch (type) {
			case TYPE_TEXT:
				return new TextQuestion(id, text, orderedAnswers);
			case TYPE_FILL_IN:
				return new FillInQuestion(id, text, orderedAnswers);
			case TYPE_MULTIPLE_CHOICE:
				return new MultipleChoiceQuestion(id, text, orderedAnswers);
			case TYPE_PICTURE:
				return new PictureQuestion(id, text, orderedAnswers);
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
	 * create an Answer associated with this Question
	 * @param correct whether or not the answer is correct
	 * @param texts the valid texts recognized for this answer
	 * @return the created Answer
	 */
	public Answer createAnswer(boolean correct, ArrayList<String> texts) 
			throws ModelException {
		Integer pos = orderedAnswers.size() != 0 ? orderedAnswers.lastKey() + 1 : 1;
		Answer ans = Answer.create(this, pos, correct);
		for (String text : texts) {
			ans.addAnswerText(text);
		}
		return ans;
	}
	
	
	/**
	 * Retrieve Question object from db.
	 * Throws exception on error and returns null 
	 * if no Question with the desired ID was found.
	 * TODO ^^ does it return null? i don't think so anymore
	 * 				update comment based on Model behavior...
	 * @param id
	 * @return
	 */
	public static Question retrieve(int id) {
		return (Question) 
			Model.retrieve(TABLE, id, new QuestionHydrator());
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
				Question q = Question.retrieve(questionID);
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
		return id() == ((Question)obj).id();
	}
	
	/**
	 * Grade the given user answer(s)
	 * @param answers the answers provided by the user
	 * @return the grade on the question
	 */
	protected abstract Grade grade(List<Response> responses)
		throws InvalidResponseException;

	/**
	 * Grading for the question
	 */
	public class Grade {
		private int points;
		private int possible;
		public Grade(int points, int possible) {
			this.points = points;
			this.possible = possible;
		}

		public int points() {
			return points;
		}

		public int possible() {
			return possible;
		}
	}

	public String[] cols() {
		return QUESTIONS_COLUMNS;
	}
}
