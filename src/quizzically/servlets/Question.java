package quizzically.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.Quiz;
import quizzically.models.User;

/**
 * Servlet implementation class Question
 */
@WebServlet("/Question")
public class Question extends BaseServlet {
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

		String json = "{\"id\": \"" + question.id() + "\"}";
		response.getWriter().write(json);
	}
	
	
	/**
	 * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int ques_id = getInt(request, "ques_id");
		int quiz_id = getInt(request, "quizid");
		
		if (!quizzically.models.Question.retrieve(ques_id).delete(quiz_id)) {
			String json = "{\"errors\": [{ \"msg\":\"Unable to delete question. Please try again.\"}]}";
			response.getWriter().write(json);
		}
	}
}
