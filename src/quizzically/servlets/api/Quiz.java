package quizzically.servlets.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.User;

/**
 * Servlet implementation class Quiz
 */
@WebServlet("/api/Quiz")
public class Quiz extends ApiServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user;
		String name, description;
		quizzically.models.Quiz quiz;
		int id;

		umbli(request);
		user = getUser(request);
		id = getInt(request, "id", -1);
		requireFields(request, new String[]{"name", "description"});

		name = getString(request, "name");
		description = getString(request, "description");

		if (id != -1) {
			quiz = quizzically.models.Quiz.retrieve(id);
//			TODO support update
//			quiz.setName(name);
//			quiz.setDescription(description);
//			quiz.save();
		} else {
//			quiz = quizzically.models.Quiz.create(name, user.getId());
		}

		// respond with id
		PrintWriter out = response.getWriter();
//		out.println(quiz.id());
	}
}
