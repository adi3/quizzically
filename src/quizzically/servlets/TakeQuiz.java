package quizzically.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.QuizAttempt;

/**
 * Servlet implementation class TakeQuiz
 */
@WebServlet("/TakeQuiz")
public class TakeQuiz extends BaseServlet implements Servlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public TakeQuiz() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		umbli(request);
		int qAId = getInt(request, "attempt_id");
		if(qAId == 0){ // new attempt
			int qId = getInt(request, "quiz_id");
			QuizAttempt qA = QuizAttempt.create(qId, getUser(request).getId());
			
			
			RequestDispatcher dispatch = request.getRequestDispatcher("ShowQuiz.jsp");
			dispatch.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
