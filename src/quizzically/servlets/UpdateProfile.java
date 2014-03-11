package quizzically.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.Account;
import quizzically.models.User;

/**
 * Servlet implementation class UpdateProfile
 */
@WebServlet("/UpdateProfile")
public class UpdateProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateProfile() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	/*	String username = (String) request.getSession().getAttribute("user");
		User user = new User(username);
		
		String name = user.getName();
		name = name == null ? "" : name;
		request.setAttribute("name", name);
		
		String email = user.getEmail();
		email = email == null ? "" : email;
		request.setAttribute("email", email);
		
		request.setAttribute("username", username);
		request.getRequestDispatcher("UpdateProfile.jsp").forward(request, response);
	*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Account acc = (Account) this.getServletContext().getAttribute("acc");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String loc = request.getParameter("loc");
		
		String usernameOld = (String) request.getSession().getAttribute("user");
		ArrayList<String> errors = acc.updateAccount(usernameOld, name, email, username, loc);
		if (errors.isEmpty()) {
			request.getSession().setAttribute("user", username);
			request.getSession().setAttribute("name", name);
			response.sendRedirect("Profile");
		} else {
			request.setAttribute("errors", errors);
			request.getRequestDispatcher("UpdateProfile.jsp").forward(request, response); 
		}
	}

}
