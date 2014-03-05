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

	private Answer(int ID, int questionID, int position, boolean correct, Set<String> answerTexts) {
		this.ID = ID;
		this.correct = correct;
		this.questionID = questionID;
		this.position = position;
		this.answerTexts = answerTexts;
	}
	
	public boolean isCorrect() {
		return correct;
	}
	
	/**
	 * save object to database.
	 * called by createAnswer
	 */
	private void save() {
		
	}
	
	// TODO
	public static Answer create(int questionID, int position, boolean correct, Set<String> answerTexts) {
		MySQL sql = MySQL.getInstance();
		// tuple to be inserted in ANSWERS_TABLE
		String[] answerValues = new String[]{Integer.toString(questionID), Integer.toString(position), Boolean.toString(correct)};
		ResultSet genKeys = sql.insert(MyDBInfo.ANSWERS_TABLE, ANSWERS_COLUMNS, answerValues);
		try{
			if(genKeys.first()){
				int insertionID = genKeys.getInt(1); // ID of inserted answer
				for(String answerText: answerTexts){
					// tuple to be inserted in ANSWER_TEXTS_TABLE
					String[] textValues = new String[]{Integer.toString(insertionID), answerText};
					ResultSet genTextKeys = sql.insert(MyDBInfo.ANSWER_TEXTS_TABLE, ANSWER_TEXTS_COLUMNS, textValues);
					if(!genTextKeys.first()){
						throw new RuntimeException("Insertion failed.");
					}
				}
				return new Answer(insertionID, questionID, position, correct, answerTexts);
			} else {
				throw new RuntimeException("Insertion failed.");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static Answer retrieveByID(int ID){
		MySQL sql = MySQL.getInstance();
		ResultSet answerResult = sql.get(MyDBInfo.ANSWERS_TABLE, "id="+ID);
		return null;
		
	}
	
	public static Set<Answer> retrieveByQuestionID(int questionID) {
		return null;
	}
	
	

}
