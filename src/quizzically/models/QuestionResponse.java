package quizzically.models;

import java.util.List;
import java.util.ArrayList;

import quizzically.exceptions.InvalidResponseException;

public class QuestionResponse {
	private Question q; //question
	private List<Response> r; //responses
	private int p; //position
	public QuestionResponse(Question question, int position) {
		q = question;
		p = position;
		r = new ArrayList<Response>();
	}

	public void addResponse(int answerId, String answerText) {
		Response rsp = Response.create(q, answerId, answerText);
		r.add(rsp);
	}

	public Question question() {
		return q;
	}

	public Question.Grade grade() throws InvalidResponseException {
		return q.grade(r);
	}

	public int position() {
		return p;
	}

	public String responseString() {
		return ""; // TODO
	}
}
