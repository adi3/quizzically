package quizzically.models;

import java.util.*;

import quizzically.lib.MySql;
import quizzically.lib.SqlResult;

public class QuizAttempt extends Model {
	private static final int NULL_VALUE = -1;
	private static final int SHOW_LIMIT = 5;
	private static final String TABLE = "quiz_attempts";
	private static final String[] QUIZ_ATTEMPTS_COLUMNS = {"created_at", "completed_at", "score", "quiz_id", "user_id", "position"};
	
	private int quizId;
	private int userId;
	private Date createdAt;
	private Date completedAt;
	private int score;
	private int position;

	protected QuizAttempt(int id, int quizId, int userId, Date createdAt, Date completedAt, 
			int score, int position) {
		super(id, TABLE, new QuizAttemptHydrator());
		this.quizId = quizId;
		this.userId = userId;
		this.createdAt = createdAt;
		this.completedAt = completedAt;
		this.score = score;
		this.position = position;
	}

	
	public static QuizAttempt create(int quiz_id, int user_id) {
		Date createdAt = new Date();
		QuizAttempt qA = new QuizAttempt(NULL_VALUE, quiz_id, user_id, createdAt, null, NULL_VALUE, 0);
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
	
	
	
	public static List<Quiz> popularQuizzes() {
		List<Quiz> popularQuizzes = new ArrayList<Quiz>();
		MySql sql = MySql.getInstance();
		String query = "SELECT COUNT(*) as `POP`, `quiz_id` FROM `quiz_attempts` GROUP BY `quiz_id` ORDER BY `POP` DESC";
		SqlResult result = sql.getCustomQuery(query);
		for(int i=0; i<Math.min(SHOW_LIMIT, result.size()); i++){
			HashMap<String, String> row = result.get(i);
			popularQuizzes.add(Quiz.retrieve(Integer.parseInt(row.get("quiz_id"))));
		}
		return popularQuizzes;
	}
	
	
	
	
	
	
	

}
