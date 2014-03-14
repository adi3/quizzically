package quizzically.test;
import quizzically.models.*;

import static org.junit.Assert.*;

import org.junit.Test;

import quizzically.models.Quiz;

public class QuizTest {

	@Test
	public void test() {
		Quiz quiz = Quiz.retrieve(19);
		for (Question q: quiz.questions()){
			System.out.println(q.id());
		}
	}

}
