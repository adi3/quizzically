package quizzically.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.Message;
import quizzically.models.User;

/**
 * Servlet implementation class Quiz
 */
@WebServlet("/Quiz")
public class Quiz extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("user") != null) {
			boolean hasUnread = Message.hasUnread((String) request.getSession().getAttribute("user"));
			String msgIcon = hasUnread ? "msg-new.png" : "msg-def.png";
			request.setAttribute("msgIcon", msgIcon);
		}
		
		request.getRequestDispatcher("CreateQuiz.jsp").forward(request, response);	
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user;
		String name, description;
		quizzically.models.Quiz quiz;
		int id, pageFormat, order;

		umbli(request);
		user = getUser(request);
		id = getInt(request, "id", -1);

		name = getString(request, "name");
		description = getString(request, "description");
		pageFormat = getInt(request, "page_format");
		order = getInt(request, "order");

		if (id != -1) {
			quiz = quizzically.models.Quiz.retrieve(id);
//			TODO support update
			quiz.setName(name);
			quiz.setDescription(description);
			quiz.save();
		} else {
			quiz = quizzically.models.Quiz.create(name, 
					user.getId(), description, pageFormat, order);
		}

		String json = "{\"id\": \"" + quiz.id() + "\"}";
		response.getWriter().write(json);
	}
}
