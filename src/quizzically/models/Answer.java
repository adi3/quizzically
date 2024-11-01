package quizzically.models;

import quizzically.config.MyDBInfo;
import quizzically.lib.*;

import java.util.*;

/**
 * Represents an answer to a question in a database, with attributes for correctness,
 * position, and associated text, and provides methods for creation, retrieval, and
 * deletion.
 */
public class Answer extends Model {
	private static final String TABLE = MyDBInfo.ANSWERS_TABLE;
	private static final String[] ANSWERS_COLUMNS = new String[]{"question_id", "position", "correct"};
	private static final String[] ANSWER_TEXTS_COLUMNS = new String[]{"answer_id", "text"};
	
	private int questionID; // ID of question to which this answer belongs
	private int position; // position of answer in question, -1 if not applicable
	private boolean correct;
	private Set<AnswerText> answerTexts; // set of Strings which correspond to this answer, e.g. {FloMo, Florence Moore}

	protected Answer(int ID, int questionID, int position, boolean correct, Set<AnswerText> answerTexts) {
		super(ID, TABLE, new AnswerHydrator());
		this.correct = correct;
		this.questionID = questionID;
		this.position = position;
		this.answerTexts = answerTexts;
	}
	
	/**
	 * Returns a boolean value indicating whether a condition or state is correct.
	 *
	 * @returns a boolean value representing the correctness of a particular state.
	 */
	public boolean correct() {
		return correct;
	}

	/**
	 * Sets a boolean value indicating whether an answer is correct, updating the internal
	 * state of the object with the provided `correct` parameter. It assigns the value
	 * directly to the `correct` field without any validation or side effects.
	 *
	 * @param correct value to be assigned to the `correct` field.
	 */
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
	
	/**
	 * Returns the value of the `questionID` variable.
	 * This function appears to be a getter method for accessing the `questionID` field.
	 *
	 * @returns an integer value representing the question ID.
	 */
	public int questionId() {
		return questionID;
	}
	
	/**
	 * Returns the current position, which is stored in the variable `position`.
	 * The function simply retrieves and returns this value.
	 *
	 * @returns the current position value, which is an integer.
	 */
	public int position() {
		return position;
	}

	/**
	 * Sets the position to a specified integer value, updating an instance variable named
	 * `position`. The function takes an integer as input and assigns it to the `position`
	 * variable. This allows the position to be dynamically updated.
	 *
	 * @param position new position value to be assigned to the object's `position` field.
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	
	/**
	 * Returns a set of answer texts.
	 *
	 * @returns a set of AnswerText objects.
	 */
	public Set<AnswerText> answerTexts() {
		return answerTexts;
	}

	/**
	 * Populates a set of `AnswerText` objects based on a given set of strings, then
	 * assigns this set to the `answerTexts` variable.
	 *
	 * @param texts set of answer texts to be added to the `answerTexts` collection, where
	 * each text is associated with a unique `id` and the given text string.
	 */
	public void setAnswerTextsStrings(Set<String> texts) {
		Set<AnswerText> ats = new HashSet<AnswerText>();
		for (String s : texts) {
			ats.add(new AnswerText(-1, id(), s));
		}
		
		answerTexts = ats;
	}
	
	/**
	 * Sets the `answerTexts` field to the provided `texts` set.
	 * It appears to be a setter function for the `answerTexts` field, likely used for
	 * data storage or synchronization purposes.
	 *
	 * @param texts set of answer texts to be stored in the `answerTexts` field.
	 */
	public void setAnswerTexts(Set<AnswerText> texts) {
		answerTexts = texts;
		// TODO update db..?
	}

	/**
	 * Get a single answer text
	 */
	public String text() {
		for (AnswerText t : answerTexts()) {
			return t.text();
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
		Answer answer = new Answer(-1, question.id(), position, correct, new HashSet<AnswerText>());
		answer.save(true);
		question.addAnswer(answer, position); // position vacant
		return answer;
	}

	/**
	 * Performs a database deletion of the current entity, along with its associated
	 * AnswerTexts, based on the specified conditions and table constraints.
	 */
	public void delete() {
		MySql sql = MySql.getInstance();
		QueryBuilder qb = QueryBuilder.deleteInstance(TABLE, cols());
		// delete my AnswerTexts
		AnswerText.deleteOthers(this, new HashSet<AnswerText>());
		qb.addConstraint("id", QueryBuilder.Operator.EQUALS, id());
		// delete me
		sql.delete(qb);
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
			answerTexts.add(AnswerText.create(this, answerText));
		}
	}
	
	
	/**
	 * Retrieves the Answer object stored in the db with the given ID.
	 * Throws a RuntimeException on retrieval error.
	 * Returns null if no tuple is found in the ANSWERS_TABLE with the given ID.
	 * @param ID
	 * @return retrieved Answer object
	 */
	public static Answer retrieve(int id){
		return (Answer) 
			Model.retrieve(TABLE, id, new AnswerHydrator());
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
		return id() == ((Answer)obj).id();
	}
	
	/**
	 * Returns an array of column names, specifically referencing a predefined constant
	 * `ANSWERS_COLUMNS`.
	 *
	 * @returns an array of strings representing column names.
	 */
	public String[] cols() {
		return ANSWERS_COLUMNS;
	}
}
