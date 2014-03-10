package quizzically.models;

import quizzically.config.MyDBInfo;
import quizzically.lib.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Answer {
	private static final String[] ANSWERS_COLUMNS = new String[]{"question_id", "position", "correct"};
	private static final String[] ANSWER_TEXTS_COLUMNS = new String[]{"answer_id", "text"};
	
	private int ID;
	private int questionID; // ID of question to which this answer belongs
	private int position; // position of answer in question, -1 if not applicable
	private boolean correct;
	private Set<String> answerTexts; // set of Strings which correspond to this answer, e.g. {FloMo, Florence Moore}

	/*
	 * should only be called by create and retrieve
	 */
	private Answer(int ID, int questionID, int position, boolean correct, Set<String> answerTexts) {
		this.ID = ID;
		this.correct = correct;
		this.questionID = questionID;
		this.position = position;
		this.answerTexts = answerTexts;
	}
	
	public int id() {
		return ID;
	}
	
	public boolean correct() {
		return correct;
	}
	
	public int questionID() {
		return questionID;
	}
	
	public int position() {
		return position;
	}
	
	public Set<String> answerTexts() {
		return answerTexts;
	}

	/**
	 * Get a single answer text
	 */
	public String text() {
		for (String s : answerTexts()) {
			return s;
		}
		return ""; // shouldn't occur
	}
	
	/**
	 * Creates an Answer object with the given values and inserts it into the db.
	 * Performs the insertion first, retrieves the generated ID and then creates the object.
	 * Throws a RuntimeException if there is an error or if the insertion fails.
	 * @param questionID
	 * @param position
	 * @param correct
	 * @param answerTexts
	 * @return newly created Answer object
	 */
	public static Answer create(int questionID, int position, boolean correct, Set<String> answerTexts) {
		MySql sql = MySql.getInstance();
		// tuple to be inserted in ANSWERS_TABLE
		String[] answerValues = {Integer.toString(questionID), Integer.toString(position), correct ? "1" : "0"};
		int insertionID = sql.insert(MyDBInfo.ANSWERS_TABLE, ANSWERS_COLUMNS, answerValues);
		for(String answerText: answerTexts){
			// tuple to be inserted in ANSWER_TEXTS_TABLE
			String[] textValues = new String[]{Integer.toString(insertionID), answerText};
			sql.insert(MyDBInfo.ANSWER_TEXTS_TABLE, ANSWER_TEXTS_COLUMNS, textValues);
		}
		return new Answer(insertionID, questionID, position, correct, answerTexts);
	}
	
	/**
	 * Retrieves the Answer object stored in the db with the given ID.
	 * Throws a RuntimeException on retrieval error.
	 * Returns null if no tuple is found in the ANSWERS_TABLE with the given ID.
	 * @param ID
	 * @return retrieved Answer object
	 */
	public static Answer retrieveByID(int answerID){
		MySql sql = MySql.getInstance();
		SqlResult answerResult = sql.get(MyDBInfo.ANSWERS_TABLE, "`id`="+answerID);
		SqlResult answerTextsResult = sql.get(MyDBInfo.ANSWER_TEXTS_TABLE, "`answer_id`="+answerID);
		if(answerResult.size() == 0){ // answerID not found
			return null;
		}
		int qID;
		int pos;
		boolean corr;
		try{
			qID = Integer.parseInt(answerResult.get(0).get("question_id"));
			pos = Integer.parseInt(answerResult.get(0).get("position"));
			corr = Integer.parseInt(answerResult.get(0).get("correct")) == 1;
		} catch(NumberFormatException e){
			return null;
		}
		Set<String> answerTexts = new HashSet<String>();
		for(int i=0; i<answerTextsResult.size(); i++){
			answerTexts.add(answerTextsResult.get(i).get("text"));
		}
		return new Answer(answerID, qID, pos, corr, answerTexts);		
	}
	
	/**
	 * Retrieves all answers belonging to the question whose ID is questionID.
	 * If no answers were found returns an empty set.
	 * Throws a RuntimeException on retrieval error.
	 * @param questionID
	 * @return answerSet
	 */
	public static List<Answer> retrieveByQuestionID(int questionID) {
		List<Answer> answers = new ArrayList<Answer>();
		MySql sql = MySql.getInstance();
		SqlResult answerResult = sql.get(MyDBInfo.ANSWERS_TABLE, "`question_id`="+questionID);
		if(answerResult.size() == 0){
			return answers;
		}
		
		SortedMap<Integer, Answer> orderedAnswers = new TreeMap<Integer, Answer>();
		for(int i=0; i<answerResult.size(); i++){
			try{
				int answerID = Integer.parseInt(answerResult.get(i).get("id"));
				int position = Integer.parseInt(answerResult.get(i).get("position"));
				orderedAnswers.put(position, Answer.retrieveByID(answerID));
			} catch(NumberFormatException e){
				// ignore
			}
		}
		
		for(Answer a: orderedAnswers.values()){
			answers.add(a); // put ordered by key
		}
		return answers;
	}
	
	

}
