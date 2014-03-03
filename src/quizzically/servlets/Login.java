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
		request.getRequestDispatcher("LogIn.jsp").forward(request, response); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Account acc = (Account) this.getServletContext().getAttribute("acc");
		String user = request.getParameter("user");
		String pass = request.getParameter("pass");
		
		if (acc.checkCredentials(user, pass)) {
			request.getSession().setAttribute("user", user);	//TODO: check if this should be hashed?
			request.getRequestDispatcher("Welcome.jsp").forward(request, response); 
		} else {
			ArrayList<String> errors = new ArrayList<String>();
			errors.add("Your credentials could not be verified. Please try again.");
			request.setAttribute("errors", errors);
			request.setAttribute("user", user);
			request.getRequestDispatcher("LogIn.jsp").forward(request, response); 
		}
	}

}
