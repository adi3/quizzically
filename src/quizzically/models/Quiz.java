package quizzically.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import quizzically.config.MyDBInfo;
import quizzically.lib.MySql;
import quizzically.lib.QueryBuilder;
import quizzically.lib.SqlResult;

public class Quiz extends Model {
	private static final String TABLE = MyDBInfo.QUIZZES_TABLE;
	private static final String[] QUIZZES_COLUMNS = new String[]{"name", "owner_id", "description", "created_at", "page_format", "order", "immediate_correction"};
	private static final String[] QUIZ_QUESTIONS_COLUMNS = new String[]{"quiz_id", "question_id", "position"};

	public static final int PAGE_FORMAT_ALL_IN_ONE = 0;
	public static final int PAGE_FORMAT_ONE_PER_PAGE = 1;

	public static final int[] PAGE_FORMATS = {
		PAGE_FORMAT_ALL_IN_ONE,
		PAGE_FORMAT_ONE_PER_PAGE
	};

	public static final String[] PAGE_FORMAT_STRINGS = {
		"Single Page",
		"Multiple Pages"
	};

	public static final int ORDER_STANDARD = 0;
	public static final int ORDER_RANDOM = 1;

	public static final int[] ORDERS = {
		ORDER_STANDARD,
		ORDER_RANDOM
	};

	public static final String[] ORDER_STRINGS = {
		"Given Order",
		"Random Order"
	};

	private String name;
	private String description;
	private int owner_id;
	private Date createdAt;
	private int pageFormat, order;
	private boolean immediateCorrection;
	private User owner;
	private SortedMap<Integer, Question> orderedQuestions;
	
	protected Quiz(int id, String name, int owner_id, 
			String description, Date createdAt, int pageFormat, 
			int order, boolean immediateCorrection) {
		this(id, name, owner_id, description, createdAt, 
				pageFormat, order, immediateCorrection, 
				new TreeMap<Integer, Question>());
	}

	private Quiz(int id, String name, int owner_id, 
			String description, Date createdAt, int pageFormat, 
			int order, boolean immediateCorrection,
			SortedMap<Integer, Question> questions) {
		super(id, TABLE, new QuizHydrator());
		this.name = name;
		this.owner_id = owner_id;
		this.description = description;
		this.createdAt = createdAt;
		this.pageFormat = pageFormat;
		this.order = order;
		this.immediateCorrection = immediateCorrection;
		this.orderedQuestions = questions;
	}

	public static Quiz create(String name, int ownerId, 
			String description, int pageFormat, 
			int order, boolean immediateCorrection) {
		Date createdAt = new Date(); // now
		Quiz quiz = new Quiz(-1, name, ownerId, description, 
				createdAt, pageFormat, order, immediateCorrection);
		quiz.save(true);
		return quiz;
	}
	
	/**
	 * Get the quiz with the given id or null if it doesn't exist
	 */
	public static Quiz retrieve(int id) {
		Model m = Model.retrieve(TABLE, id, new QuizHydrator());
		Quiz quiz = (Quiz) m;
		return quiz;
	}

	public static Quiz[] retrieveByOwnerId(int ownerId) {
		MySql sql = MySql.getInstance();
		QueryBuilder qb = QueryBuilder.selectInstance(TABLE, QUIZZES_COLUMNS);
		Model[] models;
		qb.addConstraint("owner_id", QueryBuilder.Operator.EQUALS, ownerId);
		models = sql.getMany(qb, new QuizHydrator());
		return Arrays.copyOf(models, models.length, Quiz[].class);
	}

	public static Quiz[] retrieveByOwnerIdOrderByCreated(int ownerId) {
		MySql sql = MySql.getInstance();
		QueryBuilder qb = QueryBuilder.selectInstance(TABLE, QUIZZES_COLUMNS);
		Model[] models;
		qb.addConstraint("owner_id", QueryBuilder.Operator.EQUALS, ownerId);
		qb.addConstraint("completed_at", QueryBuilder.Operator.NOT_NULL);
		qb.setOrder("completed_at", QueryBuilder.Order.DESCENDING);
		qb.setLimit(5);
		models = sql.getMany(qb, new QuizHydrator());
		return Arrays.copyOf(models, models.length, Quiz[].class);
	}

