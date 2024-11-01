package quizzically.models;

import java.util.*;

import quizzically.config.MyDBInfo;
import quizzically.lib.MySql;
import quizzically.lib.QueryBuilder;

/**
 * This class represents user achievements in a system, with methods to create,
 * retrieve, and earn achievements based on user activity.
 */
public class Achievement extends Model {
	private static final int NULL_VALUE = -1;
	private static final String TABLE = "achievements";
	private static final String[] ACHIEVEMENTS_COLUMNS = {"type", "user_id"};
	
	// TODO: should have used enums
	private static final int AMATEUR_AUTHOR = 0;
	private static final int PROLIFIC_AUTHOR = 1;
	private static final int PRODIGIOUS_AUTHOR = 2;
	private static final int LEGIONARY = 3;
	private static final int CONQUEROR = 4;
	
	private static final int AMATEUR_LIMIT = 1;
	private static final int PROLIFIC_LIMIT = 5;
	private static final int PRODIGIOUS_LIMIT = 10;
	private static final int LEGIONARY_LIMIT = 10;

	private int id;
	private int type;
	private int userId;

	protected Achievement(int id, int type, int userId) {
		super(id, TABLE, new AchievementHydrator());
		this.type = type;
		this.userId = userId;
	}
	
	
	/**
	 * Creates a new Achievement object with a specified type and user ID, saves it to
	 * the database, and returns the Achievement object with its ID set to the generated
	 * key.
	 *
	 * @param type type of achievement being created, used as a parameter in the `Achievement`
	 * constructor.
	 *
	 * @param userId identifier of the user associated with the achievement being created.
	 *
	 * @returns an instance of the Achievement class with a generated ID.
	 */
	private static Achievement create(int type, int userId) {
		Achievement ach = new Achievement(NULL_VALUE, type, userId);
		ach.save(true); // dehydrates, inserts into DB and sets id to generated key
		return ach;
	}
	
	/**
	 * Retrieves an achievement from the database based on its ID, utilizing a hydrator
	 * to convert the retrieved data into an Achievement object.
	 *
	 * @param id unique identifier of the achievement to be retrieved.
	 *
	 * @returns an instance of the Achievement class, hydrated from database data.
	 */
	private static Achievement retrieve(int id){
		return (Achievement) Model.retrieve(TABLE, id, new AchievementHydrator());
	}
	
	/**
	 * @param userId: id of user who just wrote a quiz
	 * @return
	 */
	public static List<Achievement> newAuthorAchievements(int userId) {
		Set<Integer> previousTypes = Achievement.achievementTypesEarned(Achievement.earnedAchievements(userId));
		Set<Integer> currentTypes = new HashSet<Integer>();
		
		MySql sql = MySql.getInstance();
		String table = MyDBInfo.QUIZZES_TABLE;
		String condition = "`owner_id` = " + userId;
		int authored = sql.count(table, condition);
		if(authored >= AMATEUR_LIMIT) {
			currentTypes.add(AMATEUR_AUTHOR);
		}
		if(authored >= PROLIFIC_LIMIT) {
			currentTypes.add(PROLIFIC_AUTHOR);
		}
		if(authored >= PRODIGIOUS_LIMIT) {
			currentTypes.add(PRODIGIOUS_AUTHOR);
		}
		currentTypes.removeAll(previousTypes);
		
		List<Achievement> newAchievements = new ArrayList<Achievement>();
		for(int type: currentTypes){ // newly earned achievements, should only be one
			newAchievements.add(Achievement.create(type, userId));
		}
		return newAchievements;
		
	}
	
