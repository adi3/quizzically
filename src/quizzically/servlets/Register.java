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
		String username = (String) request.getSession().getAttribute("user");
		if (username != null) {
			response.sendRedirect("Profile");
		} else {
			request.setAttribute("name", "");
			request.setAttribute("email", "");
			request.setAttribute("username", "");
			request.getRequestDispatcher("Register.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Account acc = (Account) this.getServletContext().getAttribute("acc");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		ArrayList<String> errors = acc.createAccount(name, email, username, password, false);
		if (errors.isEmpty()) {
			request.getSession().setAttribute("user", username);	//TODO: check if this should be hashed?
			response.sendRedirect("Profile");
		} else {
			name = name == null ? "" : name;
			request.setAttribute("name", name);
			
			email = email == null ? "" : email;
			request.setAttribute("email", email);
			
			username = username == null ? "" : username;
			request.setAttribute("username", username);
			
			request.setAttribute("errors", errors);
			request.getRequestDispatcher("Register.jsp").forward(request, response); 
		}
	}

}