	public static Quiz[] retrieveOrderByCreated() {
		MySql sql = MySql.getInstance();
		QueryBuilder qb = QueryBuilder.selectInstance(TABLE, QUIZZES_COLUMNS);
		Model[] models;
		qb.addConstraint("completed_at", QueryBuilder.Operator.NOT_NULL);
		qb.setOrder("completed_at", QueryBuilder.Order.DESCENDING);
		qb.setLimit(5);
		models = sql.getMany(qb, new QuizHydrator());
		return Arrays.copyOf(models, models.length, Quiz[].class);
	}
	
	protected void setQuestions(SortedMap<Integer, Question> orderedQuestions) {
		this.orderedQuestions = orderedQuestions;
	}

	/**
	 * Appends the question to the quiz as in
	 * addQuestion(Question, position)
	 */
	public void addQuestion(Question question) {
		Integer key =  orderedQuestions.size() != 0 ? 
			orderedQuestions.lastKey() + 1 : 1;
		addQuestion(question, key);
	}

	/**
	 * Adds the question to the questions list, and inserts the quiz-question relation
	 * into the QUIZ_QUESTIONS_TABLE. question can have either been just created or
	 * it can belong to other Quizzes.
	 * If question is already contained in the quiz or if position is taken
	 * the method does nothing.
	 * @param question
	 */
	public void addQuestion(Question question, int position){
		MySql sql = MySql.getInstance();
		if(orderedQuestions.containsKey(position) && 
				!orderedQuestions.containsValue(question)) {
			orderedQuestions.put(position, question);
			sql.update(MyDBInfo.QUIZ_QUESTIONS_TABLE, 
					"`question_id`=" + question.id(),
					"`position`=" + position + " AND " +
					"`quiz_id`=" + this.id()
				);
			return;
		}
		// Note that when addQuestion is called all questions in the 
		// DB are also in the SortedMap of the Quiz object
		if(! orderedQuestions.containsValue(question)){ // id comparison
			orderedQuestions.put(position, question);
			String[] values = {Integer.toString(id()), Integer.toString(question.id()), Integer.toString(position)};
			sql.insert(MyDBInfo.QUIZ_QUESTIONS_TABLE, QUIZ_QUESTIONS_COLUMNS, values);
		}
	}
	
	public String takeLink() {
		return "TakeQuiz?id="+id();
	}
	
	public String editLink() {
		return "Quiz?id="+id();
	}
	
	public String name() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int ownerId() {
		return owner_id;
	}

	public void setOwnerId(int ownerId) {
		this.owner_id = ownerId;
	}
	
	public User owner(){
		return owner;
	}
	
	protected void setOwner(User owner) {
		this.owner = owner;
	}

	public String description() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date createdAt() {
		return createdAt;
	}

	public int pageFormat() {
		return pageFormat;
	}

	public void setPageFormat(int pageFormat) {
		this.pageFormat = pageFormat;
	}

	public int order() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean immediateCorrection() {
		return immediateCorrection;
	}

	public void setImmediateCorrection(boolean immediateCorrection) {
		this.immediateCorrection = immediateCorrection;
	}

	/**
	 * Return owned Questions sorted by position
	 * @return
	 */
	public List<Question> questions() {
		List<Question> questions = new ArrayList<Question>();
		for (Question q: orderedQuestions.values()) { // in sorted order
			questions.add(q);
		}
		
		if (order == ORDER_RANDOM) { // shuffle questions
			Collections.shuffle(questions);
		}
		
		return questions;
	}

	/**
	 * Get the user's most recent attempts on the quiz
	 */
	public QuizAttempt[] userAttempts(User user) {
		return QuizAttempt.retrieveByQuizIdAndUserIdOrderByCompletedAt(id(), user.getId());
	}

	/**
	 * Get the highest scoring attempts on the quiz
	 */
	public QuizAttempt[] highestAttempts() {
		return QuizAttempt.retrieveByQuizIdOrderByScore(id());
	}


	/**
	 * Get the highest scoring attempts on the quiz
	 */
	public QuizAttempt[] highestTodayAttempts() {
		// 24 h ago
		Date today = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000));

		return QuizAttempt.retrieveByQuizIdAfterDateOrderByScore(id(), today);
	}

	/**
	 * Get the highest scoring attempts on the quiz
	 */
	public QuizAttempt[] recentAttempts() {
		return QuizAttempt.retrieveByQuizIdOrderByCompletedAt(id());
	}


	public String[] cols() {
		return QUIZZES_COLUMNS;
	}

	public int possiblePoints() {
		List<Question> questions = questions();
		int poss = 0;
		for (Question q : questions) {
			poss += q.possiblePoints();
		}
		return poss;
	}
}