	/**
	 * @param userId: id of user who just took a quiz
	 * @param quizId: id of quiz that was just taken
	 * @return
	 */
	public static List<Achievement> newTakerAchievements(int userId, int quizId){
		Set<Integer> previousTypes = Achievement.achievementTypesEarned(Achievement.earnedAchievements(userId));
		Set<Integer> currentTypes = new HashSet<Integer>();
		
		MySql sql = MySql.getInstance();
		String table = MyDBInfo.QUIZ_ATTEMPTS_TABLE;
		String condition = " `user_id` = " + userId + " AND `completed_at` IS NOT NULL";
		int taken = sql.count(table, condition);
		if(taken >= LEGIONARY_LIMIT) {
			currentTypes.add(LEGIONARY);
		}
		
		condition = " `quiz_id` = " + quizId;
		int maxQuizScore = sql.max(table, condition, "score");
		condition = " `quiz_id` = " + quizId + " AND `user_id` = " + userId;
		int maxUserQuizScore = sql.max(table, condition, "score");
		if(maxQuizScore != -1 && maxUserQuizScore == maxQuizScore){
			currentTypes.add(CONQUEROR);
		}
		
		currentTypes.removeAll(previousTypes);
	
		List<Achievement> newAchievements = new ArrayList<Achievement>();
		for(int type: currentTypes){ // newly earned achievements
			newAchievements.add(Achievement.create(type, userId));
		}
		return newAchievements;
		
	}

	
	/**
	 * Retrieves a list of achievements earned by a specified user, identified by the
	 * `userId` parameter, from a database table using a query builder and hydrator.
	 *
	 * @param userId unique identifier for the user whose earned achievements are being
	 * retrieved.
	 *
	 * @returns a list of Achievement objects earned by the specified user.
	 */
	public static List<Achievement> earnedAchievements(int userId) {
		MySql sql = MySql.getInstance();
		QueryBuilder qb = QueryBuilder.selectInstance(TABLE, ACHIEVEMENTS_COLUMNS);
		Model[] models;
		qb.addConstraint("user_id", QueryBuilder.Operator.EQUALS, userId);
		models = sql.getMany(qb, new AchievementHydrator());
		return Arrays.asList(Arrays.copyOf(models, models.length, Achievement[].class));
	}
	
	/**
	 * Extracts unique achievement types from a list of achievements and returns them as
	 * a set. It iterates over the achievements, adding each type to the set, which
	 * automatically eliminates duplicates. The function returns a collection of distinct
	 * achievement types.
	 *
	 * @param achievements collection of achievements from which the function extracts
	 * unique types.
	 *
	 * @returns a set of unique integer values representing earned achievement types.
	 */
	private static Set<Integer> achievementTypesEarned(List<Achievement> achievements){
		Set<Integer> typesEarned = new HashSet<Integer>();
		for(Achievement ach: achievements){
			typesEarned.add(ach.type());
		}
		return typesEarned;
	}
	
	
	/**
	 * Returns the value of the `id` variable.
	 * It has no parameters and does not modify any data.
	 *
	 * @returns an integer value representing an identifier.
	 */
	public int id() {
		return id;
	}
	
	/**
	 * Returns the value of the `type` variable.
	 *
	 * @returns an integer value representing the type.
	 */
	public int type() {
		return type;
	}
	
	/**
	 * Returns the value of the `userId` variable.
	 *
	 * @returns an integer representing the user's ID.
	 */
	public int userId() {
		return userId;
	}
	
	/**
	 * Returns a string representation of an object based on its `type` field, mapping
	 * specific types to predefined string values, and returning `null` for unknown types.
	 *
	 * @returns a string representing the author type, such as "AMATEUR AUTHOR" or "PROLIFIC
	 * AUTHOR".
	 *
	 * The output is a string representing an author type, which can be one of five values:
	 * AMATEUR AUTHOR, PROLIFIC AUTHOR, PRODIGIOUS AUTHOR, LEGIONARY, or CONQUEROR.
	 */
	@Override
	public String toString() {
		switch(type){
			case AMATEUR_AUTHOR:
				return "AMATEUR AUTHOR";
			case PROLIFIC_AUTHOR:
				return "PROLIFIC AUTHOR";
			case PRODIGIOUS_AUTHOR:
				return "PRODIGIOUS AUTHOR";
			case LEGIONARY:
				return "LEGIONARY";
			case CONQUEROR:
				return "CONQUEROR";
			default:
				return null;
		}
	}
	
	/**
	 * Returns an array of strings representing column names for an achievements table.
	 * The actual column names are stored in the `ACHIEVEMENTS_COLUMNS` constant. This
	 * function is likely used for data retrieval or display purposes.
	 *
	 * @returns an array of strings containing column names for achievements.
	 */
	@Override
	public String[] cols() {
		return ACHIEVEMENTS_COLUMNS;
	}
}
