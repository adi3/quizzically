package quizzically.models;

import java.util.*;

import quizzically.exceptions.InvalidResponseException;

/**
 * Single answer multiple choice question
 */
public class MultipleChoiceQuestion extends Question {

	public MultipleChoiceQuestion(int id, String text, SortedMap<Integer, Answer> orderedAnswers) {
		super(id, text, orderedAnswers);
		this.type = Question.TYPE_MULTIPLE_CHOICE;
	}

	public int grade(List<Response> responses) {
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
			return -1; // TODO just throw an exception here, wtf happened?
		}

		return correctAnswer.id() == rsp.id() ? 1 : 0;
	}

	public int possiblePoints() {
		return 1;
	}
}
