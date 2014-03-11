package quizzically.servlets.api;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import quizzically.servlets.BaseServlet;

public abstract class ApiServlet extends BaseServlet {

	/**
	 * Get the id parameter from the request
	 * @param request to extract from
	 * @return either the id supplied or -1 if none
	 * @throws ServletException if an invalid id is provided
	 */
	protected int getId(HttpServletRequest request) 
			throws ServletException {
		return getInt(request, "id", -1);
	}

	protected int getInt(HttpServletRequest request, String field) 
			throws ServletException {
				return getInt(request, field, 0, true);
	}

	protected int getInt(HttpServletRequest request, String field, int defaultVal) 
			throws ServletException {
				return getInt(request, field, defaultVal, false);
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

	protected String getString(HttpServletRequest request, String field)
			throws ServletException {
		return getString(request, field, null, true);
	}

	protected String getString(HttpServletRequest request, String field, String defaultVal) 
			throws ServletException {
		return getString(request, field, defaultVal, false);
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

	protected String[] getArray(HttpServletRequest request, String field) 
			throws ServletException {
		String[] strs = request.getParameterValues(field);
		if (strs == null) {
			throw new ServletException("Required field '" + field + 
					"' is missing");
		}
		return strs;
	}

	/**
	 * Require given fields in the request
	 * @param request the request to require the parameters for
	 * @Param fields the parameters to require
	 * @throws ServletException
	 */
	protected void requireFields(HttpServletRequest request, String[] fields) 
			throws ServletException {
		for (String f : fields) {
			if (request.getParameter(f) == null) {
				throw new ServletException("Missing required field '" + f + "'");
			}
		}
	}
}
