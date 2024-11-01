package quizzically.test;
import quizzically.models.*;

import static org.junit.Assert.*;

import org.junit.Test;

import quizzically.models.Quiz;

/**
 * Runs unit tests on the Quiz model by retrieving a specific quiz and printing the
 * IDs of its questions.
 */
public class QuizTest {

	/**
	 * Retrieves a quiz by its ID (19), iterates over its questions, and prints the ID
	 * of each question to the console.
	 */
	@Test
	public void test() {
		Quiz quiz = Quiz.retrieve(19);
		for (Question q: quiz.questions()){
			System.out.println(q.id());
		}
	}

}
