package quizzically.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.Question;
import quizzically.models.User;
import quizzically.servlets.api.ApiServlet;

/**
 * Servlet implementation class Answer
 */
@WebServlet("/Answer")
public class Answer extends ApiServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user;
		String[] texts = {};
		ArrayList<String> cleanTexts = new ArrayList<String>();
		Question question;
		quizzically.models.Answer answer;
		int id, questionId, type, correct;

		umbli(request);
		user = getUser(request);

		id = getInt(request, "id", -1);
		questionId = getInt(request, "question_id");
		correct = getInt(request, "correct", 0);
		texts = getArray(request, "texts");

		question = Question.retrieve(questionId);
		if (question == null) {
			throw new ServletException("Question does not exist");
		}

		for (String text : texts) {
			if (text.equals("")) {
				continue;
			}
			cleanTexts.add(text);
		}

		if (id != -1) {
			answer = quizzically.models.Answer.retrieve(id);
//			TODO support update
		} else {
			try {
				answer = question.createAnswer(correct == 1 ? true : false, cleanTexts);
			} catch (quizzically.exceptions.ModelException e) {
				throw new ServletException(e.getMessage());
			}
		}

		String json = "{\"id\": \"" + answer.id() + "\"}";
		response.getWriter().write(json);
	}

}
