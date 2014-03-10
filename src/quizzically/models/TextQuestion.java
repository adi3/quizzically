package quizzically.models;

import java.util.*;

import quizzically.exceptions.InvalidResponseException;

/**
 * Regular Question-Response question
 * text question, text answer
 */
public class TextQuestion extends Question {
	// possible points
	private static int POSSIBLE = 1;

	public TextQuestion(int id, String text, List<Answer> answers) {
		super(id, text, answers);
		this.type = Question.TYPE_TEXT;
	}

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
		for (String text : correctAnswer.answerTexts()) {
			if (text.equals(rsp.text())) {
				return new Grade(1, POSSIBLE);
			}
		}
		return new Grade(0, POSSIBLE);
	}
}
