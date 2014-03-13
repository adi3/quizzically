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

	public Answer createAnswer(boolean correct, Set<String> texts) 
			throws ModelException {
		if (answers().size() != 0) {
			throw new ModelException("There can only be one answer for this type of question");
		}
		return super.createAnswer(correct, texts);
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
