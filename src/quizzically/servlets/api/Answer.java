package quizzically.servlets.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.Question;
import quizzically.models.User;

/**
 * Servlet implementation class Answer
 */
@WebServlet("/api/Answer")
public class Answer extends ApiServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user;
		String[] texts = {};
		Set<String> cleanTexts = new HashSet<String>();
		Question question;
		quizzically.models.Answer answer;
		int id, questionId, type;
		boolean correct;

		umbli(request);
		user = getUser(request);

		id = getInt(request, "id", -1);
		questionId = getInt(request, "question_id");
		correct = getInt(request, "correct", 0) == 1;
		texts = getArray(request, "texts");

		question = Question.retrieve(questionId);

		for (String text : texts) {
			// skip empty
			if (text.equals("")) {
				continue;
			}
			cleanTexts.add(text);
		}

		if (id != -1) {
			answer = quizzically.models.Answer.retrieve(id);
			answer.setCorrect(correct);
			answer.setAnswerTextsStrings(cleanTexts);
			answer.save();
		} else {
			try {
				answer = question.createAnswer(correct, cleanTexts);
			} catch (quizzically.exceptions.ModelException e) {
				throw new ServletException(e.getMessage());
			}
		}

		// respond with id
		PrintWriter out = response.getWriter();
		out.println(answer.id());
	}

}
