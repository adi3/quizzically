package quizzically.models;

import java.util.*;

import quizzically.config.MyDBInfo;
import quizzically.lib.MySql;
import quizzically.lib.QueryBuilder;

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
	
	
	private static Achievement create(int type, int userId) {
		Achievement ach = new Achievement(NULL_VALUE, type, userId);
		ach.save(true); // dehydrates, inserts into DB and sets id to generated key
		return ach;
	}
	
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

	
	public static List<Achievement> earnedAchievements(int userId) {
		MySql sql = MySql.getInstance();
		QueryBuilder qb = QueryBuilder.selectInstance(TABLE, ACHIEVEMENTS_COLUMNS);
		Model[] models;
		qb.addConstraint("user_id", QueryBuilder.Operator.EQUALS, userId);
		models = sql.getMany(qb, new AchievementHydrator());
		return Arrays.asList(Arrays.copyOf(models, models.length, Achievement[].class));
	}
	
	private static Set<Integer> achievementTypesEarned(List<Achievement> achievements){
		Set<Integer> typesEarned = new HashSet<Integer>();
		for(Achievement ach: achievements){
			typesEarned.add(ach.type());
		}
		return typesEarned;
	}
	
	
	public int id() {
		return id;
	}
	
	public int type() {
		return type;
	}
	
	public int userId() {
		return userId;
	}
	
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
	
	@Override
	public String[] cols() {
		return ACHIEVEMENTS_COLUMNS;
	}
}
