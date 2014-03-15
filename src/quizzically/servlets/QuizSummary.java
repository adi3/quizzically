package quizzically.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.Quiz;
import quizzically.models.QuizAttempt;
import quizzically.models.User;

/**
 * Servlet implementation class QuizSummary
 */
@WebServlet("/QuizSummary")
public class QuizSummary extends BaseServlet implements Servlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public QuizSummary() {
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = getInt(request, "id");
		Quiz quiz;
		User user;
		String avgScore = "";
		QuizAttempt[] userAttempts, highestAllTimeAttempts, 
			highestTodayAttempts, recentAttempts;
		umbli(request);
		quiz = quizzically.models.Quiz.retrieve(id);
		user = getUser(request);

		userAttempts = quiz.userAttempts(user);
		highestAllTimeAttempts = quiz.highestAttempts();
		highestTodayAttempts = quiz.highestTodayAttempts();
		recentAttempts = quiz.recentAttempts();

		avgScore = QuizAttempt.averagePercent(quiz);

		request.setAttribute("quiz", quiz);
		request.setAttribute("user", getUser(request));

		request.setAttribute("userAttempts", userAttempts);
		request.setAttribute("highestAllTimeAttempts", highestAllTimeAttempts);
		request.setAttribute("highestTodayAttempts", highestTodayAttempts);
		request.setAttribute("recentAttempts", recentAttempts);
		request.setAttribute("averageScore", avgScore);
		RequestDispatcher dispatch = request.getRequestDispatcher("QuizSummary.jsp");
		dispatch.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
