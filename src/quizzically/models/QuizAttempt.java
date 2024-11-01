package quizzically.models;

import java.util.*;

import quizzically.lib.MySql;
import quizzically.lib.QueryBuilder;
import quizzically.lib.SqlResult;

/**
 * Provides functionality for managing quiz attempts, including creation, retrieval,
 * and analysis of quiz attempt data.
 */
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

	
	/**
	 * Instantiates a new `QuizAttempt` object with default values, saves it to the
	 * database, and returns the saved object. The `save` method dehydrates the object,
	 * inserts it into the database, and sets the object's ID to the generated key.
	 *
	 * @param quiz Quiz object associated with the newly created QuizAttempt.
	 *
	 * @param user individual taking the quiz, used to associate the quiz attempt with
	 * the user.
	 *
	 * @returns a `QuizAttempt` object with all its properties initialized and saved to
	 * the database.
	 */
	public static QuizAttempt create(Quiz quiz, User user) {
		Date createdAt = new Date();
		QuizAttempt qA = new QuizAttempt(NULL_VALUE, quiz, user, createdAt, null, 0, 0);
		qA.save(true); // dehydrates, inserts into DB and sets id to generated key
		return qA;
	}
	
	/**
	 * Retrieves a `QuizAttempt` object from the database based on the provided `id` and
	 * returns it, using a `QuizAttemptHydrator` to hydrate the object.
	 *
	 * @param id identifier of the QuizAttempt to be retrieved from the database.
	 *
	 * @returns an instance of the `QuizAttempt` class, hydrated from database data.
	 */
	public static QuizAttempt retrieve(int id){
		return (QuizAttempt) Model.retrieve(TABLE, id, new QuizAttemptHydrator());
	}

	/**
	 * Retrieves a list of up to 5 QuizAttempt objects from a database, ordered by their
	 * completed_at timestamp in descending order, where the quiz_id matches the specified
	 * id and completed_at is not null.
	 *
	 * @param quizId identifier for the quiz for which quiz attempts are retrieved and filtered.
	 *
	 * @returns an array of `QuizAttempt` objects ordered by `completed_at` in descending
	 * order.
	 *
	 * The output returned by the function is an array of QuizAttempt objects. Each
	 * QuizAttempt object contains quiz_id, completed_at, and possibly other properties.
	 * The array is ordered by completed_at in descending order, with the most recent
	 * attempts first.
	 */
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

	/**
	 * Retrieves completed quiz attempts ordered by completion date in descending order,
	 * for a specified user and quiz, and returns up to 5 results.
	 *
	 * @param quizId identifier for a quiz, used as a constraint in a database query to
	 * retrieve quiz attempts for a specific user.
	 *
	 * @param userId identifier for the user for whom quiz attempts are to be retrieved.
	 *
	 * @returns an array of `QuizAttempt` objects ordered by `completed_at` in descending
	 * order.
	 *
	 * The output is an array of `QuizAttempt` objects, each containing the quiz attempt
	 * data.
	 */
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

	/**
	 * Retrieves a list of quiz attempts for a given user ID, sorted in descending order
	 * by the completed date, and returns up to 5 results.
	 *
	 * @param userId identifier used to filter quiz attempts retrieved from the database.
	 *
	 * @returns an array of QuizAttempt objects sorted by completed_at in descending order.
	 *
	 * The output is an array of `QuizAttempt` objects. Each `QuizAttempt` object has
	 * properties such as user_id, completed_at, and other attributes as specified in the
	 * `QuizAttempt` class. The array contains up to 5 objects ordered in descending order
	 * by completed_at.
	 */
	public static QuizAttempt[] retrieveByUserIdOrderByCompletedAt(int userId) {
		MySql sql = MySql.getInstance();
		QueryBuilder qb = QueryBuilder.selectInstance(TABLE, COLUMNS);
		Model[] models;
		qb.addConstraint("user_id", QueryBuilder.Operator.EQUALS, userId);
		qb.addConstraint("completed_at", QueryBuilder.Operator.NOT_NULL);
		qb.setOrder("completed_at", QueryBuilder.Order.DESCENDING);
		qb.setLimit(5);
		models = sql.getMany(qb, new QuizAttemptHydrator());
		return Arrays.copyOf(models, models.length, QuizAttempt[].class);
	}

	/**
	 * Calculates the average score of a completed quiz and returns it as a percentage,
	 * rounded to the nearest integer. It retrieves the average score from the database
	 * using the quiz ID and converts it to a double value.
	 *
	 * @param quiz object that contains the quiz's id, which is used to filter the query
	 * results in the database.
	 *
	 * Contain `id` property.
	 *
	 * @returns a string representing the average percentage score of a given quiz.
	 *
	 * The returned output is a string representing the average percentage correct score.
	 */
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


	/**
	 * Retrieves the top 5 QuizAttempt records by score in descending order from a database,
	 * where the quiz_id matches the specified ID and the completed_at date is after the
	 * specified date.
	 *
	 * @param quizId identifier that filters the retrieved `QuizAttempt` objects for a
	 * specific quiz.
	 *
	 * @param date minimum completion date of quizzes for which results should be retrieved.
	 *
	 * Include year, month, and day as separate properties.
	 *
	 * @returns an array of `QuizAttempt` objects ordered by score in descending order.
	 *
	 * The output is an array of `QuizAttempt` objects. Each `QuizAttempt` object has
	 * properties such as `quiz_id`, `completed_at`, and `score`.
	 */
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
	

	/**
	 * Retrieves a limited number of QuizAttempt objects from the database, ordered by
	 * score in descending order, where the quiz ID matches the specified ID and the
	 * attempt is completed. The function returns an array of QuizAttempt objects.
	 *
	 * @param quizId identifier for a quiz that is used to filter and retrieve quiz attempts.
	 *
	 * @returns an array of the top 5 QuizAttempt objects by score for the specified quiz
	 * ID.
	 *
	 * Contain an array of QuizAttempt objects.
	 * Each QuizAttempt object contains properties for quiz_id, completed_at, and score.
	 */
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
	
	/**
	 * Indicates whether a task has been completed by checking if a `completedAt` timestamp
	 * exists. It returns `true` if the task is completed and `false` otherwise. The
	 * presence of a timestamp signifies task completion.
	 *
	 * @returns a boolean value indicating whether a completion date has been set.
	 */
	public boolean completed() {
		return completedAt != null;
	}
	
	/**
	 * Returns the value of the `quizId` variable. The function simply retrieves and
	 * returns the stored `quizId` without any calculations or modifications. It is a
	 * getter method for the `quizId` field.
	 *
	 * @returns the value of the `quizId` variable.
	 */
	public int quizId() {
		return quizId;
	}

	/**
	 * Returns an instance of the `Quiz` class, likely a single object that encapsulates
	 * quiz-related data and functionality.
	 *
	 * @returns an instance of the `Quiz` class.
	 */
	public Quiz quiz() {
		return quiz;
	}
	
	/**
	 * Returns the value of the `userId` variable. The function does not modify or update
	 * the `userId` value; it simply retrieves its current state. The function is a getter,
	 * allowing external access to the `userId` variable.
	 *
	 * @returns the value of the `userId` variable.
	 */
	public int userId() {
		return userId;
	}

	/**
	 * Returns the value of the `user` variable.
	 * It is a getter method, likely used to access the user object.
	 *
	 * @returns an object reference of type `User`.
	 */
	public User user() {
		return user;
	}
	
	/**
	 * Returns the value of the `createdAt` field, which is likely a Date object representing
	 * the date and time when the object was created.
	 *
	 * @returns a Date object representing the creation date of an entity.
	 */
	public Date createdAt() {
		return createdAt;
	}
	
	/**
	 * Returns the date of completion.
	 * It appears to be a getter method, allowing access to an internal `completedAt` field.
	 *
	 * @returns a Date object representing the completion date.
	 */
	public Date completedAt() {
		return completedAt;
	}
	
	/**
	 * Returns the value of the `score` variable. The function does not modify the `score`
	 * value; it simply retrieves and returns its current value.
	 *
	 * @returns the value of the `score` variable.
	 */
	public int score() {
		return score;
	}
	
	/**
	 * Returns the current position value, which is stored in the `position` variable.
	 * The function does not update or change the position value; it simply provides
	 * access to its current state.
	 *
	 * @returns the current position value, which is an integer.
	 */
	public int position() {
		return position;
	}

	/**
	 * Calculates the percentage of correct answers by calling another function with the
	 * current score and quiz as parameters. The result is returned as a string. It appears
	 * to be a wrapper function, delegating the actual calculation to another method.
	 *
	 * @returns the percentage of correct answers in the quiz.
	 */
	public String percentCorrect() {
		return percentCorrect(this.score(), this.quiz());
	}


	/**
	 * Calculates the time elapsed between the creation and completion of an object. It
	 * returns the duration in seconds. The calculation is based on the time difference
	 * between the `createdAt` and `completedAt` dates.
	 *
	 * @returns the time in seconds taken to complete a task.
	 */
	public String timeTaken() {
		Date after = completedAt();
		Date before = createdAt();
		long diff = after.getTime() - before.getTime();
		return (diff / 1000) + " seconds";
	}
	
	/**
	 * Sets a new integer value to the `position` variable, updating its current state.
	 * The function takes an integer parameter and assigns it to the `position` field.
	 *
	 * @param position new position to be assigned to the object's internal state.
	 */
	public void setPosition(int position){
		this.position = position;
	}
	
	/**
	 * Sets the completion date of an object to the specified `Date` instance, updating
	 * the `completedAt` field.
	 *
	 * @param completedAt date when an activity or task is completed.
	 */
	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}
	
	/**
	 * Assigns the provided integer `score` to the instance variable `score`.
	 *
	 * @param score new value to be assigned to the object's score field.
	 */
	public void setScore(int score) {
		this.score = score;
	}
	
	/**
	 * Returns an array of column names, defined by the constant `COLUMNS`. The function
	 * overrides a default implementation, likely part of an interface or abstract class.
	 * It provides a specific column configuration.
	 *
	 * @returns an array of strings containing column names.
	 */
	@Override
	public String[] cols() {
		return COLUMNS;
	}
	
	/**
	 * Retrieves a list of popular quizzes from a database, ordered by the number of
	 * attempts in descending order, and returns them as a list of `Quiz` objects, limited
	 * to a maximum of `SHOW_LIMIT` quizzes.
	 *
	 * @returns a list of `Quiz` objects ordered by their popularity in descending order.
	 *
	 * The output is a List of Quiz objects. Each Quiz object contains attributes related
	 * to a quiz. The attributes include quiz information.
	 */
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


	/**
	 * This should be in a util clss
	 * Renders a table of Quiz Attempts
	 */
	public static String renderTable(QuizAttempt[] attempts) {
		return renderTable(attempts, true);
	}

	/**
	 * Generates an HTML table displaying quiz attempt statistics. It takes an array of
	 * `QuizAttempt` objects and a boolean indicating whether to show the user's name,
	 * then returns the table as a string.
	 *
	 * @param attempts array of QuizAttempt objects to be rendered as a table.
	 *
	 * Destructure `attempts` into its constituent elements.
	 * The `attempts` is an array of `QuizAttempt` objects.
	 *
	 * @param showUser boolean flag that determines whether to display the user's name
	 * and profile link in the table.
	 *
	 * @returns an HTML table displaying QuizAttempt data, including user information if
	 * requested.
	 */
	public static String renderTable(QuizAttempt[] attempts, boolean showUser) {
		String output = "";
		if (attempts.length == 0) {
			return "Nothing to see here...";
		}

		output += "<table>";
		if (showUser) {
			output += "<th>Who</th>";
		}
		output += "<th>Percent Correct</th>";
		output += "<th>Time Taken</th>";
		output += "<th>When</th>";
		for (QuizAttempt qa : attempts) {
			output += "<tr>";
			if (showUser) {
				output += "<td>" +
					"<a href=\"" + qa.user().profileLink() + "\">" + 
					qa.user().getName() + "</a>" +
					"</td>";
			}
			output += "<td>" +
				qa.percentCorrect() +
				"</td>";
			output += "<td>" +
				qa.timeTaken() +
				"</td>";
			output += "<td>" +
				qa.completedAt() +
				"</td>";
			output += "</tr>";
		}
		output += "</table>";
		return output;
	}

}
