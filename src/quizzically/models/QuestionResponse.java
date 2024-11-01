package quizzically.models;

import java.util.List;
import java.util.ArrayList;

import quizzically.exceptions.InvalidResponseException;

/**
 * Represents a response to a question, allowing multiple responses to be added and
 * providing methods to retrieve the question, grade the responses, and obtain the
 * response string. It encapsulates the question, its position, and the associated responses.
 */
public class QuestionResponse {
	private Question q; //question
	private List<Response> r; //responses
	private int p; //position
	public QuestionResponse(Question question, int position) {
		q = question;
		p = position;
		r = new ArrayList<Response>();
	}

	/**
	 * Adds a new response to a question, and
	 * stores the response in a collection.
	 * The response is created with the question, answer ID, and answer text.
	 *
	 * @param answerId identifier of a response created in the `Response.create` method.
	 *
	 * @param answerText text of the response being added.
	 */
	public void addResponse(int answerId, String answerText) {
		Response rsp = Response.create(q, answerId, answerText);
		r.add(rsp);
	}

	/**
	 * Returns the value of the `q` object.
	 *
	 * @returns an object of type `Question` stored in the variable `q`.
	 */
	public Question question() {
		return q;
	}

	/**
	 * Calculates a grade based on a question and a response. It returns a grade object.
	 * The function throws an exception if the response is invalid.
	 *
	 * @returns the grade of the response, evaluated by the `q.grade(r)` method.
	 */
	public Question.Grade grade() throws InvalidResponseException {
		return q.grade(r);
	}

	/**
	 * Returns the current position or value of the variable `p` as an integer.
	 *
	 * @returns the current position `p` stored in the object.
	 */
	public int position() {
		return p;
	}

	/**
	 * Concatenates elements from a collection `r` into a single string, separating each
	 * element with a comma and a space. It iterates over the collection, appending each
	 * element's text to the string. The result is returned as a string.
	 *
	 * @returns a comma-separated string of text from each element in the collection r.
	 */
	public String responseString() {
		String str = "";
		for (int i = 0; i < r.size(); i++) {
			str += r.get(i).text();
			if (i + 1 < r.size()) {
				str += ", ";
			}
		}
		return str;
	}
}
