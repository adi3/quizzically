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
	 * The Answer object is created *only* if the desired position in the Question is not occupied,
	 * in which case the newly created Answer is inserted into the Question object's 
	 * Answer collection. If the condition fails the method returns null.
	 * Note: The rationale behind this protocol is that an Answer is owned by a unique Question,
	 * hence adding an Answer to a Question occurs exactly once in its lifetime.
	 * The protocol is different when creating a Question object, as a Question can be owned
	 * by multiple Quizzes.
	 * @param question
	 * @param position
	 * @param correct
	 * @param answerTexts
	 * @return newly created Answer object
	 */
	public static Answer create(Question question, int position, boolean correct) {
		if(question.occupiedPositions().contains(position)){
			return null;
		}
		MySql sql = MySql.getInstance();
		// tuple to be inserted in ANSWERS_TABLE
		String[] answerValues = {Integer.toString(question.id()), Integer.toString(position), correct ? "1" : "0"};
		int insertionID = sql.insert(MyDBInfo.ANSWERS_TABLE, ANSWERS_COLUMNS, answerValues);
		Answer newAnswer = new Answer(insertionID, question.id(), position, correct, new HashSet<String>());
		question.addAnswer(newAnswer, position); // position vacant
		return newAnswer;
	}
	
	/**
	 * Add specified String to Set of answerTexts.
	 * Insert relation into ANSWER_TEXTS_TABLE.
	 * @param answerText
	 */
	public void addAnswerText(String answerText){
		// all answer texts in DB currently contained
		// in answerTexts
		if(! answerTexts.contains(answerText)){
			answerTexts.add(answerText);
			MySql sql = MySql.getInstance();
			String[] values = {Integer.toString(ID), answerText};
			sql.insert(MyDBInfo.ANSWER_TEXTS_TABLE, ANSWER_TEXTS_COLUMNS, values);
		}
	}
	
	
	/**
	 * Retrieves the Answer object stored in the db with the given ID.
	 * Throws a RuntimeException on retrieval error.
	 * Returns null if no tuple is found in the ANSWERS_TABLE with the given ID.
	 * @param ID
	 * @return retrieved Answer object
	 */
	public static Answer retrieve(int answerID){
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
	public static SortedMap<Integer, Answer> retrieveByQuestionID(int questionID) {
		SortedMap<Integer, Answer> orderedAnswers = new TreeMap<Integer, Answer>();
		MySql sql = MySql.getInstance();
		SqlResult answerResult = sql.get(MyDBInfo.ANSWERS_TABLE, "`question_id`="+questionID);
		if(answerResult.size() == 0){
			return orderedAnswers;
		}
		
		for(int i=0; i<answerResult.size(); i++){
			try{
				int answerID = Integer.parseInt(answerResult.get(i).get("id"));
				int position = Integer.parseInt(answerResult.get(i).get("position"));
				orderedAnswers.put(position, Answer.retrieve(answerID));
			} catch(NumberFormatException e){
				// ignore
			}
		}
		
		return orderedAnswers;
	}
	
	/**
	 * True if obj is an Answer with the same id.
	 */
	@Override
	public boolean equals(Object obj){
		if(obj == this) return true;
		if(! (obj instanceof Answer)) return false;
		return ID == ((Answer)obj).ID;
	}
	
	

}
