package quizzically.models;

import java.util.*;

import quizzically.lib.MySql;
import quizzically.lib.QueryBuilder;
import quizzically.lib.SqlResult;

public class QuizAttempt extends Model {
	private static final int NULL_VALUE = -1;
	private static final int SHOW_LIMIT = 5;
	private static final String TABLE = "quiz_attempts";
	private static final String[] COLUMNS = {"created_at", "completed_at", "score", "quiz_id", "user_id", "position"};
	
	private Quiz quiz;
	private int quizId;
	private User user;
	private int userId;
	private Date createdAt;
	private Date completedAt;
	private int score;
	private int position;

	protected QuizAttempt(int id, Quiz quiz, User user, Date createdAt, Date completedAt, 
			int score, int position) {
		super(id, TABLE, new QuizAttemptHydrator());
		this.quiz = quiz;
		this.user = user;
		this.quizId = quiz.id();
		this.userId = user.getId();
		this.createdAt = createdAt;
		this.completedAt = completedAt;
		this.score = score;
		this.position = position;
	}

	
	public static QuizAttempt create(Quiz quiz, User user) {
		Date createdAt = new Date();
		QuizAttempt qA = new QuizAttempt(NULL_VALUE, quiz, user, createdAt, null, 0, 0);
		qA.save(true); // dehydrates, inserts into DB and sets id to generated key
		return qA;
	}
	
	public static QuizAttempt retrieve(int id){
		return (QuizAttempt) Model.retrieve(TABLE, id, new QuizAttemptHydrator());
	}

	public static QuizAttempt[] retrieveByQuizIdOrderByCompletedAt(int quizId) {
		MySql sql = MySql.getInstance();
		QueryBuilder qb = QueryBuilder.selectInstance(TABLE, COLUMNS);
		Model[] models;
		qb.addConstraint("quiz_id", QueryBuilder.Operator.EQUALS, quizId);
		qb.addConstraint("completed_at", QueryBuilder.Operator.NOT_NULL);
		qb.setOrder("completed_at", QueryBuilder.Order.DESCENDING);
		qb.setLimit(5);
		models = sql.getMany(qb, new QuizAttemptHydrator());
		return Arrays.copyOf(models, models.length, QuizAttempt[].class);
	}

	public static QuizAttempt[] retrieveByQuizIdAndUserIdOrderByCompletedAt(int quizId, int userId) {
		MySql sql = MySql.getInstance();
		QueryBuilder qb = QueryBuilder.selectInstance(TABLE, COLUMNS);
		Model[] models;
		qb.addConstraint("user_id", QueryBuilder.Operator.EQUALS, userId);
		qb.addConstraint("quiz_id", QueryBuilder.Operator.EQUALS, quizId);
		qb.addConstraint("completed_at", QueryBuilder.Operator.NOT_NULL);
		qb.setOrder("completed_at", QueryBuilder.Order.DESCENDING);
		qb.setLimit(5);
		models = sql.getMany(qb, new QuizAttemptHydrator());
		return Arrays.copyOf(models, models.length, QuizAttempt[].class);
	}

	public static String averagePercent(Quiz quiz) {
		MySql sql = MySql.getInstance();
		String where = "`quiz_id` = " + quiz.id() + " AND `completed_at` IS NOT NULL";
		SqlResult r = sql.get(new String[]{"AVG(`score`)"}, TABLE, where);
		HashMap<String, String> hm = r.get(0);
		String avg = "0";
		// should only be one! i hope...
		for (String s : hm.values()) {
			avg = s;
		}
		Double d = 0.0;
		try {
			d = Double.parseDouble(avg);
		} catch (NumberFormatException e) {}
		int scoreTruncated = (int) d.doubleValue();
		return percentCorrect(scoreTruncated, quiz);
	}


	public static QuizAttempt[] retrieveByQuizIdAfterDateOrderByScore(int quizId, Date date) {
		MySql sql = MySql.getInstance();
		QueryBuilder qb = QueryBuilder.selectInstance(TABLE, COLUMNS);
		Model[] models;
		qb.addConstraint("quiz_id", QueryBuilder.Operator.EQUALS, quizId);
		qb.addConstraint("completed_at", QueryBuilder.Operator.NOT_NULL);
		qb.addConstraint("completed_at", QueryBuilder.Operator.GREATER_THAN_OR_EQUAL, date);
		qb.setOrder("score", QueryBuilder.Order.DESCENDING);
		qb.setLimit(5);
		models = sql.getMany(qb, new QuizAttemptHydrator());
		return Arrays.copyOf(models, models.length, QuizAttempt[].class);
	}
	

	public static QuizAttempt[] retrieveByQuizIdOrderByScore(int quizId) {
		MySql sql = MySql.getInstance();
		QueryBuilder qb = QueryBuilder.selectInstance(TABLE, COLUMNS);
		Model[] models;
		qb.addConstraint("quiz_id", QueryBuilder.Operator.EQUALS, quizId);
		qb.addConstraint("completed_at", QueryBuilder.Operator.NOT_NULL);
		qb.setOrder("score", QueryBuilder.Order.DESCENDING);
		qb.setLimit(5);
		models = sql.getMany(qb, new QuizAttemptHydrator());
		return Arrays.copyOf(models, models.length, QuizAttempt[].class);
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

	public User user() {
		return user;
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

	public String percentCorrect() {
		return percentCorrect(this.score(), this.quiz());
	}


	public String timeTaken() {
		Date after = completedAt();
		Date before = createdAt();
		long diff = after.getTime() - before.getTime();
		return (diff / 1000) + " seconds";
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
		return COLUMNS;
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
	
	
	
	
	
	
	

	/**
	 * percentage correct given a score
	 * @return score / possible
	 */
	public static String percentCorrect(int score, Quiz quiz) {
		float pct = (float) score / quiz.possiblePoints();
		return "" + (int) (pct * 100) + "%";
	}


}
