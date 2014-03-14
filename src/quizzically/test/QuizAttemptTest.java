package quizzically.test;

import static org.junit.Assert.*;
import quizzically.models.*;
import java.util.*;

import org.junit.Test;

import quizzically.models.QuizAttempt;

public class QuizAttemptTest {
	private static final String TABLE = "quiz_attempts";

	@Test
	public void test() {
		int quizId = 11;
		int userId = 100;
		QuizAttempt attempt = QuizAttempt.create(quizId, userId);
		QuizAttempt retrieved = QuizAttempt.retrieve(attempt.id());
		assertState(retrieved, quizId, userId, -1, attempt.createdAt(), null);
		
		// complete retrieved quiz
		int score = 99;
		retrieved.setCompletedAt(new Date());
		retrieved.setScore(score);
		retrieved.save();
		
		QuizAttempt r2 = QuizAttempt.retrieve(attempt.id());
		assertState(r2, quizId, userId, score, retrieved.createdAt(), retrieved.completedAt());
		
		
		
		
	}
	
	
	private void assertState(QuizAttempt attempt, int quizId, int userId, int score, 
			Date createdAt, Date completedAt){
		assertTrue(attempt.quizId() == quizId);
		assertTrue(attempt.userId() == userId);
		assertTrue(attempt.score() == score);
		System.out.println("existing creation: "+ createdAt.getTime());
		System.out.println("retrieved creation: "+ attempt.createdAt().getTime());
		if(completedAt != null){
			System.out.println("existing completion: "+ createdAt.getTime());
			System.out.println("retrieved completion: "+ attempt.createdAt().getTime());
		}
	}

}