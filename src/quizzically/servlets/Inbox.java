package quizzically.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.Message;
import quizzically.models.User;

/**
 * Servlet implementation class Messages
 */
@WebServlet("/Inbox")
public class Inbox extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Inbox() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = (String) request.getSession().getAttribute("user");
		
		if (username == null) {
			// popup error here
			response.sendRedirect("Home");
		} else {
			User user = new User(username);
			
			boolean hasUnread = Message.hasUnread(username);
			String msgIcon = hasUnread ? "msg-new.png" : "msg-def.png";
			request.setAttribute("msgIcon", msgIcon);
			
			request.setAttribute("name", user.getName());
			
			request.setAttribute("msgs", Message.getMessages(user));
			request.getRequestDispatcher("Inbox.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
