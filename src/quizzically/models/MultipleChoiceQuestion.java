package quizzically.models;

import java.util.*;

import quizzically.exceptions.InvalidResponseException;

/**
 * Single answer multiple choice question
 */
public class MultipleChoiceQuestion extends Question {
	// possible points
	private static int POSSIBLE = 1;

	public MultipleChoiceQuestion(int id, String text, List<Answer> answers) {
		super(id, text, answers);
		this.type = Question.TYPE_MULTIPLE_CHOICE;
	}

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
}
