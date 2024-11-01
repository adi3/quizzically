package quizzically.test;

import static org.junit.Assert.*;
import quizzically.models.*;
import org.junit.Test;
import java.util.*;

/**
 * Contains a single test method that exercises three Achievement factory methods,
 * printing the resulting achievements to the console.
 */
public class AchievementTest {

	/**
	 * Verifies the functionality of three Achievement methods: `newAuthorAchievements`,
	 * `newTakerAchievements`, and `earnedAchievements`, by printing their results for a
	 * given ID (41) and optionally a different ID (73).
	 */
	@Test
	public void test() {
		List<Achievement> achievements = Achievement.newAuthorAchievements(41);
		for(Achievement a: achievements){
			System.out.println(a);
		}
		System.out.println();
		
		achievements = Achievement.newTakerAchievements(41, 73);
		for(Achievement a: achievements){
			System.out.println(a);
		}
		System.out.println();
		
		achievements = Achievement.earnedAchievements(41);
		for(Achievement a: achievements){
			System.out.println(a);
		}
		
		System.out.println("Done.");
	}

}
