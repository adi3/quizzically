package quizzically.models;

import java.util.*;

public class QuizAttempt extends Model {
	private static final int NULL_VALUE = -1;
	private static final String TABLE = "quiz_attempts";
	private static final String[] QUIZ_ATTEMPTS_COLUMNS = {"created_at", "completed_at", "score", "quiz_id", "user_id", "position"};
	
	private Quiz quiz;
	private int quizId;
	private int userId;
	private Date createdAt;
	private Date completedAt;
	private int score;
	private int position;

	protected QuizAttempt(int id, Quiz quiz, int userId, Date createdAt, Date completedAt, 
			int score, int position) {
		super(id, TABLE, new QuizAttemptHydrator());
		this.quiz = quiz;
		this.quizId = quiz.id();
		this.userId = userId;
		this.createdAt = createdAt;
		this.completedAt = completedAt;
		this.score = score;
		this.position = position;
	}

	
	public static QuizAttempt create(Quiz quiz, int user_id) {
		Date createdAt = new Date();
		QuizAttempt qA = new QuizAttempt(NULL_VALUE, quiz, user_id, createdAt, null, 0, 0);
		qA.save(true); // dehydrates, inserts into DB and sets id to generated key
		return qA;
	}
	
	public static QuizAttempt retrieve(int id){
		return (QuizAttempt) Model.retrieve(TABLE, id, new QuizAttemptHydrator());
	}
	
	public boolean completed() {
		return completedAt != null;
	}
	
	public int quizId() {
		return quizId;
	}

	public Quiz quiz() {
		return quiz;
	}
	
	public int userId() {
		return userId;
	}
	
	public Date createdAt() {
		return createdAt;
	}
	
	public Date completedAt() {
		return completedAt;
	}
	
	public int score() {
		return score;
	}
	
	public int position() {
		return position;
	}
	
	public void setPosition(int position){
		this.position = position;
	}
	
	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	@Override
	public String[] cols() {
		return QUIZ_ATTEMPTS_COLUMNS;
	}

}
