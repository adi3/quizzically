package quizzically.test;

import static org.junit.Assert.*;
import quizzically.models.*;
import org.junit.Test;
import java.util.*;

public class AchievementTest {

	@Test
	public void test() {
		List<Achievement> achievements = Achievement.newAuthorAchievements(100);
		for(Achievement a: achievements){
			System.out.println(a);
		}
		
		achievements = Achievement.newTakerAchievements(100, 11);
		for(Achievement a: achievements){
			System.out.println(a);
		}
		
		System.out.println("Done.");
	}

}
