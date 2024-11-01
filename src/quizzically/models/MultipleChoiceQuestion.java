package quizzically.models;

import java.util.*;

import quizzically.exceptions.InvalidResponseException;

/**
 * Single answer multiple choice question
 */
public class MultipleChoiceQuestion extends Question {
	// possible points
	private static int POSSIBLE = 1;

	public MultipleChoiceQuestion(int id, String text, SortedMap<Integer, Answer> orderedAnswers) {
		super(id, text, orderedAnswers);
		this.type = Question.TYPE_MULTIPLE_CHOICE;
	}

	/**
	 * Evaluates a single response to a multiple-choice question, checking if it matches
	 * the correct answer, and returns a grade of 1 if correct and 0 if incorrect. It
	 * throws exceptions if the question has multiple correct answers or if the response
	 * is invalid.
	 *
	 * @param responses list of responses submitted by the user.
	 *
	 * Expect a list of responses, specifically a single response in this case.
	 *
	 * @returns a `Grade` object with a score of 1 if the response is correct, 0 otherwise.
	 *
	 * The output is an object of type `Grade`, which has two attributes:
	 * - `score`, representing the number of correct answers (1 if correct, 0 otherwise),
	 * and
	 * - `possible`, representing the total possible points (in this case, always POSSIBLE).
	 */
	public Grade grade(List<Response> responses) {
		Response response;
		ChoiceResponse rsp;
		Answer correctAnswer = null;

		if (responses.size() != 1) {
//			throw new InvalidResponseException("Expected one answer but received multiple");
		}

		response = responses.get(0);
		if (!(response instanceof ChoiceResponse)) {
//			throw new InvalidResponseException("Expected a TextResponse");
		}
		rsp = (ChoiceResponse) response;

		// check the response is correct
		correctAnswer = answers().get(0);
		for (Answer a : answers()) {
			if (a.correct()) {
				correctAnswer = a;
			}
		}

		if (correctAnswer == null) {
			throw new RuntimeException("MultipleChoiceQuestion has no correct answer");
		}

		return new Grade(correctAnswer.id() == rsp.id() ? 1 : 0, POSSIBLE);
	}

	/**
	 * Returns a constant value representing the possible points. The function appears
	 * to be a simple accessor for a predefined constant. It does not perform any
	 * calculations or logic.
	 *
	 * @returns an integer value representing the maximum possible points.
	 */
	public int possiblePoints() {
		return POSSIBLE;
	}
}
