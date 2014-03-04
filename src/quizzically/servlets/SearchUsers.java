package quizzically.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.Account;
import quizzically.models.User;

/**
 * Servlet implementation class SearchUsers
 */
@WebServlet("/SearchUsers")
public class SearchUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchUsers() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String param = (String) request.getAttribute("param");
		if (param == null) param = "";
		request.setAttribute("param", param);
		request.getRequestDispatcher("SearchUsers.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = (String) request.getSession().getAttribute("user");
		User user = new User(username);
		
		String param = request.getParameter("param");
		ArrayList<String> errors = new ArrayList<String>();
		
		if (param.isEmpty()) {
			errors.add("Search parameter cannot be empty.");
			request.setAttribute("errors", errors);
		} else {
			ArrayList<User> users = user.search(param, username);
			request.setAttribute("param", param);
			request.setAttribute("users", users);
		}
		request.getRequestDispatcher("SearchUsers.jsp").forward(request, response);
	}

}
