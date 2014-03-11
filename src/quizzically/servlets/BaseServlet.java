package quizzically.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import quizzically.models.User;

public abstract class BaseServlet extends HttpServlet {
	/**
	 * Require the user toe be logged in
	 * @throws ServletException if user isn't logged in
	 */
	public void umbli(HttpServletRequest request) throws ServletException {
		String username = (String) request.getSession().getAttribute("user");
		if (username == null) {
			throw new ServletException("You must be logged in to do that");
		}
	}

	/**
	 * Get the logged in user
	 */
	public User getUser(HttpServletRequest request) {
		String username = (String) request.getSession().getAttribute("user");
		return new User(username);
	}

}
