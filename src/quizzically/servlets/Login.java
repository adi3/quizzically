package quizzically.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.Account;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = (String) request.getSession().getAttribute("user");
		if (username != null) {
			response.sendRedirect("Profile");
		} else {
			request.setAttribute("username", "");
			request.getRequestDispatcher("LogIn.jsp").forward(request, response); 
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Account acc = (Account) this.getServletContext().getAttribute("acc");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if (acc.checkCredentials(username, password)) {
			request.getSession().setAttribute("user", username);	//TODO: check if this should be hashed?
			response.sendRedirect("Profile");
		} else {
			ArrayList<String> errors = new ArrayList<String>();
			errors.add("Your credentials could not be verified. Please try again.");
			request.setAttribute("errors", errors);
			
			username = username == null ? "" : username;
			request.setAttribute("username", username);
			request.getRequestDispatcher("LogIn.jsp").forward(request, response); 
		}
	}

}
