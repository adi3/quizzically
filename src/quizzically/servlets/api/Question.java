package quizzically.servlets.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.Quiz;
import quizzically.models.User;

/**
 * Servlet implementation class Question
 */
@WebServlet("/api/Question")
public class Question extends ApiServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user;
		String text;
		quizzically.models.Question question;
		Quiz quiz;
		int id, quizId, type;

		umbli(request);
		user = getUser(request);

		id = getInt(request, "id", -1);
		quizId = getInt(request, "quiz_id");
		type = getInt(request, "type");
		text = getString(request, "text");

		quiz = Quiz.retrieve(quizId);
		if (quiz == null) {
			throw new ServletException("Quiz does not exist");
		}

		if (id != -1) {
			question = quizzically.models.Question.retrieve(id);
			question.setType(type);
			question.setText(text);
			question.save();
//			TODO support update
		} else {
			question = quizzically.models.Question.create(text, type);
		}
		quiz.addQuestion(question);

		// respond with id
		PrintWriter out = response.getWriter();
		out.println(question.id());
	}
}
