package quizzically.test;

import static org.junit.Assert.*;
import quizzically.models.*;
import java.util.*;

import org.junit.Test;

import quizzically.models.QuizAttempt;

/**
 * Is a unit test class that verifies the functionality of the QuizAttempt model,
 * covering creation, retrieval, and updating of quiz attempts.
 */
public class QuizAttemptTest {
	private static final String TABLE = "quiz_attempts";

	/**
	 * Tests the functionality of the `QuizAttempt` class, including creation, retrieval,
	 * completion, and updating of quiz attempts. It also tests the `popularQuizzes`
	 * method, which returns a list of popular quizzes.
	 */
	@Test
	public void test() {
		int quizId = 11;
		int userId = 100;
		int position = 0;
		Quiz quiz = Quiz.retrieve(quizId);
		User user = User.retrieve(userId);
		QuizAttempt attempt = QuizAttempt.create(quiz, user);
		QuizAttempt retrieved = QuizAttempt.retrieve(attempt.id());
		assertState(retrieved, quizId, userId, -1, attempt.createdAt(), null, 0);
		
		// complete retrieved quiz
		int score = 99;
		position = 10;
		retrieved.setCompletedAt(new Date());
		retrieved.setScore(score);
		retrieved.setPosition(position);
		retrieved.save();
		
		QuizAttempt r2 = QuizAttempt.retrieve(attempt.id());
		assertState(r2, quizId, userId, score, retrieved.createdAt(), retrieved.completedAt(), retrieved.position());
		
		List<Quiz> popularQuizzes = QuizAttempt.popularQuizzes();
		for(Quiz q: popularQuizzes){
			System.out.println(q.id());
		}
	}
	
	
	/**
	 * Verifies the state of a QuizAttempt object by comparing its properties with expected
	 * values and timestamps. It checks if the object's quiz ID, user ID, score, and
	 * position match the provided values. It also compares the creation and completion
	 * timestamps.
	 *
	 * @param attempt QuizAttempt object being validated, containing details such as quiz
	 * ID, user ID, score, and position.
	 *
	 * Destructure: QuizAttempt attempt
	 * Main properties: quizId, userId, score, position, createdAt, completedAt
	 *
	 * @param quizId expected ID of a quiz to be verified against the ID of a `QuizAttempt`
	 * object.
	 *
	 * @param userId identifier for the user associated with the quiz attempt.
	 *
	 * @param score expected score value for the quiz attempt, which is asserted to be
	 * equal to the actual score obtained from the `attempt.score()` method.
	 *
	 * @param createdAt date when the quiz attempt was created.
	 *
	 * Have
	 * The `createdAt` property is a `Date` object, which represents a point in time.
	 *
	 * @param completedAt completion date of a quiz attempt, which is compared to the
	 * retrieved completion date from the `attempt` object.
	 *
	 * Exist. It is a nullable Date object representing the completion time of a quiz attempt.
	 *
	 * @param position position of the quiz attempt, likely indicating its ranking or order.
	 */
	private void assertState(QuizAttempt attempt, int quizId, int userId, int score, 
			Date createdAt, Date completedAt, int position){
		assertTrue(attempt.quizId() == quizId);
		assertTrue(attempt.userId() == userId);
		assertTrue(attempt.score() == score);
		assertTrue(attempt.position() == position);
		System.out.println("existing creation: "+ createdAt.getTime());
		System.out.println("retrieved creation: "+ attempt.createdAt().getTime());
		if(completedAt != null){
			System.out.println("existing completion: "+ createdAt.getTime());
			System.out.println("retrieved completion: "+ attempt.createdAt().getTime());
		}
	}

}
