package quizzically.models;

import java.util.*;

import quizzically.exceptions.InvalidResponseException;
import quizzically.exceptions.ModelException;

/**
 * Regular Question-Response question
 * text question, text answer
 */
public class TextQuestion extends Question {
	// possible points
	private static int POSSIBLE = 1;

	public TextQuestion(int id, String text, SortedMap<Integer, Answer> orderedAnswers) {
		super(id, text, orderedAnswers);
		this.type = Question.TYPE_TEXT;
	}

	/**
	 * Creates an answer for a question, throwing an exception if an answer already exists
	 * for the same question type, then calls the superclass's `createAnswer` method to
	 * create the answer.
	 *
	 * @param correct correctness of the answer being created, indicating whether the
	 * provided texts are accurate or not.
	 *
	 * @param texts set of text values that make up the answer.
	 *
	 * @returns an Answer object, either created by the superclass or a new instance.
	 */
	public Answer createAnswer(boolean correct, Set<String> texts) 
			throws ModelException {
		if (answers().size() != 0) {
			throw new ModelException("There can only be one answer for this type of question");
		}
		return super.createAnswer(correct, texts);
	}

	/**
	 * Checks a single response against the first correct answer in a list of possible
	 * answers and returns a grade of 1 if the response matches any correct answer,
	 * otherwise returns a grade of 0.
	 *
	 * @param responses list of responses to be evaluated.
	 *
	 * Contain a list of Response objects
	 *
	 * @returns a `Grade` object with a score of 1 or 0 and a status of POSSIBLE.
	 *
	 * The output is an instance of the `Grade` class, which has two attributes:
	 * - a score, represented by an integer value, and
	 * - a grade, represented by an enumeration value named `POSSIBLE`.
	 */
	public Grade grade(List<Response> responses) throws InvalidResponseException {
		Response response;
		TextResponse rsp;
		Answer correctAnswer;

		if (responses.size() != 1) {
			throw new InvalidResponseException("Expected one answer but received multiple");
		}

		response = responses.get(0);
		if (!(response instanceof TextResponse)) {
			throw new InvalidResponseException("Expected a TextResponse");
		}
		rsp = (TextResponse) response;

		// check the response is correct
		correctAnswer = answers().get(0);
		for (AnswerText text : correctAnswer.answerTexts()) {
			if (text.text().equals(rsp.text())) {
				return new Grade(1, POSSIBLE);
			}
		}
		return new Grade(0, POSSIBLE);
	}

	/**
	 * Returns the value of the constant `POSSIBLE`, indicating the total number of
	 * possible points.
	 *
	 * @returns an integer value representing the total possible points.
	 */
	public int possiblePoints() {
		return POSSIBLE;
	}
}
