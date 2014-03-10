package quizzically.models;

import java.util.*;

import quizzically.exceptions.InvalidResponseException;

/**
 * Regular Question-Response question
 * text question, text answer
 */
public class TextQuestion extends Question {
	public TextQuestion(int id, String text, SortedMap<Integer, Answer> orderedAnswers) {
		super(id, text, orderedAnswers);
		this.type = Question.TYPE_TEXT;
	}

	public int grade(List<Response> responses) throws InvalidResponseException {
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
				return 1;
			}
		}
		return 0;
	}

	public int possiblePoints() {
		return 1;
	}
}
