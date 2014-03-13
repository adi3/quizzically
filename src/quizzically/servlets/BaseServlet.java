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

	protected int getInt(HttpServletRequest request, String field) 
			throws ServletException {
				return getInt(request, field, 0, true);
	}

	protected int getInt(HttpServletRequest request, String field, int defaultVal) 
			throws ServletException {
				return getInt(request, field, defaultVal, false);
	}

	protected String getString(HttpServletRequest request, String field)
			throws ServletException {
		return getString(request, field, null, true);
	}

	protected String getString(HttpServletRequest request, String field, String defaultVal) 
			throws ServletException {
		return getString(request, field, defaultVal, false);
	}

	protected String[] getArray(HttpServletRequest request, String field) 
			throws ServletException {
		String[] strs = request.getParameterValues(field);
		if (strs == null) {
			throw new ServletException("Required field '" + field + 
					"' is missing");
		}
		return strs;
	}

	private int getInt(HttpServletRequest request, String field, int defaultVal, boolean required) 
			throws ServletException {
		int val = defaultVal;
		try {
			String str = getString(request, field, null, required);
			if (str != null) {
				val = Integer.parseInt(str);
			}
		} catch (NumberFormatException e) {
			throw new ServletException("Expected integer for field '" +
				 	field + "'");
		}
		return val;
	}

	private String getString(HttpServletRequest request, String field, String defaultVal, boolean required) 
			throws ServletException {
		String str = request.getParameter(field);
		if (str == null) {
			if (required) {
				throw new ServletException("Required field '" + 
						field + "' is missing");
			} else {
				return defaultVal;
			}
		}
		return str;
	}

}
