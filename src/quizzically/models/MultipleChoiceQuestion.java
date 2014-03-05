package quizzically.models;

import java.util.Set;

import quizzically.exceptions.InvalidResponseException;

/**
 * Single answer multiple choice question
 */
public class MultipleChoiceQuestion extends Question {

	public MultipleChoiceQuestion(String text) {
		super(text);
		this.type = Question.TYPE_MULTIPLE_CHOICE;
	}

	public int grade(Set<Response> responses) {
		Response response;
		ChoiceResponse rsp;
		Answer correctAnswer;

		if (responses.size() != 1) {
			throw new InvalidResponseException("Expected one answer but received multiple");
		}

		response = responses.get(0);
		if (!(response instanceof ChoiceResponse)) {
			throw new InvalidResponseException("Expected a TextResponse");
		}
		rsp = (ChoiceResponse) response;

		// check the response is correct
		correctAnswer = answers().get(0);
		return correctAnswer.id() == rsp.id() ? 1 : 0;
	}

	public int possiblePoints() {
		return 1;
	}
}
