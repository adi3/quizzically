package quizzically.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.Message;
import quizzically.models.User;

/**
 * Servlet implementation class Profile
 */
@WebServlet("/Profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Profile() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionName = null;
		String userId = request.getParameter("id");

		if (userId == null) {
			String username = (String) request.getSession().getAttribute("user");
			userId = new User(username).getId();
			
			if (username != null) {
				sessionName = (String) request.getSession().getAttribute("name");
				if (sessionName == null) request.getSession().setAttribute("name", 
						new User((String) request.getSession().getAttribute("user")).getName());
			}
		}

		if (userId == null) {
			response.sendRedirect("Home");
		} else {		
			User user = User.getUserById(userId);
			
			request.setAttribute("name", user.getName());
			request.setAttribute("email", user.getEmail());
			request.setAttribute("loc", user.getLoc());
			request.setAttribute("img", user.getImg());
			
			if (request.getSession().getAttribute("user") != null) {
				boolean hasUnread = Message.hasUnread((String) request.getSession().getAttribute("user"));
				String msgIcon = hasUnread ? "msg-new.png" : "msg-def.png";
				request.setAttribute("msgIcon", msgIcon);
			}
			
			request.setAttribute("username", user.getUsername());
			request.setAttribute("friends", user.getFriends());

			request.getRequestDispatcher("Profile.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
