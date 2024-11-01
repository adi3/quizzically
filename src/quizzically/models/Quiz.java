package quizzically.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import quizzically.config.MyDBInfo;
import quizzically.lib.MySql;
import quizzically.lib.QueryBuilder;
import quizzically.lib.SqlResult;

/**
 * Represents a quiz entity with properties such as name, owner, questions and scoring
 * attempts, providing methods for creation, retrieval and manipulation.
 */
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

	/**
	 * Creates a new Quiz object, sets its properties, and saves it to the database,
	 * returning the newly created quiz instance.
	 *
	 * @param name name of the quiz being created and is passed to the `Quiz` constructor
	 * to initialize the quiz object.
	 *
	 * @param ownerId identifier of the user who owns the created Quiz.
	 *
	 * @param description description of the quiz being created.
	 *
	 * It is used as an argument when creating a new `Quiz` object.
	 *
	 * @param pageFormat format of the quiz pages.
	 *
	 * @param order ordering position of the quiz.
	 *
	 * @param immediateCorrection setting for enabling or disabling immediate correction
	 * for the created quiz.
	 *
	 * @returns a `Quiz` object representing the newly created quiz.
	 */
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

	/**
	 * Retrieves a list of quizzes associated with a specified owner ID from a database.
	 * It uses a query builder to construct a query and a hydrator to map the query results
	 * to quiz objects. The function returns an array of quiz objects.
	 *
	 * @param ownerId id of the quiz owner, which is used to filter quiz results.
	 *
	 * @returns an array of `Quiz` objects, filtered by the specified `ownerId`.
	 */
	public static Quiz[] retrieveByOwnerId(int ownerId) {
		MySql sql = MySql.getInstance();
		QueryBuilder qb = QueryBuilder.selectInstance(TABLE, QUIZZES_COLUMNS);
		Model[] models;
		qb.addConstraint("owner_id", QueryBuilder.Operator.EQUALS, ownerId);
		models = sql.getMany(qb, new QuizHydrator());
		return Arrays.copyOf(models, models.length, Quiz[].class);
	}

	/**
	 * Retrieves a maximum of 5 quizzes from the database for a specified owner ID, ordered
	 * by the 'completed_at' timestamp in descending order, and returns them as an array
	 * of Quiz objects.
	 *
	 * @param ownerId identifier of the entity whose quizzes are being retrieved.
	 *
	 * @returns an array of up to 5 Quiz objects ordered by most recently completed.
	 *
	 * The output is an array of `Quiz` objects. Each `Quiz` object has properties related
	 * to a quiz, such as its owner's ID, creation date, and possibly other details. The
	 * array contains a maximum of 5 elements, ordered by the completion date in descending
	 * order.
	 */
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

	/**
	 * Retrieves an array of Quiz objects from a database in descending order by the
	 * completed_at timestamp, excluding null values, and limits the result to 5 items.
	 * The retrieved data is then converted to a Quiz array.
	 *
	 * @returns an array of up to 5 Quiz objects sorted by completed_at in descending order.
	 */
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
	
	/**
	 * Sets the ordered questions, updating the internal state of the object with a sorted
	 * map of question IDs to their respective question objects. The map is stored in the
	 * `orderedQuestions` field.
	 *
	 * @param orderedQuestions ordered map of questions with their respective integer
	 * keys, which is assigned to the instance variable `orderedQuestions`.
	 */
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
	
	/**
	 * Generates a URL string by concatenating a static string with the result of the
	 * `id()` function, which presumably returns an identifier. This generated URL is in
	 * the format "TakeQuiz?id=identifier". It returns the resulting URL as a string.
	 *
	 * @returns a string containing the URL "TakeQuiz?id=" followed by the result of the
	 * `id` function.
	 */
	public String takeLink() {
		return "TakeQuiz?id="+id();
	}
	
	/**
	 * Constructs a string representing a URL for editing, combining a constant "Quiz"
	 * with the result of calling the `id` method.
	 *
	 * @returns a string concatenating "Quiz?id=" with the result of the `id` method.
	 */
	public String editLink() {
		return "Quiz?id="+id();
	}
	
	/**
	 * Returns the value of the `name` field, which is a string representing a person's
	 * or object's name. The function does not modify the `name` field; it simply retrieves
	 * its current value.
	 *
	 * @returns the value of the `name` variable.
	 */
	public String name() {
		return name;
	}

	/**
	 * Assigns a specified string value to the `name` attribute of the current object.
	 *
	 * @param name new value to be assigned to the object's `name` field.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the value of the `owner_id` variable, allowing access to the owner's identifier.
	 *
	 * @returns the integer value of the `owner_id` variable.
	 */
	public int ownerId() {
		return owner_id;
	}

	/**
	 * Assigns a specified integer value to the `owner_id` field, updating the owner's
	 * ID. This function modifies the object's state. The new ID is stored for future use.
	 *
	 * @param ownerId new ID of the owner being set.
	 */
	public void setOwnerId(int ownerId) {
		this.owner_id = ownerId;
	}
	
	/**
	 * Returns the current instance's owner object, presumably a User, without any
	 * modification or validation.
	 *
	 * @returns the value of the `owner` field, which is presumably a User object.
	 */
	public User owner(){
		return owner;
	}
	
	/**
	 * Sets the owner of an object to a specified user, updating the object's internal
	 * state with the new owner reference.
	 *
	 * @param owner new user entity that is being assigned to the current entity.
	 */
	protected void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * Returns the value of the `description` field.
	 *
	 * @returns a string value that is stored in the `description` variable.
	 */
	public String description() {
		return description;
	}

	/**
	 * Sets a new description for an object. It takes a `String` parameter and assigns
	 * it to the `description` field of the object.
	 *
	 * @param description value to be assigned to the instance variable `description`.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the date when the object was created. The function is a getter, allowing
	 * access to the `createdAt` field. It does not modify the field.
	 *
	 * @returns a Date object representing the creation time of the entity.
	 */
	public Date createdAt() {
		return createdAt;
	}

	/**
	 * Returns the value of the variable `pageFormat` as an integer, presumably indicating
	 * a page format setting.
	 *
	 * @returns an integer representing the page format.
	 */
	public int pageFormat() {
		return pageFormat;
	}

	/**
	 * Assigns a specified `pageFormat` value to an instance variable, effectively setting
	 * the page format for an object. This change is stored within the object itself,
	 * allowing for future retrieval and use. The function takes an integer parameter.
	 *
	 * @param pageFormat format of a page, and its value is assigned directly to the
	 * instance variable `pageFormat`.
	 */
	public void setPageFormat(int pageFormat) {
		this.pageFormat = pageFormat;
	}

	/**
	 * Returns the current order value.
	 *
	 * @returns the current value of the `order` instance variable.
	 */
	public int order() {
		return order;
	}

	/**
	 * Sets the value of the `order` field to the specified integer value, making it
	 * accessible for future use.
	 *
	 * @param order value assigned to the instance variable `order`.
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Returns a boolean value indicating the immediate correction status.
	 *
	 * @returns a boolean value representing the state of `immediateCorrection`.
	 */
	public boolean immediateCorrection() {
		return immediateCorrection;
	}

	/**
	 * Sets a boolean flag indicating whether immediate correction is enabled.
	 * The immediateCorrection variable is updated with the provided value.
	 *
	 * @param immediateCorrection flag that determines whether correction should be applied
	 * immediately.
	 */
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


	/**
	 * Returns an array of strings representing the columns for a quizzes database.
	 * The columns are likely defined in the QUIZZES_COLUMNS constant elsewhere in the code.
	 *
	 * @returns an array of strings containing column names for a quizzes database.
	 */
	public String[] cols() {
		return QUIZZES_COLUMNS;
	}

	/**
	 * Calculates the total possible points from a list of questions by summing up the
	 * possible points of each individual question. It retrieves a list of questions using
	 * the `questions` method and then iterates over the list to calculate the total points.
	 *
	 * @returns the total of all possible points from a list of questions.
	 */
	public int possiblePoints() {
		List<Question> questions = questions();
		int poss = 0;
		for (Question q : questions) {
			poss += q.possiblePoints();
		}
		return poss;
	}
}
