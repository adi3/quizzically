package quizzically.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.Account;
import quizzically.models.Message;
import quizzically.models.User;

/**
 * Servlet implementation class Message
 */
@WebServlet("/Messages")
public class Messages extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Messages() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = (String) request.getSession().getAttribute("user");
		String id = (String) request.getParameter("id");
		String mode = (String) request.getParameter("mode");
		
		if (username == null) {
			response.sendRedirect("Login");
		}  else if (mode != null) {
			if (mode.equals("new")) {
				request.setAttribute("friends", new User(username).getFriends());
				request.getRequestDispatcher("CreateMessage.jsp").forward(request, response);
			} else if (mode.equals("reply")) {
				if (id == null) {
					response.sendRedirect("Messages?mode=new");
				} else {
					request.setAttribute("msg", Message.getMessageById(id));
					request.getRequestDispatcher("CreateMessage.jsp").forward(request, response);
				}
			}
		} else if (id != null) {
			User user = new User(username);
			request.setAttribute("name", user.getName());
			
			Message msg = Message.getMessageById(id);
			msg.markRead();
			
			if (msg.getToUser().equals(user)) {
				request.setAttribute("msgId", id);
				request.setAttribute("msg", msg);
				
				request.getRequestDispatcher("Message.jsp").include(request, response);
			} else {
				// growl error here
				response.sendRedirect("Inbox");
			}
		} else {
			response.sendRedirect("Inbox");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = (String) request.getSession().getAttribute("user");
		String to = (String) request.getParameter("to");
		String msg = (String) request.getParameter("msg");
		String challenge = (String) request.getParameter("challenge");
		
		System.out.println(msg);
		String type = "NOTE";
		if (challenge != null) type = "CHALLENGE";
		
		ArrayList<String> errors = new ArrayList<String>();
		User fromUser = new User(username);
		List<String> toArr = Arrays.asList(to.split(","));
		
		for (String toUsername : toArr) {
			if (!Account.accountExists(toUsername.trim())) {
				errors.add("Message not delivered to " + toUsername + ".");
			} else {
				User toUser = new User(toUsername.trim());	
				if (!new Message(msg, type, fromUser, toUser).save()) {
					errors.add("Message not delivered to " + toUsername  + ".");
				}
			}
		}
		
		if (errors.size() == 0) {
			String json = "{\"name\": \"" + fromUser.getName() + "\"}";
			response.getWriter().write(json);
		} else {
			String json = "{\"errors\": [";
			for (String err : errors) json += "{ \"msg\":\"" + err + "\"},";
			json = json.substring(0, json.length() - 1) + "]}";
			response.getWriter().write(json);
		}
	}
	
}
