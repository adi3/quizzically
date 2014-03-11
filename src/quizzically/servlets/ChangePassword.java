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
 * Servlet implementation class ChangePassword
 */
@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePassword() {
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
	*/
		request.getRequestDispatcher("ChangePassword.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Account acc = (Account) this.getServletContext().getAttribute("acc");
		String pass = request.getParameter("pass");
		String passConf = request.getParameter("passConf");
		
		String username = (String) request.getSession().getAttribute("user");
		ArrayList<String> errors = acc.updatePassword(username, pass, passConf);
		if (!errors.isEmpty()) {
			String json = "{\"errors\": [";
			for (String err : errors) json += "{ \"msg\":\"" + err + "\"},";
			json = json.substring(0, json.length() - 1) + "]}";
			response.getWriter().write(json);
		} else {
			response.getWriter().write("{}");
		}
	}

}
