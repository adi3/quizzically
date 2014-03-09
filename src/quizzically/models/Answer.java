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
	// TODO: change signature of insert
	// TODO: create transaction
	public static Answer create(int questionID, int position, boolean correct, Set<String> answerTexts) {
		MySql sql = MySql.getInstance();
		// tuple to be inserted in ANSWERS_TABLE
		String[] answerValues = new String[]{Integer.toString(questionID), Integer.toString(position), Boolean.toString(correct)};
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
		MySQL sql = MySQL.getInstance();
		ResultSet answerResult = sql.get(MyDBInfo.ANSWERS_TABLE, "\"id=\""+answerID);
		ResultSet answerTextsResult = sql.get(MyDBInfo.ANSWER_TEXTS_TABLE, "\"answer_id=\""+answerID);
		if(answerResult == null || answerTextsResult == null){
			throw new RuntimeException("Retrieval failed.");
		}
		try{
			if(answerResult.first()){ // tuple found in ANSWERS_TABLE
				int qID = answerResult.getInt("question_id");
				int pos = answerResult.getInt("position");
				boolean corr = answerResult.getBoolean("correct");
				Set<String> answerTexts = new HashSet<String>();
				while(answerTextsResult.next()){
					answerTexts.add(answerTextsResult.getString("text"));
				}
				return new Answer(answerID, qID, pos, corr, answerTexts);
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return null;		
	}
	
	/**
	 * Retrieves all answers belonging to the question whose ID is questionID.
	 * If no answers were found returns an empty set.
	 * Throws a RuntimeException on retrieval error.
	 * @param questionID
	 * @return answerSet
	 */
	public static Collection<Answer> retrieveByQuestionID(int questionID) {
		MySQL sql = MySQL.getInstance();
		ResultSet answerResult = sql.get(MyDBInfo.ANSWERS_TABLE, "\"question_id=\""+questionID);
		if(answerResult == null){
			throw new RuntimeException("Retrieval failed.");
		}
		Collection<Answer> answerSet = new ArrayList <Answer>();
		try{
			while(answerResult.next()){
				int answerID = answerResult.getInt("id");
				answerSet.add(Answer.retrieveByID(answerID));
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return answerSet;
	}
	
	

}
