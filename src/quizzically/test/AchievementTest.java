package quizzically.test;

import static org.junit.Assert.*;
import quizzically.models.*;
import org.junit.Test;
import java.util.*;

public class AchievementTest {

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
