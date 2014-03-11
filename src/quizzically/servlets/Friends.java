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
 * Servlet implementation class Friends
 */
@WebServlet("/Friends")
public class Friends extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Friends() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Account acc = (Account) this.getServletContext().getAttribute("acc");
		User self = new User((String)request.getSession().getAttribute("user"));
		User friend = new User(request.getParameter("user"));
		String mode = request.getParameter("mode");
		
		if (mode.equals("add")) {
			if (self.addFriend(friend)) {
				String json = "{\"name\": \"" + friend.getName() + "\"}";
				response.getWriter().write(json);
			} else {
				String json = "{\"errors\": [{ \"msg\":\"Trouble adding friend. Please try again.\"}]}";
				response.getWriter().write(json);
			}
		} else if (mode.equals("accept")) {
			if (self.isFriend(friend)) {
				String json = "{\"errors\": [{ \"msg\":\"You are already friends with " + friend.getName() + "\"}]}";
				response.getWriter().write(json);
			} else if (self.acceptRequest(friend)) {
				String json = "{\"name\": \"" + friend.getName() + "\"}";
				response.getWriter().write(json);
			} else {
				String json = "{\"errors\": [{ \"msg\":\"Trouble accepting friend request. Please try again.\"}]}";
				response.getWriter().write(json);
			}
		}
	}

}
