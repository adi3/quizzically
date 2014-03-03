package quizzically.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.Account;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("Register.jsp").forward(request, response); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Account acc = (Account) this.getServletContext().getAttribute("acc");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String user = request.getParameter("user");
		String pass = request.getParameter("pass");
		
		ArrayList<String> errors = acc.createAccount(name, email, user, pass, false);
		if (errors.isEmpty()) {
			request.getSession().setAttribute("user", user);	//TODO: check if this should be hashed?
			request.getRequestDispatcher("Welcome.jsp").forward(request, response); 
		} else {
			request.setAttribute("name", name);
			request.setAttribute("email", email);
			request.setAttribute("user", user);
			
			request.setAttribute("errors", errors);
			request.getRequestDispatcher("Register.jsp").forward(request, response); 
		}
	}

}
